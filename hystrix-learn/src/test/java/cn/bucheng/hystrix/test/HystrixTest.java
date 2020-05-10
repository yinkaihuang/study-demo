package cn.bucheng.hystrix.test;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yinchong
 * @create 2020/5/10 12:59
 * @description
 */
public class HystrixTest {
    public static class HelloWorldCommand extends HystrixCommand<String> {

        private String name;

        protected HelloWorldCommand(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }

        @Override
        protected String run() throws Exception {
            return "Hello " + name + " thread:" + Thread.currentThread().getName();
        }
    }

    /**
     * 使用命令模式封装依赖逻辑
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void normalHystrixTest() throws InterruptedException, ExecutionException, TimeoutException {
        HelloWorldCommand helloWorldCommand = new HelloWorldCommand("Synchronous-hystrix");
        String result = helloWorldCommand.execute();
        System.out.println("result=" + result);

        helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
        Future<String> future = helloWorldCommand.queue();
        result = future.get(100, TimeUnit.MILLISECONDS);
        System.out.println("result=" + result);
        System.out.println("mainThread=" + Thread.currentThread().getName());
    }


    /**
     * 注册异步事件回调执行
     */
    @Test
    public void observerHystrixTest() throws InterruptedException {
        //注册观察者事件拦截
        Observable<String> fs = new HelloWorldCommand("World").observe();
        //注册结果回调事件
        fs.subscribe(new Action1<String>() {
            @Override
            public void call(String result) {
                //执行结果处理,result 为HelloWorldCommand返回的结果
                //用户对结果做二次处理.
                System.out.println("call:" + result);
            }
        });
        //注册完整执行生命周期事件
        fs.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                // onNext/onError完成之后最后回调
                System.out.println("execute onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // 当产生异常时回调
                System.out.println("onError " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                // 获取结果后回调
                System.out.println("onNext: " + v);
            }
        });

        Thread.sleep(10000);
    }

    static class HelloWorldFallBackCommand extends HystrixCommand<String> {
        private final String name;

        public HelloWorldFallBackCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
                    /* 配置依赖超时时间,500毫秒*/
                    .andCommandPropertiesDefaults(HystrixCommandProperties.defaultSetter().withExecutionTimeoutInMilliseconds(5000)));
            this.name = name;
        }

        @Override
        protected String getFallback() {
            return "exeucute Falled";
        }

        @Override
        protected String run() throws Exception {
            //sleep 1 秒,调用会超时
            TimeUnit.MILLISECONDS.sleep(1000);
            return "Hello " + name + " thread:" + Thread.currentThread().getName();
        }
    }

    /**
     * 添加fallBack
     */
    @Test
    public void hystrixFallBackTest() {
        //重载HystrixCommand 的getFallback方法实现逻辑
        HelloWorldFallBackCommand command = new HelloWorldFallBackCommand("test-Fallback");
        String result = command.execute();
        System.out.println(result);
    }

}

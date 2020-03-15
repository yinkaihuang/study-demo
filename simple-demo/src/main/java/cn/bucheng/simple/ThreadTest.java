package cn.bucheng.simple;
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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yinchong
 * @create 2020/2/26 18:36
 * @description
 */
public class ThreadTest {

    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();
        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printA();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printB();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                printABC.printC();
            }
        }).start();
    }

    static class PrintABC {
        ReentrantLock lock = new ReentrantLock();
        Condition ca = lock.newCondition();
        Condition cb = lock.newCondition();
        Condition cc = lock.newCondition();
        int flag = 0;

        public void printA() {
            try {
                lock.lock();
                while (true) {
                    if (flag != 0) {
                        ca.await();
                    }
                    System.out.println("A");
                    flag = 1;
                    cb.signal();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        public void printB() {
            try {
                lock.lock();
                while (true) {
                    if (flag != 1) {
                        cb.await();
                    }
                    System.out.println("B");
                    flag = 2;
                    cc.signal();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        public void printC() {
            try {
                lock.lock();
                while (true) {
                    if (flag != 2) {
                        cc.await();
                    }
                    System.out.println("C");
                    flag = 1;
                    ca.signal();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }


    }

}

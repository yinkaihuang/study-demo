package cn.bucheng.springmybatisdemo.configuration;

import cn.bucheng.springmybatisdemo.pluging.MyPageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

//@org.springframework.context.annotation.Configuration
public class MapperConfig {
    //将插件加入到mybatis插件拦截链中
    @Bean
    @Order(-Integer.MAX_VALUE)
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                //保证myPlugin1和myPlugin2优先级最低
                MyPageInterceptor myplug = new MyPageInterceptor();
                try {
                    Field field = Configuration.class.getDeclaredField("interceptorChain");
                    field.setAccessible(true);
                    InterceptorChain chainList = (InterceptorChain) field.get(configuration);
                    List<Interceptor> interceptors = chainList.getInterceptors();
                    LinkedList<Interceptor> tempList = new LinkedList<>();
                    tempList.add(myplug);
                    tempList.addAll(interceptors);
                    Field chainField = InterceptorChain.class.getDeclaredField("interceptors");
                    chainField.setAccessible(true);
                    List<Interceptor> interceptorList = (List<Interceptor>) chainField.get(chainList);
                    interceptorList.clear();
                    interceptorList.addAll(tempList);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        };
    }
}
package cn.bucheng.springmybatisdemo.configuration;

import cn.bucheng.springmybatisdemo.pluging.MyPageInterceptor;
import cn.bucheng.springmybatisdemo.pluging.MyResultInterceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MapperConfig {
    //将插件加入到mybatis插件拦截链中
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                MyPageInterceptor myPlugin1 = new MyPageInterceptor();
                MyResultInterceptor myPlugin2 = new MyResultInterceptor();
                configuration.addInterceptor(myPlugin1);
                configuration.addInterceptor(myPlugin2);
            }
        };
    }
}
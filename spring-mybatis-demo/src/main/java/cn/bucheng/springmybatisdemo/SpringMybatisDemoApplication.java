package cn.bucheng.springmybatisdemo;

import cn.bucheng.springmybatisdemo.annotation.EachDataSource;
import cn.bucheng.springmybatisdemo.annotation.EnableMuchDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("cn.bucheng.springmybatisdemo.mapper")
@EnableMuchDataSource(prefix = "custom.mysql",value = {
        @EachDataSource(dbPrefix = "db0",mapperScanPackages = "cn.bucheng.springmybatisdemo.mapper",typeAliasesPackage = "cn.bucheng.springmybatisdemo.domain",mapperLocations = "classpath:mapper/*.xml"),
        @EachDataSource(dbPrefix = "db1",mapperScanPackages = "cn.bucheng.springmybatisdemo.dao",typeAliasesPackage = "cn.bucheng.springmybatisdemo.entity",mapperLocations = "classpath:mapper2/*.xml")
})
public class SpringMybatisDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisDemoApplication.class, args);
    }

}

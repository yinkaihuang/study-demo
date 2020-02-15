package cn.bucheng.springmybatisdemo;

import cn.bucheng.springmybatisdemo.configuration.TestConfiguration;
import cn.bucheng.springmybatisdemo.dao.UserDao;
import cn.bucheng.springmybatisdemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootTest
class SpringMybatisDemoApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TestConfiguration testConfiguration;
    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        BindResult<TestConfiguration> bind = Binder.get(environment).bind("yinchong.testname", TestConfiguration.class);
        System.out.println(bind.get());
//        System.out.println(testConfiguration);
//        System.out.println(testConfiguration);
//        PageHelper.startPage(1, 20);
//        Page<User> users = userMapper.pageAll();
//        System.out.println(users.getResult());
//        PageHelper.startPage(1, 20);
//        Page<UserEntity> userRecord = userDao.pageAll();
//        System.out.println(userRecord.getResult());


//        List<User> userList = userMapper.findLimit();
//        System.out.println(userList);
//        userList = userMapper.findAll();
//        System.out.println(userList);
    }

}

package cn.bucheng.springmybatisdemo;

import cn.bucheng.springmybatisdemo.dao.UserDao;
import cn.bucheng.springmybatisdemo.domain.User;
import cn.bucheng.springmybatisdemo.entity.UserEntity;
import cn.bucheng.springmybatisdemo.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringMybatisDemoApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
//        BindResult<TestConfiguration> bind = Binder.get(environment).bind("yinchong.testname", TestConfiguration.class);
//        System.out.println(bind.get());

        PageHelper.startPage(1, 20);
        Page<User> users = userMapper.pageAll();
        System.out.println(users.getResult());
        PageHelper.startPage(1, 20);
        Page<UserEntity> userRecord = userDao.pageAll();
        System.out.println(userRecord.getResult());

//        List<User> all = userMapper.findAll();
//        System.out.println(all);


//        List<User> userList = userMapper.findLimit();
//        System.out.println(userList);
//        userList = userMapper.findAll();
//        System.out.println(userList);
    }

}

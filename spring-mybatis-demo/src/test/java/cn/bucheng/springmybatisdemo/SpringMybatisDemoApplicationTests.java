package cn.bucheng.springmybatisdemo;

import cn.bucheng.springmybatisdemo.domain.User;
import cn.bucheng.springmybatisdemo.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringMybatisDemoApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        PageHelper.startPage(1, 20);
        Page<User> users = userMapper.pageAll();
        System.out.println(users.getResult());
        List<User> userList = userMapper.findLimit();
        System.out.println(userList);
        userList = userMapper.findAll();
        System.out.println(userList);
    }

}

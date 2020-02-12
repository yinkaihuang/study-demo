package cn.bucheng.mybatis.xml;

import cn.bucheng.mybatis.domain.User;
import cn.bucheng.mybatis.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class UserMapperTest {
    InputStream is = null;
    SqlSessionFactory factory = null;
    SqlSession session = null;

    @Before
    public void init() throws IOException {
        //读取配置文件
        is = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        factory = new SqlSessionFactoryBuilder().build(is);
        //生产SQLSession对象
        session = factory.openSession();
    }

    @After
    public void close() throws IOException {
        session.commit();
        session.close();
        is.close();
    }

    @Test
    public void findAllTest() {
        UserMapper userDao = session.getMapper(UserMapper.class);
        PageHelper.startPage(1, 3);
        Page<User> page = userDao.pageAll();
        for (User user : page.getResult()) {
            System.out.println(user);
        }

//        List<Object> objects = session.selectList("cn.bucheng.mybatis.mapper.UserMapper.findLimit");
//        System.out.println(objects);
//        List<Object> userList = session.selectList("cn.bucheng.mybatis.mapper.UserMapper.findAll");
//        System.out.println(userList);
    }

}
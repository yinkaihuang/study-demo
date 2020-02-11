package cn.bucheng.mybatis.dao;

import cn.bucheng.mybatis.domain.User;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * ，用户的持久层接口
 */
public interface UserDao {

    List<User> findAll();

    Page<User> pageAll();

    List<User> findLimit();

}
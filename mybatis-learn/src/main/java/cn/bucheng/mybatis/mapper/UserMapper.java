package cn.bucheng.mybatis.mapper;

import cn.bucheng.mybatis.domain.User;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * ，用户的持久层接口
 */
public interface UserMapper {

    List<User> findAll();

    Page<User> pageAll();

    List<User> findLimit();

}
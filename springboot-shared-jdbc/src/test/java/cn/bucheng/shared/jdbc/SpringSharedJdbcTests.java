package cn.bucheng.shared.jdbc;


import cn.bucheng.shared.jdbc.domain.Order;
import cn.bucheng.shared.jdbc.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSharedJdbcTests {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    void contextLoads() {
        List<Order> orders = orderMapper.listAll();
        System.out.println(orders.size());
    }

}

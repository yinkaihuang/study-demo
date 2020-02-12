package cn.bucheng.shared.jdbc;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.bucheng.shared.jdbc.util.DataSourceUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

/**
 * @author yinchong
 * @create 2020/2/12 11:10
 * @description
 */
public class SharedJdbcTest {

    @Test
    public void queryAllTest()throws Exception{
        DataSource dataSource = DataSourceUtils.getDataSource();
        Connection connection = dataSource.getConnection();
        String sql = "select * from t_order order by order_id desc limit 0,100";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int order_id = rs.getInt("order_id");
            int user_id = rs.getInt("user_id");
            System.out.println("order_id:"+order_id+" user_id:"+user_id);
        }

    }

    @Test
    public void queryByOrderIdTest()throws Exception{
        int orderId = 647671;
        DataSource dataSource = DataSourceUtils.getDataSource();
        Connection connection = dataSource.getConnection();
        String sql = "select * from t_order where  order_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,orderId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int order_id = rs.getInt("order_id");
            int user_id = rs.getInt("user_id");
            System.out.println("order_id:"+order_id+" user_id:"+user_id);
        }

    }

    @Test
    public void queryByOrderIdAndUserIdTest()throws Exception{
        int orderId = 647671;
        int userId = 264;
        DataSource dataSource = DataSourceUtils.getDataSource();
        Connection connection = dataSource.getConnection();
        String sql = "select * from t_order where user_id = ? and order_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,userId);
        ps.setInt(2,orderId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int order_id = rs.getInt("order_id");
            int user_id = rs.getInt("user_id");
            System.out.println("order_id:"+order_id+" user_id:"+user_id);
        }

    }

    @Test
    public void saveTest() throws Exception {
        Random random = new Random();
        DataSource dataSource = DataSourceUtils.getDataSource();
        for (int i = 1; i < 1000; i++) {
            Connection conn = dataSource.getConnection();
            String sql = "insert into t_order(order_id,user_id) values(?,?)";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, random.nextInt(10000000));
            prep.setInt(2, i);
            prep.execute();
            prep.close();
            conn.close();
        }
    }
}

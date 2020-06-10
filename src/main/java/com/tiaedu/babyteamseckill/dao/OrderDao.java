package com.tiaedu.babyteamseckill.dao;

import com.tiaedu.babyteamseckill.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDao {

    void insert(Order order);

    Order findByOrderNo(@Param("orderNo") String orderNo);

}

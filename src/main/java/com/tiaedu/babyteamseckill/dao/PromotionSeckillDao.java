package com.tiaedu.babyteamseckill.dao;

import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface PromotionSeckillDao {

    List<PromotionSeckill> findUnstartedSeckill();
    void update(PromotionSeckill promotionSeckill);
    PromotionSeckill findById(@Param("id") Long id);

}

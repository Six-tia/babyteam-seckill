package com.tiaedu.babyteamseckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PromotionSeckill {

    private Long psId;
    private Long goodsId;
    private Integer psCount;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Float currentPrice;

}

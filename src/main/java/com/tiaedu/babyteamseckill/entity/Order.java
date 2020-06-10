package com.tiaedu.babyteamseckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {

    private Long orderId;
    private String orderNo;
    private Integer orderStatus;
    private String userId;
    private String recvName;
    private String recvAddress;
    private String recvMobile;
    private Float Postage;
    private Float amount;
    private Date createTime;

}

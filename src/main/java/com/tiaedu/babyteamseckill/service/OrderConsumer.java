package com.tiaedu.babyteamseckill.service;

import com.tiaedu.babyteamseckill.dao.OrderDao;
import com.tiaedu.babyteamseckill.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import com.rabbitmq.client.Channel;
import java.util.Date;
import java.util.Map;

@Component
public class OrderConsumer {

    @Resource
    private OrderDao orderDao;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-order"),
                    exchange = @Exchange(value = "exchange-order", type = "fanout")
            )
    )
    @RabbitHandler
    //@Payload:表示消息的主体数据
    //@Header:表示消息的头信息
    public void handleMessage(@Payload Map data, Channel channel, @Headers Map<String,Object> headers){
        System.out.println("======get order message=======" + data);

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        Order order = new Order();
        order.setOrderNo(data.get("orderNo").toString());
        order.setOrderStatus(0);
        order.setUserId(data.get("userId").toString());
        order.setRecvName("tia");
        order.setRecvMobile("1347236789");
        order.setRecvAddress("dshfisdf");
        order.setAmount(19.8f);
        order.setPostage(0f);
        order.setCreateTime(new Date());
        orderDao.insert(order);
        Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        try{
            channel.basicAck(tag, false);
            System.out.println(data.get("orderNo") + " order is created!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @RabbitListener(queuesToDeclare = @Queue("work"))
    @RabbitHandler
    //@Payload:表示消息的主体数据
    //@Header:表示消息的头信息
    public void handleMessage1(@Payload Map data, Channel channel, @Headers Map<String,Object> headers){
        System.out.println("======get order message=======" + data);

        try{
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        Order order = new Order();
        order.setOrderNo(data.get("orderNo").toString());
        order.setOrderStatus(0);
        order.setUserId(data.get("userId").toString());
        order.setRecvName("tia");
        order.setRecvMobile("1347236789");
        order.setRecvAddress("dshfisdf");
        order.setAmount(19.8f);
        order.setPostage(0f);
        order.setCreateTime(new Date());
        orderDao.insert(order);
        Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        try{
            channel.basicAck(tag, false);
            System.out.println(data.get("orderNo") + " order is created!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

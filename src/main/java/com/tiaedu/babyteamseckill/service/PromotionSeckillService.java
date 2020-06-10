package com.tiaedu.babyteamseckill.service;

import com.tiaedu.babyteamseckill.dao.OrderDao;
import com.tiaedu.babyteamseckill.dao.PromotionSeckillDao;
import com.tiaedu.babyteamseckill.entity.Order;
import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import com.tiaedu.babyteamseckill.service.exception.SeckillException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PromotionSeckillService {

    @Resource
    private PromotionSeckillDao promotionSeckillDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderDao orderDao;

    //num:每个用户每次抢购的最大数目
    public void processSeckill(Long id, String userId, Integer num) throws SeckillException {
        PromotionSeckill promotionSeckill = promotionSeckillDao.findById(id);
        if(promotionSeckill == null){
            throw new SeckillException("seckill activity does not exist!");
        }
        if(promotionSeckill.getStatus() == 0){
            throw new SeckillException("seckill activity is not start!");
        }
        if(promotionSeckill.getStatus() == 2){
            throw new SeckillException("seckill activity is already finished!");
        }

        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop("seckill:count:" + promotionSeckill.getPsId());
        if(goodsId != null){
            //判断是否已经抢购过
            boolean isExist = redisTemplate.opsForSet().isMember("seckill:users:" + promotionSeckill.getPsId(), userId);
            if(!isExist){
                System.out.println("Congratulation!");
                redisTemplate.opsForSet().add("seckill:users:" + promotionSeckill.getPsId(), userId);
            }else{
                redisTemplate.opsForList().rightPush("seckill:count:" + promotionSeckill.getPsId(), promotionSeckill.getGoodsId());
                throw new SeckillException("You have already joined this activity!");
            }
        }else{
            throw new SeckillException("This good is already sold out, please try next time!");
        }

    }

    public String sendOrderToQueue(String userId){
        System.out.println("Ready to send message to queue!");
        Map data = new HashMap();
        data.put("userId",userId);
        String orderNo = UUID.randomUUID().toString();
        data.put("orderNo", orderNo);
        //可附加额外的订单信息
        //。。。。。
        rabbitTemplate.convertAndSend("exchange-order", null, data);
        return orderNo;
    }

    public String sendOrderToQueue1(String userId){
        System.out.println("Ready to send message to queue!");
        Map data = new HashMap();
        data.put("userId",userId);
        String orderNo = UUID.randomUUID().toString();
        data.put("orderNo", orderNo);
        //可附加额外的订单信息
        //。。。。。
        rabbitTemplate.convertAndSend("work", data);
        return orderNo;
    }

    public Order checkOrder(String orderNo){
        Order order = orderDao.findByOrderNo(orderNo);
        return order;
    }


}
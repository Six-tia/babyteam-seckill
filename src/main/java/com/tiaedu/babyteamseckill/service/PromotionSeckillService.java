package com.tiaedu.babyteamseckill.service;

import com.tiaedu.babyteamseckill.dao.PromotionSeckillDao;
import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import com.tiaedu.babyteamseckill.service.exception.SeckillException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PromotionSeckillService {

    @Resource
    private PromotionSeckillDao promotionSeckillDao;

    @Resource
    private RedisTemplate redisTemplate;

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

}

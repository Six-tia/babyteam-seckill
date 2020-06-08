package com.tiaedu.babyteamseckill.scheduler;

import com.tiaedu.babyteamseckill.dao.PromotionSeckillDao;
import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeckillTask {

    @Resource
    private PromotionSeckillDao promotionSeckillDao;

    //Spring封装的Redis操作类
    //底层默认采用jdk序列化的方式（会产生乱码，需要修改）
    @Resource
    private RedisTemplate redisTemplate;

    //public void  xxx(){}
    //@Scheduled(cron = "* * * * * ?"): 每秒钟执行一次
    //@Scheduled(cron = "0 * * * * ?"): 每分钟(第0秒)执行一次
    //@Scheduled(cron = "0,15,30,45 0 2 1 * ?"): 每月1日2:00的0，15，30，45秒各执行一次
    //秒 分 小时 日 月 星期
    // *：表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。
    // ?：只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和 DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。
    // -：表示范围，例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次
    // /：表示起始时间开始触发，然后每隔固定时间触发一次，例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次.
    // ,：表示列出枚举值值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
    @Scheduled(cron = "0/5 * * * * ?")
    public void startSeckill(){
        List<PromotionSeckill> list = promotionSeckillDao.findUnstartedSeckill();
        System.out.println("test");
        System.out.println(list);
        for(PromotionSeckill ps : list){
            System.out.println(ps.getPsId() + "seckill activity is start!");
            //删掉以前重复的活动任务缓存
            redisTemplate.delete("seckill:count:" + ps.getPsId());
            redisTemplate.delete("seckill:users:" + ps.getPsId());

            //按照库存数量添加进list
            for(int i = 0; i < ps.getPsCount(); i++){
                System.out.println("add");
                redisTemplate.opsForList().rightPush("seckill:count:" + ps.getPsId(), ps.getGoodsId());
            }
            ps.setStatus(1);
            promotionSeckillDao.update(ps);
        }
    }
}

package com.tiaedu.babyteamseckill.controller;

import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import com.tiaedu.babyteamseckill.service.PromotionSeckillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PromotionSeckillController {

    @Resource
    private PromotionSeckillService promotionSeckillService;

    @RequestMapping("/seckill")
    @ResponseBody
    public Map processSeckill(Long psId, String userId){
        Map result = new HashMap();
        try{
            promotionSeckillService.processSeckill(psId, userId,1);
            result.put("code", "0");
            result.put("message", "success");
        }catch (Exception e){
            result.put("code", "500");
            result.put("message", "error");
        }
        return result;
    }

}

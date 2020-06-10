package com.tiaedu.babyteamseckill.controller;

import com.tiaedu.babyteamseckill.entity.Order;
import com.tiaedu.babyteamseckill.entity.PromotionSeckill;
import com.tiaedu.babyteamseckill.service.PromotionSeckillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PromotionSeckillController {

    @Resource
    private PromotionSeckillService promotionSeckillService;

    @RequestMapping("/seckill")
    @ResponseBody
    public Map processSeckill(Long psId, String userId) {
        Map result = new HashMap();
        try {
            Map data = new HashMap();
            promotionSeckillService.processSeckill(psId, userId, 1);
            String orderNo = promotionSeckillService.sendOrderToQueue1(userId);
            data.put("orderNo", orderNo);
            result.put("code", "0");
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "error");
        }
        return result;
    }

    @GetMapping("/checkorder")
    public ModelAndView checkOrder(String orderNo) {
        Order order = promotionSeckillService.checkOrder(orderNo);
        ModelAndView modelAndView = new ModelAndView();
        if(order != null){
            modelAndView.addObject("order", order);
            modelAndView.setViewName("/order");
        }else{
            modelAndView.addObject("orderNo", orderNo);
            modelAndView.setViewName("/waiting");
        }
        return modelAndView;
    }

}

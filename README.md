# 超卖问题

*Redis*&nbsp;  *单线程模型*&nbsp;  *高并发*

---

模拟高并发环境下，固定数量商品的超卖（超发）现象及处理方法。

## Redis ##
 1. 单线程模型
 2. 内存存储高
 3. 天生分布式支持
### 解决思路 ###
 1. Redis List类型存储商品库存
    格式：key:seckill:count:secid list:商品编号
 2. Redis Set类型存储秒杀成功用户
    格式：key:seckill:users:secid set:秒杀成功用户
 3. list每删除一个商品（value），set中要多一个用户（value） 

即每个商品被秒杀，list数量-1，set数量+1

### 整合SpringBoot项目 ###
1. RestTemplate使用（修改序列化方式解决乱码问题）
2. 获取到秒杀活动后，删去redis的缓存，重新键入商品数据到list中
3. 设计Exception类，处理各种异常情况
4. JMeter压力测试

# MQ流量削峰

*RabbitMQ*

---

##流量冲击示意
秒杀用户 --> 用户确权（1000/s）--> 订单系统（200/s）
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(同步等待）

##流量削峰示意
秒杀用户 --> 用户确权（1000/s）--> MQ <-- 订单系统（200/s）
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(异步处理）

##实现思路
1. 用户秒杀到商品（list-1，set+1），将数据存储到队列中
2. 使用工厂模式，设置prefetch值，orderConsumer进行异步消费
3. 引入sweetalert控件，实现秒杀成功后，提示等待和等待一段时间后成功与失败的不同处理




 

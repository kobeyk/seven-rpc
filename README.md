# seven-rpc

#### 介绍

```
    用7这个数字，是因为我儿子是七夕生的。开源的rpc框架有很多，公司的、个人的，优秀的很多。那为什么我又要写个呢？
因为spring、springboot、因为分布式服务协调器zk、因为nio，或者说是因为netty。看了spring的源码，也clone了项目
调了很长一段时间，总想上手一个"框架"来练练手，期间也手写过简易版的mybaits、spring-mvc框架，很不过瘾。用netty
也编码了一段时间，总感觉少了点什么，于是乎，便决定利用平时空闲之余，串联下几大技术，自己也写一个rpc框架，切身感受
一下站在巨人肩膀上撸代码的兴奋和快感！

    首先一个rpc框架基本该有的功能都得有吧，其次模块得鲜明、代码思路得清晰，让人一看就懂：“哦吼，原来
Spring的BeanPostProcessor可以用的这么嗨皮啊；原来反射写起来也可以让人爱不释手啊；原来动态代理的时机可以结合bean的
生命周期来玩啊；原来接口类型的bean注入时也是可以往ioc容器里注册beandefinition的啊；原来FactoryBean是这样用的啊；
原来netty的ByteBuf居然这么好用的啊；原来消息编·解码器这么写就可以解决TCP的拆包或粘包的问题啊；原来netty api用起来
是如此轻松嗨皮的啊；原来用zk搭建一个rpc服务的注册与发现中心只需一个工具类和为数不多行的代码就可以轻松办到的啊...etc”

```

#### 软件架构

![Seven-Rpc架构图-简](https://gitee.com/appleyk/seven-rpc/raw/master/src/main/resources/static/images/%E6%9E%B6%E6%9E%841.png)

![Seven-Rpc架构图-详](https://gitee.com/appleyk/seven-rpc/raw/master/src/main/resources/static/images/%E6%9E%B6%E6%9E%842.jpg)



#### 安装`使用教程

1.  git clone https://gitee.com/appleyk/seven-rpc
2.  idea导入父pom，配置maven，并下载相关依赖
3.  idea编译项目，检查是否报错
4.  运行rpc-use-case模块中对应的案例启动类即可
5.  具体使用可参考博客：https://blog.csdn.net/Appleyk/article/details/117392129

#### 模块说明

1. rpc-common  : 定义通用工具类和netty消息编`解码器
2. rpc-core    : 定义核心业务模型和公共自定义注解re
3. rpc-registry: 服务注册与发现中心模块，主要是基于zookeeper curator框架实现的
4. rpc-client  : rpc-netty客户端模块，主要对接口进行动态代理，并封装请求，与rpc-netty服务端建立连接并进行通信
5. rpc-server  : rpc-netty服务端模块，主要对接口实现模块中的可用服务进行一个注册，信息写入到zk中，同时启动一个netty服务，对客户端连接进行监听
6. rpc-use-case : rpc功能测试用例模块，其又分为3个模块
 6.1. rpc-demo-api: 定义公共接口
 6.2. rpc-demo-consumer: 服务消费方，主要用来测试rpc服务能力的，依赖rpc-demo-api模块
 6.3. rpc-demo-provider: 服务提供方，用来满足rpc接口功能实现的，依赖rpc-demo-api模块

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
5.  如果你提交的request对项目有用，我会merge的


#### 番外

1. Spring·Bean全生命周期（简图）

![Spring·Bean完整生命周期](https://gitee.com/appleyk/seven-rpc/raw/master/src/main/resources/static/images/Spring%C2%B7Bean%E5%AE%8C%E6%95%B4%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)

2. ZK注册的服务和API节点

![ZK服务节点](https://gitee.com/appleyk/seven-rpc/raw/master/src/main/resources/static/images/ZK%E6%B3%A8%E5%86%8C%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%92%8CAPI%E8%8A%82%E7%82%B9.jpg)

3. Netty Reactor模型

![Netty Reactor设计模型](https://gitee.com/appleyk/seven-rpc/raw/master/src/main/resources/static/images/netty%C2%B7reactor.png)

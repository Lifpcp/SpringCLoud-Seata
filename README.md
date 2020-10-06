# SpringCLoud-Seata

#### 介绍
分布式事务组件seata的使用demo，AT模式、TCC模式，集成springboot、springcloud（nacos注册中心、openFeign服务调用、Ribbon负载均衡器）、spring jpa，数据库采用mysql

demo中使用的相关版本号，具体请看代码。如果搭建个人demo不成功，验证是否是由版本导致，版本稍有变化可能出现相关组件的版本不一致便会出现许多奇怪问题

seata服务端 1.3
Nacos服务端 1.1.4
spring-cloud-alibaba-dependencies 2.1.0.RELEASE
springboot 2.1.3.RELEASE
springcloud Greenwich.RELEASE

#### 软件架构
软件架构说明
  springcloud-common        公共模块
  springcloud-order-AT      订单服务 
  springcloud-product-AT    商品库存服务 
  springcloud-consumer-AT   消费调用者
  springcloud-business-Tcc  工商银行服务
  springcloud-merchants-Tcc 招商银行服务
  springcloud-Pay-Tcc       消费调用者
  
AT模式：springcloud-order-AT，springcloud-product-AT，springcloud-consumer-AT为AT模式Dome；模拟场景用户购买商品下单；
调用流程springcloud-consumer-AT调用订单服务创建订单（新增一条数据到订单表）；在调用商品库存服务扣减商品库存数量（修改商品库存表商品数量）；最后出现异常则统一回滚，负责统一提交；

第一阶段：准备阶段（prepare）协调者通知参与者准备提交订单，参与者开始投票。协调者完成准备工作向协调者回应Yes。
第二阶段：提交(commit)/回滚(rollback)阶段协调者根据参与者的投票结果发起最终的提交指令。如果有参与者没有准备好则发起回滚指令。

![输入图片说明](https://images.gitee.com/uploads/images/2020/1006/112439_f799d202_1340534.png "屏幕截图.png")

1.应用程序通过事务协调器向两个库发起prepare，两个数据库收到消息分别执行本地事务，但不提交，如果执行成功则回复yes，否则回复no。
2.事务协调器收到回复，只要有一方回复no则分别向参与者发起回滚事务，参与者开始回滚事务。
3.事务协调器收到回复，全部回复yes，此时向参与者发起提交事务。如果参与者有一方提交事务失败则由事务协调器发起回滚事务。




TCC模式：springcloud-business-Tcc，springcloud-merchants-Tcc，springcloud-Pay-Tcc为TCC模式Dome；模拟场景用户转账由工商银行转钱到招商银行；调用流程springcloud-Pay-Tcc调用工商银行服务，第一阶段Try：工商账号进行余额扣减转入冻结金额加钱；在调用招商银行服务进行招商账号冻结金额加钱；第二阶段Confirm或者Cancel：工商账号冻结余额扣减（第一阶段转入的）；招商账户冻结金额扣钱、账号余额加钱；Cancel回滚相关数据；

TCC事务补偿是基于2PC实现的业务层事务控制方案，它是Try（准备）、Confirm（提交）和Cancel（回滚）三个单词的首字母，含义如下：
1、Try 检查及预留业务资源完成提交事务前的检查，并预留好资源。
2、Confirm 确定执行业务操作，对try阶段预留的资源正式执行。
3、Cancel 取消执行业务操作，对try阶段预留的资源释放。

![输入图片说明](https://images.gitee.com/uploads/images/2020/1006/112340_73f89905_1340534.png "屏幕截图.png")


#### 服务端配置

 **可以直接用项目中的nacos、Seata多是直接配置好的，可以直接启动注意启动顺序；配置初始化** 

1. Nacos-server
    版本为nacos-server-1.1.4，demo采用本地单机部署方式，请参考 https://nacos.io/zh-cn/docs/quick-start.html
2. Seata-server
    seata-server为release版本1.3，demo采用本地单机部署，从此处下载 https://github.com/seata/seata/releases 并解压
3. 修改conf/registry.conf 配置
    设置type、设置serverAddr为你的nacos节点地址。

     **注意这里有一个坑，serverAddr不能带‘http://’前缀; seata-server不同版本可能配置会存在稍微不同** 

```
        registry {
          # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
          type = "nacos"
        
          nacos {
            application = "serverAddr"
            serverAddr = "127.0.0.1:8848"
            # 如果不指定group则默认是SEATA_GROUP 需和业务服务程序保持同一个分组下
            group = "SEATA_GROUP"
            namespace = ""
            cluster = "default"
            username = "nacos"
            password = "nacos"
          }
        }
        
        config {
          # file、nacos 、apollo、zk、consul、etcd3
          type = "nacos"
        
          nacos {
            serverAddr = "127.0.0.1:8848"
            namespace = ""
            group = "SEATA_GROUP"
        	cluster = "default"
            username = "nacos"
            password = "nacos"
          }
        }
 ```
4. 修改conf/nacos-config.txt 配置
       service.vgroup_mapping.your−service−gruop=default，中间的{your-service-gruop}为自己定义的服务组名称，
       服务中的bootstrap.yml文件里配置服务组名称。

        **注意：在1.3版本中service.vgroup_mapping变为service.vgroupMapping** 

       Demo中的的服务配置如下：
   
        service.vgroupMapping.spring-cloud-consumer-service=default
        service.vgroupMapping.spring-cloud-order-service=default
        service.vgroupMapping.spring-cloud-product-service=default
        service.vgroupMapping.spring-cloud-business-service=default
        service.vgroupMapping.spring-cloud-merchants-service=default
        service.vgroupMapping.spring-cloud-pay-service=default

        修改数据库配置：（可以更换其他数据库比如orac）
         
        store.db.datasource=dbcp
        store.db.db-type=mysql
        store.db.driver-class-name=com.mysql.jdbc.Driver
        store.db.url=jdbc:mysql://127.0.0.1:3333/seata?useUnicode=true
        store.db.user=root
        store.db.password=root
        store.db.min-conn=1
        store.db.max-conn=10
        
5. 启动seata-server

         Linux下：
            # 初始化seata 的nacos配置 （在1.0版本之后好像没有了nacos-config.sh文件）
            sh nacos-config.sh 192.168.21.89(nacos的地址)
            
            # 启动seata-server
            sh seata-server.sh -p 8091

         windos下：
            # 初始化seata 的nacos配置
            sh nacos-config.sh 192.168.21.89(nacos的地址)
            
            # 启动seata-server
            sh seata-server.bat -p 8091


#### 数据库初始化

1.  seata数据库：
     直接用seata.sql （包含seata相关工作表）
2.  shop-order数据库：
     直接用shop-order.sql  （包含工商银行表，订单表，undo_log回滚记录表）
3.  shop-product数据库：
     直接用shop-product.sql（包含招商银行表，商品库存表，undo_log回滚记录表）

#### 应用配置

见代码

几个重要的配置

每个应用的resource里需要配置一个registry.conf ，demo中与seata-server里的配置相同
bootstrap.yml 的各个配置项，注意spring.cloud.alibaba.seata.tx-service-group 是服务组名称，与nacos-config.txt 配置的service.vgroup_mapping.${your-service-gruop}具有对应关系


#### 测试地址

1.AT模式下：模拟用户下单、扣库存

localhost:8093/product/order/{pid}   （pid=商品id）
当创建订单，扣减商品库存全部采购则提交事务；如果某个操作环节失败则全部回滚事务



2.TCC模式下：模拟用户转账：工商银行账户转账到招商银行账户

localhost:8073/seata/tcc

![输入图片说明](https://images.gitee.com/uploads/images/2020/1006/120921_e93b5fa4_1340534.png "屏幕截图.png")
merchantsId：转入账号（招商账号）
businessId： 转出账号（工商账号）
amount：转账金额


                                 欢迎添加作者微信进行交流

![输入图片说明](https://images.gitee.com/uploads/images/2020/1006/121520_07ffcf90_1340534.png "屏幕截图.png")
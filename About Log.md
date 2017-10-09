# About Log #

----------
## 简介： ##
为了收集记录线上不同级别的日志，生成不同级别的日志文件。能知道服务器上某个时间发生的异常，更加方便高效的查出BUG、解决BUG。
## 日志工具 ##
- slf4j + log4j（使用中）
- slf4j + logback
## 日志级别 ##
- ERROR: 严重异常，业务无法进行，需要人员介入排查。如 DB无法连接，远程服务无法调用，业务层面的数据异常
- WARN: 需要引起注意的异常，业务可以忽略这个异常并继续，但是可能造成潜在危害
- INFO: 重要业务节点完成，可以供管理员、维护人员、高级用户参考
- DEBUG: 供开发参考的信息，比如某个大流程中的某个小步骤完成了，或者开发认为可能会对故障排查有帮助的
- TRACE: 供调试的详细信息
## log4j使用 ##
### pom.xml ###
 - slf4j-log4j12
 - slf4j-api
 - log4j         

三者作用：    
1.slf4j-api作为日志接入的接口；   
2.slf4j-log4j12:链接slf4j-api和log4j中间的适配器嘛；    
3.log4j:这个是具体的日志系统。通过slf4j-log4j12初始化Log4j，达到最终日志的输出。
### web.xml ###
    <context-param>
        <param-name>log4jConfigLocation</param-name>
        <param-value>WEB-INF/classes/log4j.properties</param-value>
    </context-param>

    <context-param>
        <param-name>log4jRefreshInterval</param-name>
        <param-value>60000</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
    </listener>
### Object.class ###
申明标准:   
`private static final Logger log = LoggerFactory.getLogger(Object.class);` 
### log4j.properties ###
不做详解介绍，请去代码里查找。
## 日志收集注意点 ##
### <span style='color:red'>内容具体： ###
    log.debug("Message processed");
	log.debug(message.getJMSMessageID());
	log.debug("Message with id '{}' processed", message.getJMSMessageID());
 * 代码中相邻不意味着日志里相邻
 * 要有具体信息  

### <span style='color:red'>不能有副作用 ###
    log.trace("Id=" + request.getUser().getId() + " accesses " + manager.getPage().getUrl().toString())

	try {
    log.trace("Id=" + request.getUser().getId() + " accesses " + manager.getPage().getUrl().toString())
	} catch(NullPointerException e) {}
* 发生空指针等异常。  
### <span style='color:red'>记录对象 ###
	log.debug("Returning user: {}", user);
* 重载`toString()`
### <span style='color:red'>记录异常 ###
	catch(NullPointerException e){
    log.error("Error reading configuration file", e);
	}
* 不需要单独记录异常，web等框架会做这个事
* 对异常需要做业务上的描述的，只需要像上面那样
### <span style='color:red'>内容易于程序解析 ###
    log.debug("Begin time: {} {}", new Date(ts), ts);
	// Begin time: 28 20:14:12 CEST 2010 1272478452437

	log.debug("Begin time: date({}) timestamp({})", new Date(ts), ts);
	// Begin time: date(28 20:14:12 CEST 2010) timestamp(1272478452437)
* log不仅需要给人看，也需要给程序解析
### <span style='color:red'>禁止System.out ###
* `System.out`的内容会打到文件`catalina.out`里，不利于服务器日志收集。
* 改掉用`System.out`本地调试，`log.error()` 完全可以替代它。
### <span style='color:red'>禁止使用特殊字符做标记 ###
`log.debug("**********This is my message**********");`
### <span style='color:red'>禁止记录敏感信息 ###
* 账号、密码、手机、信用卡等敏感信息

## ELK日志系统 ##
ELK搜索规范：source:*环境.项目名_error.log   
例子:`source:*dev.gttown-enterprise-web_error.log`

查询地址：http://log.great-tao.com   
用户名： kibanaadmin   
密码： kibanagt   

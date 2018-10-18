
1.pom添加依赖
 		<!--for mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!--for Mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Spring boot Cache-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <!--for ehcache-->
        <dependency>
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>
2.建立 Ehcache配置文件(创建Ehcache的配置文件 ehcache.xml并置于项目 classpath下：)
<?xml version="1.0" encoding="UTF-8"?>
<ehcache>
    <diskStore path="java.io.tmpdir"/>
    <!-- 设定缓存的默认数据过期策略 -->
    <defaultCache
            maxElementsInMemory="10000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            overflowToDisk="true"
            maxElementsOnDisk="10000000"
            diskPersistent="false"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU"/>
    <cache name="user"
           maxElementsInMemory="1000"
           eternal="false"
           timeToIdleSeconds="10"/>
</ehcache>

3.配置 application.properties

server.port=80

# Mysql 数据源配置
spring.datasource.url=jdbc:mysql://121.196.213.251:3306/demo?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=xxxxxx
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# mybatis 配置
mybatis.type-aliases-package=cn.codesheep.springbt_ehcache.entity
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.map-underscore-to-camel-case=true

# ehcache 配置
spring.cache.ehcache.config=classpath:ehcache.xml


4. @Cacheable(value = "user", key = "#userName")
    public List<User> getUsersByName( String userName ) {
        List<User> users = userMapper.getUsersByName( userName );
        System.out.println( "从数据库读取，而非读取缓存！" );
        return users;
    }


/**
 * 
 * 读写事务,自定义事务管理器
* @ClassName: package-info
* @author xueyh
* @date 2018年8月12日 下午10:15:40
* 
*/
package com.xyh.spring.transaction;

/**
事务：
@Transactional(readOnly = false, rollbackFor = Throwable.class, isolation = Isolation.REPEATABLE_READ)
@Transactional 注解由于原理决定了他只能作用于public方法中，
1. 使用public访求；2. 写在外部类中，可被调用； 3. 使用注入的方式进行该方法的执行
spring事务的讲解：
在配置文件中，默认情况下，<tx:annotation-driven>会自动使用名称为transactionManager的事务管理器。所以，如果定义的事务管理器名称为transactionManager，那么就可以直接使用<tx:annotation-driven/>。如下：
<!-- 配置事务管理器 -->
<bean id="transactionManager"
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
    p:dataSource-ref="dataSource">
</bean>
<!-- enables scanning for @Transactional annotations -->
<tx:annotation-driven/>
<tx:annotation-driven>一共有四个属性如下，
mode：指定Spring事务管理框架创建通知bean的方式。可用的值有proxy和aspectj。前者是默认值，表示通知对象是个JDK代理；后者表示Spring AOP会使用AspectJ创建代理
proxy-target-class：如果为true，Spring将创建子类来代理业务类；如果为false，则使用基于接口的代理。（如果使用子类代理，需要在类路径中添加CGLib.jar类库）
order：如果业务类除事务切面外，还需要织入其他的切面，通过该属性可以控制事务切面在目标连接点的织入顺序。
transaction-manager：指定到现有的PlatformTransaction Manager bean的引用，通知会使用该引用
@Transactional的属性
isolation 枚举org.springframework.transaction.annotation.Isolation的值 事务隔离级别
noRollbackFor Class<? extends Throwable>[] 一组异常类，遇到时不回滚。默认为{}
noRollbackForClassName Stirng[] 一组异常类名，遇到时不回滚，默认为{}
propagation 枚举org.springframework.transaction.annotation.Propagation的值 事务传播行为
readOnly boolean 事务读写性
rollbackFor Class<? extends Throwable>[] 一组异常类，遇到时回滚
rollbackForClassName Stirng[] 一组异常类名，遇到时回滚
timeout int 超时时间，以秒为单位
value String 可选的限定描述符，指定使用的事务管理器
@Transactional标注的位置
@Transactional注解可以标注在类和方法上，也可以标注在定义的接口和接口方法上。
如果我们在接口上标注@Transactional注解，会留下这样的隐患：因为注解不能被继承，所以业务接口中标注的@Transactional注解不会被业务实现类继承。所以可能会出现不启动事务的情况。所以，spring建议我们将@Transaction注解在实现类上。
在方法上的@Transactional注解会覆盖掉类上的@Transactional。
注意：
@Transactional 可以作用于接口、接口方法、类以及类方法上。当作用于类上时，该类的所有 public 方法将都具有该类型的事务属性，同时，我们也可以在方法级别使用该标注来覆盖类级别的定义。
虽然 @Transactional 注解可以作用于接口、接口方法、类以及类方法上，但是 Spring 建议不要在接口或者接口方法上使用该注解，因为这只有在使用基于接口的代理时它才会生效。另外， @Transactional 注解应该只被应用到 public 方法上，这是由 Spring AOP 的本质决定的。如果你在 protected、private 或者默认可见性的方法上使用 @Transactional 注解，这将被忽略，也不会抛出任何异常。
默认情况下，只有来自外部的方法调用才会被AOP代理捕获，也就是，类内部方法调用本类内部的其他方法并不会引起事务行为，即使被调用方法使用@Transactional注解进行修饰。
*/
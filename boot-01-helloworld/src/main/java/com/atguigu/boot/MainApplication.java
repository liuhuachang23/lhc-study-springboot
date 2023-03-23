package com.atguigu.boot;


import ch.qos.logback.core.db.DBHelper;
import com.atguigu.boot.bean.Pet;
import com.atguigu.boot.bean.User;
import com.atguigu.boot.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 主程序类;主配置类
 * @SpringBootApplication：这是一个SpringBoot应用
 */

//@SpringBootApplication //等同于以下三个注解
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.atguigu.boot")
public class MainApplication {


    public static void main(String[] args) {
        //1、返回我们IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        //2、查看容器里面的组件
//        String[] names = run.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }

        //3、从容器中获取组件
        String[] beanNamesForType1 = run.getBeanNamesForType(WebMvcProperties.class);
        System.out.println("======"+beanNamesForType1.length);
        Pet tom01 = run.getBean("tom", Pet.class);
        Pet tom02 = run.getBean("tom", Pet.class);
        System.out.println("使用@Bean给容器添加组件，默认也是单实例："+(tom01 == tom02));


        //4. com.lhc.boot.config.MyConfig$$EnhancerBySpringCGLIB$$975aa5b0@56da7487  表示MyConfig 是一个代理对象
        //   com.lhc.boot.config.MyConfig@6cff61fc 表示不是一个代理对象
        MyConfig bean = run.getBean(MyConfig.class); //MyConfig配置类本身也是组件,可以通过获取组件的方法获取
        System.out.println("输出bean查看 他是不是一个代理对象" + bean);

        //5. 验证一下在外面调用 配置类中的 @Bean标注的方法 即组件注册的方法，是直接从容器中获取，还是 普通的方法调用创建新对象
        // 这个是取决于 配置类中 proxyBeanMethods 为true还是false ：
        //1） 如果 @Configuration(proxyBeanMethods = true) 表示MyConfig 是一个代理对象，使用代理对象调用方法
        //      SpringBoot就会去检查容器中有没有 这个方法返回的组件 如果有就直接获取, 即保持组件单实例
        //2） 如果 @Configuration(proxyBeanMethods = false) 表示MyConfig 不是一个代理对象 ,普通调用方法，创建新对象
        User user = bean.user01();
        User user1 = bean.user01();
        System.out.println("代理对象调用方法" + (user == user1));

        //6. 测试 @Configuration(proxyBeanMethods = true/false)
        //分别获取 user01 组件 和 tom组件
        User user01 = run.getBean("user01", User.class);
        Pet tom = run.getBean("tom", Pet.class);
        //判断一下 用户的宠物，是否就是 tom组件
        System.out.println("用户的宠物是否为 调用方法的宠物" + (user01.getPet() == tom));

        //7 从容器中获取组件 （添加了 @Import({User.class, DBHelper.class}) ）
        //1) 获取所有 User类型的所有组件
        String[] beanNamesForType = run.getBeanNamesForType(User.class);
        for (String s : beanNamesForType){
            System.out.println(s);
        }
        //2) 获取 DBHelper类型的组件
        DBHelper bean1 = run.getBean(DBHelper.class);
        System.out.println(bean1);
        // 3）输出结果：
        // com.lhc.boot.bean.User                          这个是@Import() 导入的
        // user01                                          这个是@Bean方法 导入的
        // ch.qos.logback.core.db.DBHelper@bf71cec         这个是@Import() 导入的
        //
        // 所以我们 @Import() 导入的组件名字 默认为全类名

        //为了接下来的操作顺利 我们先注解上面一部分内容

//        boolean user01 = run.containsBean("user01"); // 容器中是否包括这个组件
//        System.out.println("容器中user01组件是否存在：" + user01);
//
//        boolean tom = run.containsBean("tom"); // 容器中是否包括这个组件
//        System.out.println("容器中tom 组件是否存在：" + tom);

        boolean haha = run.containsBean("haha");
        boolean hehe = run.containsBean("hehe");
        System.out.println("haha："+haha);
        System.out.println("hehe："+hehe);

    }
}

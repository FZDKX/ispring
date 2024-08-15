import com.fzdkx.spring.context.support.ClassPathXmlApplicationContext;
import com.fzdkx.spring.test.CustomEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
//         t1();
        t2();
    }

    // 生命周期
    public static void t1(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath:aop.xml");
        app.registerShutdownHook();
        log.debug("==============");
        log.debug(String.valueOf(app.getBean("myBean")));
    }

    // 事件发布
    public static void t2(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath:aop.xml");
        // 发布自定义事件
        app.publishEvent(new CustomEvent(app,20021116L,"剑来"));
        app.registerShutdownHook();
        log.debug("==============");
        log.debug(String.valueOf(app.getBean("myBean")));
        log.debug("==============");
        log.debug(String.valueOf(app.getBean("myBean")));
    }
}

import com.fzdkx.spring.context.support.ClassPathXmlApplicationContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring.xml");
        app.registerShutdownHook();
        log.debug("==============");
        log.debug(String.valueOf(app.getBean("myBean")));
    }
}

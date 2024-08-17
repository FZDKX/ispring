import com.fzdkx.spring.context.support.AnnotationConfigApplicationContext;
import com.fzdkx.spring.test.MyConfiguration;
import com.fzdkx.spring.test.event.CustomEvent;
import com.fzdkx.spring.test.service.UserService;
import org.junit.Test;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class EndTest {
    @Test
    public void t1() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(MyConfiguration.class);
        for (String name : app.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        app.publishEvent(new CustomEvent(app,20021116L,"剑来"));
        UserService userService = app.getBean("userService", UserService.class);
        System.out.println(userService);
    }
}

import com.fzdkx.mytest.MyConfiguration;
import com.fzdkx.mytest.event.CustomEvent;
import com.fzdkx.mytest.service.UserService;
import com.fzdkx.mytest.loop.A;
import com.fzdkx.mytest.loop.B;
import com.fzdkx.spring.context.support.AnnotationConfigApplicationContext;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * @author 发着呆看星
 * @create 2024/8/17
 */
public class Test1 {
    @Test
    public void t1() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(MyConfiguration.class);
        // 获取所有Bean的名称
        for (String name : app.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        // 发布事件
        app.publishEvent(new CustomEvent(app, 20021116L, "剑来"));
        // 获取 userService 代理类
        UserService userService = app.getBean("userService", UserService.class);
        // 调用方法
        userService.queryUserInfo();
        // 获取 B
        B b = app.getBean("b", B.class);
        A a = app.getBean("a", A.class);
        System.out.println(b);
        app.close();
    }

    @Test
    public void t2() {
        Class<B> clazz = B.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            Class<?>[] types = constructor.getParameterTypes();
            int n = parameters.length;
            for (int i = 0; i < n; i++) {
                if (types[i] == A.class) {
                    System.out.println(true);
                }
                System.out.println(types[i] + "   " + parameters[i].getName());
            }
        }
    }
}

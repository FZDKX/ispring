import com.fzdkx.spring.aop.annotation.Aspect;
import com.fzdkx.spring.aop.aspectj.AspectJExpressionPointcut;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.context.support.ClassPathXmlApplicationContext;
import com.fzdkx.spring.test.factyory_bean.MyFactoryBean;
import com.fzdkx.spring.test.service.UserService;
import com.fzdkx.spring.test.xhyl.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
        Class<MyFactoryBean> clazz = MyFactoryBean.class;
        if (FactoryBean.class.isAssignableFrom(clazz)) {
            System.out.println("1 --- yes");
        }

        if (Person.class.isAssignableFrom(clazz)) {
            System.out.println("2 --- yes");
        }
    }


    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.fzdkx.spring.test.service.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method));

        // true、true
    }

    @Test
    public void t2() {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath:aop.xml");
        UserService userService = app.getBean("userService", UserService.class);
        log.debug(userService.queryUserInfo());
        log.debug("===========================");
        userService.register("zs");
        log.debug("===========================");
        for (String name : app.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("==================");
        System.out.println(userService);
    }

    @Test
    public void t3(){
        Date date = new Date("2021-01-23");
        System.out.println(date);
    }

}

import com.fzdkx.spring.aop.AdvisedSupport;
import com.fzdkx.spring.aop.TargetSource;
import com.fzdkx.spring.aop.annotation.Aspect;
import com.fzdkx.spring.aop.aspectj.AspectJExpressionPointcut;
import com.fzdkx.spring.aop.framework.CglibAopProxy;
import com.fzdkx.spring.aop.framework.JdkDynamicAopProxy;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.context.support.ClassPathXmlApplicationContext;
import com.fzdkx.spring.core.Order;
import com.fzdkx.spring.test.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.annotation.Target;
import java.lang.reflect.Method;

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
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.fzdkx.spring.test.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method));

        // true、true
    }

    @Test
    public void t2() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:aop.xml");
        UserService userService = applicationContext.getBean("userService", UserService.class);
        log.debug(userService.queryUserInfo());
        log.debug("===========================");
        userService.register("zs");
    }

    @Test
    public void t3() {
        Class<Person> personClass = Person.class;
        System.out.println(personClass.getAnnotation(Aspect.class));
    }
}

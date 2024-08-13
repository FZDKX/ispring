import com.fzdkx.spring.aop.AdvisedSupport;
import com.fzdkx.spring.aop.TargetSource;
import com.fzdkx.spring.aop.aspectj.AspectJExpressionPointcut;
import com.fzdkx.spring.aop.framework.CglibAopProxy;
import com.fzdkx.spring.aop.framework.JdkDynamicAopProxy;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.test.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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
        System.out.println(pointcut.matches(method, clazz));

        // true、true
    }

    @Test
    public void test_aop1() {
        // 目标对象
        UserService userService = new UserServiceImpl();

        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.fzdkx.spring.test.UserService.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        UserService proxy_jdk = (UserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        // 测试调用
        log.debug("======== jdk =========");
        proxy_jdk.queryUserInfo();
        // 代理对象(CglibAopProxy)
        UserService proxy_cglib = (UserService) new CglibAopProxy(advisedSupport).getProxy();
        // 测试调用
        log.debug("======== cglib =========");
        proxy_cglib.register("陈平安");
    }
}

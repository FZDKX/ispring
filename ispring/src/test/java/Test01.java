import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.test.MyFactoryBean;
import com.fzdkx.spring.test.Person;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 发着呆看星
 * @create 2024/8/10
 */
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
}

package com.fzdkx.spring.aop.aspectj;


import com.fzdkx.spring.aop.ClassFilter;
import com.fzdkx.spring.aop.MethodMatcher;
import com.fzdkx.spring.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


/**
 * @author 发着呆看星
 * @create 2024/8/13
 * 具体的切点表达式类，调用的是 AspectJ 中的方法
 */
public class AspectJExpressionPointcut implements ClassFilter, MethodMatcher, Pointcut {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    // 切人点表达式
    private final PointcutExpression pointcutExpression;

    // 传入知道的切点表达式字符串
    public AspectJExpressionPointcut(String expression) {
        // PointCut解析器 调用方法：获取支持指定原语的切入点解析器，并使用指定的类加载器进行解析
        // 得到切点表达式对象
        PointcutParser pointcutParser
                = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution
                                                                (SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        // 给切人点表达式赋值
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        // 判断一个类 是否符合 当前切点表达式
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 当前method 是否 匹配切点表达式
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}

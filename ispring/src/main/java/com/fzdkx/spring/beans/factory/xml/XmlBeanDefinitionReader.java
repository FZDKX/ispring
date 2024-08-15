package com.fzdkx.spring.beans.factory.xml;

import com.fzdkx.spring.beans.exception.BeanDefinitionStoreException;
import com.fzdkx.spring.beans.exception.BeansException;
import com.fzdkx.spring.beans.factory.FactoryBean;
import com.fzdkx.spring.beans.factory.config.*;
import com.fzdkx.spring.beans.factory.support.AbstractBeanDefinitionReader;
import com.fzdkx.spring.beans.factory.support.BeanDefinitionRegistry;
import com.fzdkx.spring.context.annotation.ClassPathBeanDefinitionScanner;
import com.fzdkx.spring.core.io.Resource;
import com.fzdkx.spring.core.io.ResourceLoader;
import com.fzdkx.spring.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
@Slf4j
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }


    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            // 加载资源
            InputStream in = resource.getInputStream();
            // 真正解析文件
            doLoadBeanDefinition(in);
        } catch (IOException e) {
            throw new RuntimeException("XML文档解析失败");
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    private void doLoadBeanDefinition(InputStream in) {
        // 1. 根据XML文件创建 DOM4J树
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(in);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        // 2. 获取DOM4J树的根节点 beans
        Element root = document.getRootElement();

        // 3. 解析包扫描
        componentScanHandle(root);
        // 4. 获取根节点的子节点 bean
        List<Element> beanList = root.elements("bean");

        // 5. 对bean的属性和子元素进行解析
        for (Element bean : beanList) {
            // 对Bean进行操作
            // 1：对属性进行处理
            BeanDefinition beanDefinition = attributeHandle(bean);
            // 2：对property进行处理
            List<Element> propertyList = bean.elements("property");
            if (propertyList != null && !propertyList.isEmpty()) {
                propertyHandle(propertyList, beanDefinition);
            }
            // 3：对constructor-arg进行处理
            List<Element> constructors = bean.elements("constructor-arg");
            if (constructors != null && !constructors.isEmpty()) {
                constructorHandle(constructors, beanDefinition, root);
            }
            getRegistry().registerBeanDefinition(beanDefinition.getName(), beanDefinition);
        }
    }

    private void componentScanHandle(Element root) {
        List<Element> elements = root.elements("component-scan");
        for (Element element : elements) {
            // 进行包扫描解析
            Attribute attribute = element.attribute("base-package");
            if (attribute == null) {
                // 出错了
                throw new RuntimeException("配置文件解析错误");
            }
            String basePackage = attribute.getValue();
            scanPackage(basePackage);
        }
    }

    private void scanPackage(String basePackages) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        String[] packages = basePackages.split(",");
        scanner.doScan(packages);
    }

    private void constructorHandle(List<Element> constructors, BeanDefinition bd, Element root) {
        ConstructorArgument argument = new ConstructorArgument();
        Map<Integer, ConstructorArgument.ValueHolder> map = argument.getIndexedArgumentValues();
        for (Element element : constructors) {
            Attribute id = element.attribute("index");
            ConstructorArgument.ValueHolder holder = new ConstructorArgument.ValueHolder();
            Attribute value = element.attribute("ref");
            // 是引用
            if (value != null) {
                List<Element> elements = root.elements("bean");
                for (Element e : elements) {
                    String name = value.getValue();
                    if (e.attribute("id").getValue().equals(name)) {
                        // 获取类型
                        try {
                            holder.setType(Class.forName(e.attribute("class").getValue()));
                            holder.setValue((BeanReference) () -> name);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            // 不是引用
            else {
                value = element.attribute("value");
                holder.setValue(value.getValue());
            }
            map.put(Integer.parseInt(id.getValue()), holder);
        }
        bd.setConstructorArgument(argument);
    }

    private static void propertyHandle(List<Element> propertyList, BeanDefinition bd) {
        PropertyValues propertyValues = new PropertyValues();
        for (Element element : propertyList) {
            PropertyValue property = new PropertyValue();
            // name
            Attribute name = element.attribute("name");
            if (name != null) {
                property.setPropertyName(name.getValue());
            } else {
                throw new RuntimeException("XML parse error");
            }
            // ref
            boolean flag = false;
            Attribute ref = element.attribute("ref");
            if (ref != null) {
                property.setValue((BeanReference) ref::getValue);
                flag = true;
            }
            // value
            Attribute value = element.attribute("value");
            if (value != null) {
                if (!flag) {
                    property.setValue(value.getValue());
                } else {
                    throw new RuntimeException("XML parse error");
                }
            }
            propertyValues.addProperty(property);
        }
        bd.setPropertyValues(propertyValues);

    }

    private static BeanDefinition attributeHandle(Element bean) {
        // 处理属性
        BeanDefinition beanDefinition = new BeanDefinition();

        // class
        Attribute className = bean.attribute("class");
        Class<?> clazz;
        if (className != null) {
            try {
                clazz = Class.forName(className.getValue());
                beanDefinition.setBeanClass(clazz);
                // 判断是否是FactoryBean
                if (FactoryBean.class.isAssignableFrom(clazz)) {
                    beanDefinition.setIsFactoryBean(true);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("bean定义错误");
        }

        // id
        Attribute id = bean.attribute("id");
        if (id != null) {
            beanDefinition.setName(id.getValue());
        } else {
            // id == null ，赋默认值，获取SimpleName，首字母变小写
            beanDefinition.setName(StringUtils.lowerFirst(clazz.getSimpleName()));
        }

        // scope
        Attribute scope = bean.attribute("scope");
        if (scope != null) {
            String value = scope.getValue();
            if (ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(value)) {
                beanDefinition.setScope(value);
            } else if (!ConfigurableBeanFactory.SCOPE_SINGLETON.equals(value)) {
                throw new RuntimeException("XML parse error");
            }
        }
        // isLazyInit
        Attribute lazyInit = bean.attribute("lazy-init");
        if (lazyInit != null) {
            if ("true".equals(lazyInit.getValue())) {
                beanDefinition.setLazyInit(true);
            } else if (!"false".equals(lazyInit.getValue())) {
                throw new RuntimeException("XML parse error");
            }
        }
        // initMethodName
        Attribute initMethodName = bean.attribute("init-method");
        if (initMethodName != null) {
            beanDefinition.setInitMethodName(initMethodName.getValue());
        }
        // destroyMethodName
        Attribute destroyMethodName = bean.attribute("destroy-method");
        if (destroyMethodName != null) {
            beanDefinition.setDestroyMethodName(destroyMethodName.getValue());
        }
        return beanDefinition;
    }
}

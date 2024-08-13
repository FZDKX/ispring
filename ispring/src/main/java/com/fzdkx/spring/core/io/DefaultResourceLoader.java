package com.fzdkx.spring.core.io;

import com.fzdkx.spring.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 默认的资源加载器
 */
public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        if (StringUtils.isEmpty(location)) {
            throw new RuntimeException("找不到文件");
        }
        // 如果是以 classpath: 开头
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // 截取 classpath: 后面的路径，查找文件
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

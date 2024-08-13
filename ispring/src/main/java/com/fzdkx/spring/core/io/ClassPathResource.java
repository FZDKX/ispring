package com.fzdkx.spring.core.io;

import com.fzdkx.spring.util.ClassUtils;
import com.fzdkx.spring.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 类路径下的资源
 */
@Slf4j
public class ClassPathResource implements Resource {
    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        if (StringUtils.isEmpty(path)) {
            log.debug("文件路径不能为空");
        }
        this.path = path;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in = classLoader.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
        }
        return in;
    }
}

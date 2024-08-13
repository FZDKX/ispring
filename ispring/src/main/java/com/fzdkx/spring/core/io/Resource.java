package com.fzdkx.spring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 * 资源加载接口定义
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}

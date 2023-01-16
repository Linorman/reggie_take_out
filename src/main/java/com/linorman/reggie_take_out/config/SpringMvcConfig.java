package com.linorman.reggie_take_out.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linorman.reggie_take_out.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 扩展消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1.创建Json消息转换器
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        // 2.设置对象转换器
        jsonConverter.setObjectMapper(new JacksonObjectMapper());
        // 3.将Json消息转换器添加到转换器列表中
        converters.add(0, jsonConverter);
    }

}

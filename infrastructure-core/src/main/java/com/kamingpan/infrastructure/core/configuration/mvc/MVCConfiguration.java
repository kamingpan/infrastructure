package com.kamingpan.infrastructure.core.configuration.mvc;

import com.kamingpan.infrastructure.core.interceptor.RequestInterceptor;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 拦截器配置
 *
 * @author kamingpan
 * @since 2018-06-21
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SystemProperties.class})
public class MVCConfiguration implements WebMvcConfigurer {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private RequestInterceptor requestInterceptor;

    /**
     * 添加统一拦截器
     *
     * @param registry 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册统一拦截器
        registry.addInterceptor(this.requestInterceptor).addPathPatterns("/**");

        // 还可以在这里注册其它的拦截器
        registry.addInterceptor(new LocaleChangeInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(this.responseBodyConverter());
        // 这里必须加上加载默认转换器，不然bug玩死人，并且该bug目前在网络上似乎没有解决方案
        // 百度，谷歌，各大论坛等。你可以试试去掉。
        // addDefaultHttpMessageConverters(converters);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();

        /*MediaType mediaType = new MediaType("application/json;charset=UTF-8");
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(mediaType);
        httpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);*/

        httpMessageConverter.setPrettyPrint(true);
        return httpMessageConverter;
    }

    /**
     * 添加允许跨域
     *
     * @param registry 拦截器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (!this.systemProperties.isCrossDomainAllowed()) {
            return;
        }

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                // 设置是否允许跨域传cookie
                .allowCredentials(true)
                // 设置缓存时间，减少重复响应
                .maxAge(1800L);
    }

}

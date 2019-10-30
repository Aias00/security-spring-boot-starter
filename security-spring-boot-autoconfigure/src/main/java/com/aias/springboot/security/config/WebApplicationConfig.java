package com.aias.springboot.security.config;

import com.aias.springboot.security.jwt.JwtAuthInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置信息
 *
 * @author zhangfx
 */
@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

	@Autowired
	private JwtAuthInterceptor jwtAuthInterceptor;

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {

		FastJsonHttpMessageConverter4 oFastConverter = new FastJsonHttpMessageConverter4();

		FastJsonConfig oFastJsonConfig = new FastJsonConfig();
		oFastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		oFastConverter.setFastJsonConfig(oFastJsonConfig);
		// 处理中文乱码问题
		List<MediaType> oFastMediaTypeList = new ArrayList<MediaType>();
		oFastMediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
		oFastConverter.setSupportedMediaTypes(oFastMediaTypeList);

		converters.add(oFastConverter);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtAuthInterceptor).addPathPatterns("/**")
				.excludePathPatterns("/swagger*/**")
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png",
						"/**/*.jpg", "/**/*.jpeg", "/*.html", "/**/*.html",
						"/swagger-resources/**")
				.excludePathPatterns("/*.jpg", "/*.ico", "/resources/**",
						"/**/login", "/login", "/login/*", "/**/shortMessage");
	}

}

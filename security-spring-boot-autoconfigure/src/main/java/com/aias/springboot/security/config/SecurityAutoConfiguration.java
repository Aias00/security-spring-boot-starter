package com.aias.springboot.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.aias.springboot.security.jwt.JwtAuthInterceptor;
import com.aias.springboot.security.jwt.JwtProperties;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> SecurityAutoConfiguration <br>
 * <b>@Date:</b> 2019/10/30 <br>
 *
 * @author <a> liuhy </a><br>
 */
@Configuration
@EnableConfigurationProperties({ JwtProperties.class,
		SecurityProperties.class })
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter
		implements WebMvcConfigurer {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private JwtAuthInterceptor jwtAuthInterceptor;
    @Autowired
    private JwtProperties jwtProperties;
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeRequests()
				// .antMatchers("/resources/**", "/**/login*").permitAll()
				.antMatchers(securityProperties.getHttp().getAllowPages())
				.permitAll().and().formLogin()
				// .loginPage("/login")
				.loginPage(securityProperties.getHttp().getLoginPage())
				.permitAll();
		// 禁用缓存
		httpSecurity.headers().cacheControl();
		httpSecurity.headers().contentTypeOptions().disable();

		httpSecurity.csrf().ignoringAntMatchers("/**");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				// .antMatchers("/swagger-ui.html").antMatchers("/swagger**").antMatchers("/swagger**/**")
				// .antMatchers("/webjars/**").antMatchers("/v2/api-docs")
				// .antMatchers("/**/login", "/login", "/login.*", "/login/*");
				.antMatchers(securityProperties.getWeb().getIgnorePages());
	}

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
				// .excludePathPatterns("/swagger*/**")
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png",
						"/**/*.jpg", "/**/*.jpeg", "/*.html", "/**/*.html",
						"/*.jpg", "/*.ico", "/resources/**")
				.excludePathPatterns(jwtProperties.getExcludePathPatterns());
	}
}

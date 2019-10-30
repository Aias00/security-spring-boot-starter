package com.aias.springboot.security.config;

import com.aias.springboot.security.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <b>
 *
 * <br>
 * <b>@ClassName:</b> SecurityAutoConfiguration <br>
 * <b>@Date:</b> 2019/10/30  <br>
 *
 * @author <a> liuhy </a><br>
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers("/resources/**", "/**/login*").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        httpSecurity.headers().contentTypeOptions().disable();

        httpSecurity.csrf().ignoringAntMatchers("/**");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // swagger
                .antMatchers("/swagger-ui.html").antMatchers("/swagger**").antMatchers("/swagger**/**")
                .antMatchers("/webjars/**").antMatchers("/v2/api-docs")
                .antMatchers("/**/login", "/login", "/login.*", "/login/*");
    }


}

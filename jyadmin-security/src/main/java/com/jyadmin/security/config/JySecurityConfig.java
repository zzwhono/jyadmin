package com.jyadmin.security.config;

import com.jyadmin.security.filter.TokenAuthenticationFilter;
import com.jyadmin.security.handler.DefaultAccessDeniedHandler;
import com.jyadmin.security.handler.DefaultUnAuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author LGX_TvT <br>
 * @version 1.0 <br>
 * Create by 2022-04-10 23:20 <br>
 * @description: JySecurityConfig <br>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JySecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private DefaultUnAuthHandler defaultUnAuthHandler;

    @Resource
    private DefaultAccessDeniedHandler defaultAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用表单登录，前后端分离用不上
        http.formLogin().disable();

        // 关闭csrf
        http.csrf().disable();

        // 禁用session，JWT校验不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 设置登录请求地址，token刷新地址，并设置不拦截
        http.authorizeRequests()
            .antMatchers(JyIgnoreUrlConfig.getIgnoreUrls())
            .permitAll();

        // 跨域请求会先进行一次options请求
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll();

        // 其他所有请求都需要校验
        http.authorizeRequests().anyRequest().authenticated();

        // 禁用缓存
        http.headers().cacheControl();

        // 处理异常情况
        http.exceptionHandling()
            // 认证未通过，不允许访问异常处理器
            .authenticationEntryPoint(defaultUnAuthHandler)
            // 认证通过，但是没权限处理器
            .accessDeniedHandler(defaultAccessDeniedHandler);

        //将Token校验过滤器配置到过滤器链中，否则不生效，放到UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(tokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Token校验过滤器
     */
    @Bean
    public TokenAuthenticationFilter tokenFilterBean()  {
        return new TokenAuthenticationFilter();
    }

    /**
     * 解决Security访问Swagger2被拦截的问题；
     * */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // allow Swagger URL to be accessed without authentication
        web.ignoring().antMatchers(
                "/static/**",
                "/doc.html",
                "/swagger-ui.html",
                "/v2/**",
                "/v2/api-docs",                         // swagger api json
                "/swagger-resources/configuration/ui",  // 用来获取支持的动作
                "/swagger-resources",                   // 用来获取api-docs的URI
                "/swagger-resources/configuration/security", // 安全选项
                "/swagger-resources/**",
                "/webjars/**",
                "/druid/**"
        );
    }

}

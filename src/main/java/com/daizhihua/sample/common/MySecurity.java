package com.daizhihua.sample.common;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义权限拦截
 */
@Configuration
@EnableWebSecurity
public class MySecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
                .authorizeRequests()
                .antMatchers("/static/css/**","/login").permitAll()
                .antMatchers("/*").authenticated()
                .and()
                .csrf().disable() //关闭CSRF
                .formLogin().loginPage("/login")
                //登录请求拦截的url,也就是form表单提交时指定的action
                .loginProcessingUrl("/form")
                .defaultSuccessUrl("/data") //成功登陆后跳转页面
                .failureUrl("/login?error=true")
                .and()
                .httpBasic();
    }


    //定义用户授权bena
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义用户bena
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        //可以设置内存指定的登录的账号密码,指定角色
        //不加.passwordEncoder(new MyPasswordEncoder())
        //就不是以明文的方式进行匹配，会报错
        auth.inMemoryAuthentication()
                .passwordEncoder(new MyPasswordEncoder()).withUser("admin").password("123456").roles("ADMIN");
        //.passwordEncoder(new MyPasswordEncoder())。
        //这样，页面提交时候，密码以明文的方式进行匹配。
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder()).withUser("user").password("user").roles("USER");


    }




}

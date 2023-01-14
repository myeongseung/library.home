package com.korit.library.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        //security login을 하지 않아도
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  //
        http.httpBasic().disable(); //
        http.authorizeRequests()
                .antMatchers("/mypage/**", "/security/**")
                .authenticated() //요청 주소로 mypage에 뭐가 들어오든 인증이 필요하다. 로그인 페이지로 보내기
                //이후 로그인 성공시 기존에 들어가려했던 /mapage/**로 페이지가 넘어감
                .antMatchers("/admin/**")
                .hasRole("ADMIN") //ROLE_ADMIN, MANAGER ==> Security의 문법
                .anyRequest()
                .permitAll()
                .and()
                .formLogin() //form으로 로그인 할것이다.
                .loginPage("/account/login") //로그인 페이지 get요청
                .loginProcessingUrl("/account/login") //login 인증 post요청
                .failureForwardUrl("/account/login/error") //로그인 실패시 넘어가는 페이지
                //.successForwardUrl("/mypage") //이전 요청 전부 무시하고 로그인 성공시 넘어가는 페이지
                .defaultSuccessUrl("/index"); //로그인 성공시 넘어가는 페이지
    }
}

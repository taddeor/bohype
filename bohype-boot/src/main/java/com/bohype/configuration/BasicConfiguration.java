package com.bohype.configuration;

import com.bohype.service.BoUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class BasicConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/h2-console/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin().defaultSuccessUrl("/index")
                .and()
                .httpBasic()
                .and().csrf().disable();

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authProvider(BoUserDetailService appUserDetailService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserDetailService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

}

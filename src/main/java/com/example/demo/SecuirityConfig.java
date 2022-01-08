package com.example.demo;

import com.example.demo.entite.AppUser;
import com.example.demo.filiteres.JWTAuthenticationFilter;
import com.example.demo.filiteres.JWTAuthorizationFilter;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecuirityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccountService accountService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth.userDetailsService((username)->{
            AppUser appUser=accountService.loadUserByUsername(username);
            Collection<GrantedAuthority> authorities= appUser.getAppRoles()
                    .stream().map((role)-> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());
            System.out.println(appUser.getPassword());
            return new User(appUser.getUsername(),appUser.getPassword(),authorities);
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.headers().frameOptions().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

package com.github.coding_team_sept.nd_backend.authentication.configuration;

import com.github.coding_team_sept.nd_backend.authentication.components.AuthenticationEntryPointJwt;
import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserDetailsService;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AppUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    @Autowired
    private JwtRequestFilter requestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security.cors().and().csrf().disable();

        security.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        security.exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and();

        security.authorizeRequests()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/api/v1/register").permitAll()
                .antMatchers("/api/v1/admin/**").hasAuthority(RoleType.ROLE_ADMIN.name())
                .antMatchers("/api/v1/validate").authenticated()
                .anyRequest().authenticated();

        security.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
package at.wero.spring.auth;

import at.wero.spring.auth.jwt.JwtAuthenticationFilter;
import at.wero.spring.auth.jwt.JwtAuthorizationFilter;
import at.wero.spring.auth.jwt.JwtService;
import at.wero.spring.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public WebSecurityConfig(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService,
                             UserRepository userRepository,
                             JwtService jwtService) {
        Assert.notNull(userDetailsService, "Argument userDetailService must not be null");
        Assert.notNull(userRepository, "Argument userRepository must not be null");
        Assert.notNull(jwtService, "Argument jwtService must not be null");
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtService))
                .authorizeRequests(authorizeRequest ->
                        authorizeRequest
                                .antMatchers("/login").permitAll()
                                .antMatchers("/register").permitAll()
                                .antMatchers("/api/admin").hasRole("ADMIN")
                                .antMatchers("/api/user").hasRole("USER")
                                .anyRequest().authenticated()
                );
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}

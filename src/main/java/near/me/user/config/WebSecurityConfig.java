package near.me.user.config;

import near.me.user.repository.UserRepository;
import near.me.user.security.AuthenticationFilter;
import near.me.user.security.AuthorizationFilter;
import near.me.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public WebSecurityConfig(Environment environment, BCryptPasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository) {
        this.environment = environment;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, environment.getProperty("login.url.path")).permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty("registration.url.path")).permitAll()
                .antMatchers(HttpMethod.GET, "/status").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), environment))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    private Filter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment, authenticationManager());
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
        return authenticationFilter;
    }
}

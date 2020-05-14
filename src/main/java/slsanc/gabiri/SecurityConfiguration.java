package slsanc.gabiri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT username, password, '1' as enabled FROM Users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT U.username, URT.role_name FROM " +
                        "(Users U INNER JOIN User_Roles UR on (U.user_id=UR.user_id) " +
                        "INNER JOIN User_Role_Types URT ON (UR.role_id = URT.role_id)) " +
                        "WHERE U.username=?").dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/styles").permitAll()
                .antMatchers("/registration/**").permitAll()
                .antMatchers("/**").hasAnyAuthority("SUPER_USER", "ADMIN", "USER")
                .anyRequest().authenticated()
                .and()

                .csrf().disable().formLogin()
                .loginPage("/registration/login")
                .loginProcessingUrl("/registration/login")
                .failureUrl("/registration/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()


                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home").and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }


}

package tinder2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tinder2.servicios.UsuarioServicio;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UsuarioServicio usuarioServicio;
    
    @Autowired
    public SecurityConfig(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    
    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {

        httpSec.httpBasic().disable();
/*      COMANDO CREADO PARA DESHABILITAR MENSAJE DE LOGIN AUTOMATICO DE SPRING AL TIPEAR LA URL EN EL EXPLORADOR
        https://stackoverflow.com/questions/23636368/how-to-disable-spring-security-login-screen        */

        httpSec.headers().frameOptions().sameOrigin()
                .and().authorizeRequests()
                            .antMatchers("/css/*", "/js/*", "/img/*")
                            .permitAll()
                .and().formLogin()
                            .loginPage("/login")
                            .loginProcessingUrl("/logincheck")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/inicio")
                            .permitAll()
                .and().logout()
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .permitAll();
    }

    
}


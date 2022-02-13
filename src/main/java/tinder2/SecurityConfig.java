package tinder2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.httpBasic().disable();
    }
}

/*
CLASE CREADA PARA DESHABILITAR MENSAJE DE LOGIN AUTOMATICO DE SPRING AL TIPEAR LA URL EN EL EXPLORADOR
https://stackoverflow.com/questions/23636368/how-to-disable-spring-security-login-screen
*/

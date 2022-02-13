package tinder2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tinder2.servicios.UsuarioServicio;

@SpringBootApplication
public class Tinder2Application {

    @Autowired
    private UsuarioServicio usuarioServicio;

    public static void main(String[] args) {
        SpringApplication.run(Tinder2Application.class, args);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }
}

/*
    1.  Start CMD and run as administrator
    2.  Type
                netstat -a -o -n
        and hit enter. Port can see Local Address column after : sign.
    3.  Select the process id(PID) that your port running and type
                taskkill /F /PID <PID_here>
        command and hit enter.
    https://stackoverflow.com/questions/56499928/web-server-failed-to-start-port-8080-was-already-in-use-spring-boot-microservi
 */

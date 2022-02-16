package tinder2.servicios;

import antlr.BaseAST;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinder2.entidades.Mascota;
import tinder2.entidades.Usuario;
import tinder2.errores.ErrorServicio;
import tinder2.repositorios.UsuarioRepositorio;
import tinder2.repositorios.ZonaRepositorio;

@Service

//public class UsuarioServicio {
public class UsuarioServicio implements UserDetailsService {

    private UsuarioRepositorio usuarioRepositorio;
    private FotoServicio fotoServicio;
    private NotificacionServicio notificacionServicio;
    private ZonaRepositorio zonaRepositorio;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, FotoServicio fotoServicio, NotificacionServicio notificacionServicio, ZonaRepositorio zonaRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.fotoServicio = fotoServicio;
        this.notificacionServicio = notificacionServicio;
        this.zonaRepositorio = zonaRepositorio;
    }

    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave, String clave2, String idZona, MultipartFile archivo) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2);
        Usuario usuario = new Usuario();
        usuario.setAlta(new Date());
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setFoto(fotoServicio.guardar(archivo));
        usuario.setZona(zonaRepositorio.getById(idZona));
        usuarioRepositorio.save(usuario);
//        notificacionServicio.enviar("Bienvenidos al Tinder de Mascotas!", "Tinder de Mascota", usuario.getMail());
    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String mail, String clave, String clave2, String idZona, MultipartFile archivo) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setClave(clave);
            usuario.setMail(mail);
            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }
            usuario.setFoto(fotoServicio.actualizar(idFoto, archivo));
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    @Transactional
    private void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    @Transactional
    private void habilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    private void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo.");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo.");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo.");
        }
        if (usuarioRepositorio.buscarPorMail(mail).size() == 1) {
            throw new ErrorServicio("El mail del usuario ya se encuentra registrado.");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave del usuario debe tener al menos 6 caracteres.");
        }
         if (!clave2.equals(clave)) {
                throw new ErrorServicio("Las contraseñas no coinciden");
            }
        
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail).get(0);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();

            permisos.add(new SimpleGrantedAuthority("MODULO_FOTO"));
            permisos.add(new SimpleGrantedAuthority("MODULO_MASCOTA"));
            permisos.add(new SimpleGrantedAuthority("MODULO_VOTO"));
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
    
       public Usuario validarIdUsuario(String id) throws ErrorServicio {
        Optional<Usuario> rta = usuarioRepositorio.findById(id);
        if (!rta.isPresent()) {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
        return rta.get();
    }
}

package tinder2.servicios;

import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinder2.entidades.Mascota;
import tinder2.entidades.Usuario;
import tinder2.enums.Sexo;
import tinder2.errores.ErrorServicio;
import tinder2.repositorios.MascotaRepositorio;
import tinder2.repositorios.UsuarioRepositorio;

@Service
public class MascotaServicio {

    private MascotaRepositorio mascotaRepositorio;
    private UsuarioRepositorio usuarioRepositorio;
    private FotoServicio fotoServicio;
    private UsuarioServicio usuarioServicio;

    @Autowired
    public MascotaServicio(MascotaRepositorio mascotaRepositorio, UsuarioRepositorio usuarioRepositorio, FotoServicio fotoServicio, UsuarioServicio usuarioServicio) {
        this.mascotaRepositorio = mascotaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.fotoServicio = fotoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @Transactional
    public void agregar(String idUsuario, String nombre, Sexo sexo, MultipartFile archivo) throws ErrorServicio {
        validar(nombre, sexo);
        usuarioServicio.validarIdUsuario(idUsuario);
        Mascota mascota = new Mascota();
        mascota.setAlta(new Date());
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setUsuario(usuarioRepositorio.getById(idUsuario));
        mascota.setFoto(fotoServicio.guardar(archivo));
        mascotaRepositorio.save(mascota);
    }

    @Transactional
    public void eliminar(String id, String idUsuario) throws ErrorServicio {
        Mascota mascota = validarIdMascota(id);
        validarDueño(mascota, idUsuario);

        mascota.setBaja(new Date());
        mascotaRepositorio.save(mascota);
    }

    @Transactional
    public void modificar(String id, String idUsuario, String nombre, Sexo sexo, MultipartFile archivo) throws ErrorServicio {
        Mascota mascota = validarIdMascota(id);
        validar(nombre, sexo);
        Usuario usuario = validarDueño(mascota, idUsuario);
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        String idFoto = null;
        if (mascota.getFoto() != null) {
            idFoto = mascota.getFoto().getId();
        }
        mascota.setFoto(fotoServicio.actualizar(idFoto, archivo));
        mascotaRepositorio.save(mascota);
    }

    private void validar(String nombre, Sexo sexo) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre de la mascota no puede ser nulo.");
        }
        if (sexo == null) {
            System.out.println("El sexo de la mascota no puede ser nulo.");
        }
    }

    public Usuario validarDueño(Mascota mascota, String idUsuario) throws ErrorServicio {
        if (!mascota.getUsuario().getId().equals(idUsuario)) {
            throw new ErrorServicio("El usuario no tiene permiso para editar la mascota solicitada.");
        }
        return usuarioRepositorio.getById(idUsuario);
    }

    public Mascota validarIdMascota(String id) throws ErrorServicio {
        Optional<Mascota> rta = mascotaRepositorio.findById(id);
        if (!rta.isPresent()) {
            throw new ErrorServicio("No se encontro la mascota solicitada.");
        }
        return rta.get();
    }
}

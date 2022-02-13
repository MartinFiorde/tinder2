package tinder2.servicios;

import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tinder2.entidades.Voto;
import tinder2.errores.ErrorServicio;
import tinder2.repositorios.MascotaRepositorio;
import tinder2.repositorios.VotoRepositorio;

@Service
public class VotoServicio {

    private VotoRepositorio votoRepositorio;
    private MascotaRepositorio mascotaRepositorio;
    private MascotaServicio mascotaServicio;
    private NotificacionServicio notificacionServicio;

    @Autowired
    public VotoServicio(VotoRepositorio votoRepositorio, MascotaRepositorio mascotaRepositorio, MascotaServicio mascotaServicio, NotificacionServicio notificacionServicio) {
        this.votoRepositorio = votoRepositorio;
        this.mascotaRepositorio = mascotaRepositorio;
        this.mascotaServicio = mascotaServicio;
        this.notificacionServicio = notificacionServicio;
    }

    @Transactional
    public void votar(String idUsuario, String idMasc1, String idMasc2) throws ErrorServicio {
        if (idMasc1.equals(idMasc2)) {
            throw new ErrorServicio("La mascota no puede votarse a si misma");
        }
        mascotaServicio.validarIdMascota(idMasc1);
        mascotaServicio.validarIdMascota(idMasc2);
        mascotaServicio.validarDueño(mascotaRepositorio.getById(idMasc1), idUsuario);
        Voto voto = new Voto();
        voto.setMascota1(mascotaRepositorio.getById(idMasc1));
        voto.setMascota2(mascotaRepositorio.getById(idMasc2));
        voto.setFecha(new Date());
        votoRepositorio.save(voto);
//        notificacionServicio.enviar("Tu mascota ha sido votada!", "Tinder de Mascota", voto.getMascota2().getUsuario().getMail());
    }

    @Transactional
    public void responder(String idUsuario, String id) throws ErrorServicio {
        Voto voto = validarIdVoto(id);
        if (!voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
            throw new ErrorServicio("El usuario no tiene permiso para responder el voto.");
        }
        voto.setRespuesta(new Date());
        votoRepositorio.save(voto);
//        notificacionServicio.enviar("Tu voto fue correspondido!", "Tinder de Mascota", voto.getMascota1().getUsuario().getMail());
    }

    public Voto validarIdVoto(String id) throws ErrorServicio {
        Optional<Voto> rta = votoRepositorio.findById(id);
        if (!rta.isPresent()) {
            throw new ErrorServicio("No se encontro el voto solicitado.");
        }
        return rta.get();
    }
}

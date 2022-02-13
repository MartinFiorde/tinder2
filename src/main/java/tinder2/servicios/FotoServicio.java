package tinder2.servicios;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinder2.entidades.Foto;
import tinder2.errores.ErrorServicio;
import tinder2.repositorios.MascotaRepositorio;
import tinder2.repositorios.FotoRepositorio;

@Service
public class FotoServicio {

    private FotoRepositorio fotoRepositorio;

    @Autowired
    public FotoServicio(FotoRepositorio fotoRepositorio) {
        this.fotoRepositorio = fotoRepositorio;
    }

    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Foto actualizar(String id, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = validarIdFoto(id);
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Foto validarIdFoto(String id) throws ErrorServicio {
        Optional<Foto> rta = fotoRepositorio.findById(id);
        if (!rta.isPresent()) {
            return null;
        }
        return rta.get();
    }

}

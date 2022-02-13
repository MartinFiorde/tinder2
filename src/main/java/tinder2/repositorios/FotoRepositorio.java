package tinder2.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tinder2.entidades.Foto;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}

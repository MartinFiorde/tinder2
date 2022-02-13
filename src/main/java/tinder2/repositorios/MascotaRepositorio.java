package tinder2.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tinder2.entidades.Mascota;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String> {

    @Query("SELECT a FROM Mascota a WHERE a.usuario.id = :idq")
    public List<Mascota> buscarVotosPropios(@Param("idq") String id);
}

package tinder2.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tinder2.entidades.Voto;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String> {

    @Query("SELECT a FROM Voto a WHERE a.mascota1.id = :idq ORDER BY a.fecha DESC")
    public List<Voto> buscarVotosPropios(@Param("idq") String id);

    @Query("SELECT a FROM Voto a WHERE a.mascota2.id = :idq ORDER BY a.fecha DESC")
    public List<Voto> buscarVotosRecibidos(@Param("idq") String id);

}

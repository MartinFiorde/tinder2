package tinder2.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tinder2.entidades.Zona;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String> {
    
}

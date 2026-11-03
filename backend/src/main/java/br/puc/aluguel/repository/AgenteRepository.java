package br.puc.aluguel.repository;

import br.puc.aluguel.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByUsuarioId(Long usuarioId);
}

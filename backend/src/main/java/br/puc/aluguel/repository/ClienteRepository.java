package br.puc.aluguel.repository;

import br.puc.aluguel.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuarioId(Long usuarioId);
    boolean existsByCpf(String cpf);
}

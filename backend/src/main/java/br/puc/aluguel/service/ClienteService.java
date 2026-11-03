package br.puc.aluguel.service;

import br.puc.aluguel.dto.AtualizarClienteRequest;
import br.puc.aluguel.dto.EmpregadorRequest;
import br.puc.aluguel.model.Cliente;
import br.puc.aluguel.model.Empregador;
import br.puc.aluguel.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public Cliente buscarPorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public Cliente atualizar(Long id, AtualizarClienteRequest request) {
        Cliente cliente = buscarPorId(id);

        if (request.empregadores() != null && request.empregadores().size() > 3) {
            throw new IllegalArgumentException("Cliente pode ter no máximo 3 empregadores");
        }

        cliente.setRg(request.rg());
        cliente.setEndereco(request.endereco());
        cliente.setProfissao(request.profissao());

        cliente.getEmpregadores().clear();
        List<Empregador> novos = new ArrayList<>();
        if (request.empregadores() != null) {
            for (EmpregadorRequest er : request.empregadores()) {
                Empregador empregador = new Empregador();
                empregador.setCliente(cliente);
                empregador.setNome(er.nome());
                empregador.setRendimento(er.rendimento());
                novos.add(empregador);
            }
        }
        cliente.getEmpregadores().addAll(novos);

        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }
}

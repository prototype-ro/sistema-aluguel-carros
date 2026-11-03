package br.puc.aluguel.service;

import br.puc.aluguel.dto.AutomovelRequest;
import br.puc.aluguel.model.Automovel;
import br.puc.aluguel.repository.AutomovelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;

    public AutomovelService(AutomovelRepository automovelRepository) {
        this.automovelRepository = automovelRepository;
    }

    public List<Automovel> listarTodos() {
        return automovelRepository.findAll();
    }

    public Automovel buscarPorId(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Automóvel não encontrado"));
    }

    public Automovel criar(AutomovelRequest request) {
        Automovel automovel = new Automovel();
        aplicar(automovel, request);
        return automovelRepository.save(automovel);
    }

    public Automovel atualizar(Long id, AutomovelRequest request) {
        Automovel automovel = buscarPorId(id);
        aplicar(automovel, request);
        return automovelRepository.save(automovel);
    }

    public void excluir(Long id) {
        automovelRepository.deleteById(id);
    }

    private void aplicar(Automovel automovel, AutomovelRequest request) {
        automovel.setMatricula(request.matricula());
        automovel.setAno(request.ano());
        automovel.setMarca(request.marca());
        automovel.setModelo(request.modelo());
        automovel.setPlaca(request.placa());
        automovel.setTipoProprietario(request.tipoProprietario());
        automovel.setProprietarioId(request.proprietarioId());
    }
}

package br.puc.aluguel.controller;

import br.puc.aluguel.dto.AutomovelRequest;
import br.puc.aluguel.dto.AutomovelResponse;
import br.puc.aluguel.model.Automovel;
import br.puc.aluguel.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/automoveis")
public class AutomovelController {

    private final AutomovelService automovelService;

    public AutomovelController(AutomovelService automovelService) {
        this.automovelService = automovelService;
    }

    @GetMapping
    public ResponseEntity<List<AutomovelResponse>> listar() {
        return ResponseEntity.ok(automovelService.listarTodos().stream().map(this::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<AutomovelResponse> criar(@Valid @RequestBody AutomovelRequest request) {
        return ResponseEntity.ok(toResponse(automovelService.criar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutomovelResponse> atualizar(
            @PathVariable Long id, @Valid @RequestBody AutomovelRequest request) {
        return ResponseEntity.ok(toResponse(automovelService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        automovelService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private AutomovelResponse toResponse(Automovel a) {
        return new AutomovelResponse(
                a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(),
                a.getPlaca(), a.getTipoProprietario(), a.getProprietarioId()
        );
    }
}

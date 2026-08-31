package sp.senai.org.controle_de_almoxarifado.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sp.senai.org.controle_de_almoxarifado.DTO.Request.MovimentacaoRequest;
import sp.senai.org.controle_de_almoxarifado.model.Movimentacao;
import sp.senai.org.controle_de_almoxarifado.service.MovimentacaoService;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoApiController {
    private final MovimentacaoService movimentacaoService;

    public MovimentacaoApiController(
            MovimentacaoService movimentacaoService
    ) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<Movimentacao> criar(
            @RequestBody MovimentacaoRequest request
    ) {

        Movimentacao movimentacao =
                movimentacaoService.criarMovimentacao(request);

        return ResponseEntity.ok(movimentacao);
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listar() {

        return ResponseEntity.ok(
                movimentacaoService.listarMovimentacoes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                movimentacaoService.buscarPorId(id)
        );
    }
}

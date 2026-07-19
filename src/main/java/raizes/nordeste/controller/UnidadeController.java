package raizes.nordeste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import raizes.nordeste.dto.UnidadeRequestDTO;
import raizes.nordeste.dto.UnidadeResponseDTO;
import raizes.nordeste.model.StatusUnidade;
import raizes.nordeste.service.UnidadeService;
import java.util.List;

@RestController
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Operações relacionadas ao gerenciamento das Unidades(Lojas)")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @Operation(
            summary = "Cadastrar Unidade",
            description = "Realiza o cadastro de uma nova Unidade."
        )
    @PostMapping
    public ResponseEntity<UnidadeResponseDTO> cadastrar( @RequestBody UnidadeRequestDTO request) {
        UnidadeResponseDTO response = unidadeService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar todas Unidades",
            description = "Solicita Lista das Unidades."
        )
    @GetMapping
    public ResponseEntity<List<UnidadeResponseDTO>> listar() {
        return ResponseEntity.ok(unidadeService.listarTodas());
    }

    @Operation(
            summary = "Alterar o o Status da Unidade",
            description = "Alterar o status (Aberta/Fechada) pelo Id da Unidade."
        )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> mudarStatus(
            @PathVariable Long id, 
            @RequestParam StatusUnidade novoStatus) {
        
        unidadeService.atualizarStatus(id, novoStatus);
        return ResponseEntity.noContent().build();
    }
}
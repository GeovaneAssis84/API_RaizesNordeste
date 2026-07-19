package raizes.nordeste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import raizes.nordeste.dto.UnidadeRequestDTO;
import raizes.nordeste.dto.UnidadeResponseDTO;
import raizes.nordeste.model.StatusUnidade;
import raizes.nordeste.service.UnidadeService;
import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    
    @PostMapping
    public ResponseEntity<UnidadeResponseDTO> cadastrar( @RequestBody UnidadeRequestDTO request) {
        UnidadeResponseDTO response = unidadeService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UnidadeResponseDTO>> listar() {
        return ResponseEntity.ok(unidadeService.listarTodas());
    }

    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> mudarStatus(
            @PathVariable Long id, 
            @RequestParam StatusUnidade novoStatus) {
        
        unidadeService.atualizarStatus(id, novoStatus);
        return ResponseEntity.noContent().build();
    }
}
package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.PagamentoRequestDTO;
import raizes.nordeste.dto.PagamentoResponseDTO;
import raizes.nordeste.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    //Gera a intenção de pagamento (POST /pagamentos)
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> iniciar(@Valid @RequestBody PagamentoRequestDTO request) {
        return ResponseEntity.ok(pagamentoService.iniciarPagamento(request));
    }

    //Simula o retorno do banco (PATCH /pagamentos/1/processar?aprovado=true)
    @PatchMapping("/{id}/processar")
    public ResponseEntity<PagamentoResponseDTO> processar(
            @PathVariable Long id, 
            @RequestParam boolean aprovado) {
        return ResponseEntity.ok(pagamentoService.confirmarPagamento(id, aprovado));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PagamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
    }
    
}

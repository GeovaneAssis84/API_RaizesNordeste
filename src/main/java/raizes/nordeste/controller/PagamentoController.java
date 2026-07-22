package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.PagamentoRequestDTO;
import raizes.nordeste.dto.PagamentoResponseDTO;
import raizes.nordeste.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Operações relacionadas ao gerenciamento dos pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Operation(
            summary = "Inicia um pagamento",
            description = "Inicia um pagamento de um pedido."
        )
    //Gera a intenção de pagamento (POST /pagamentos)
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> iniciar(@Valid @RequestBody PagamentoRequestDTO request) {
        return ResponseEntity.ok(pagamentoService.iniciarPagamento(request));
    }

    @Operation(
            summary = "Efetuar um pagamento",
            description = "Efetua o pagamento de um pedido através do Id"
        )
    //Simula o retorno do banco (PATCH /pagamentos/1/processar?aprovado=true)
    @PatchMapping("/{id}/processar")
    public ResponseEntity<PagamentoResponseDTO> processar(
            @PathVariable Long id, 
            @RequestParam boolean aprovado) {
        return ResponseEntity.ok(pagamentoService.confirmarPagamento(id, aprovado));
    }

    @Operation(
            summary = "Cancelar um pagamento",
            description = "Cancela o pagamento através do Id"
        )
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PagamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.cancelarPagamento(id));
    }
    
}

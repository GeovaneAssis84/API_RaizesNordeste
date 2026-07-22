package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.PedidoRequestDTO;
import raizes.nordeste.dto.PedidoResponseDTO;
import raizes.nordeste.model.StatusPedido;
import raizes.nordeste.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento dos pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um novo pedido."
        )
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO response = pedidoService.criarPedido(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
            summary = "Cancelar um pedido",
            description = "Cancela um pedido escolhido pelo Id"
        )
 // Cancelar o pedido
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @Operation(
            summary = "Consulta um pedido",
            description = "Consulta o status de um pedido através do Id"
        )
    // consultar status do pedido
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.consultarPedido(id));
    }
    
    @Operation(
            summary = "Alterar status de um pedido",
            description = "Altera o status de um pedido através do Id"
        )
    // Rota para avançar o status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> mudarStatus(
            @PathVariable Long id, 
            @RequestParam StatusPedido novoStatus) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }


    
}

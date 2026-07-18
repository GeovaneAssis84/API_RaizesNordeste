package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.PedidoRequestDTO;
import raizes.nordeste.dto.PedidoResponseDTO;
import raizes.nordeste.model.StatusPedido;
import raizes.nordeste.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO response = pedidoService.criarPedido(request);
        return ResponseEntity.ok(response);
    }
    
 // Cancelar o pedido
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    // Rota para avançar o status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> mudarStatus(
            @PathVariable Long id, 
            @RequestParam StatusPedido novoStatus) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }


    
}

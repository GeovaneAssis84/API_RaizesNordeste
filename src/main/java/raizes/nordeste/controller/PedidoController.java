package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.PedidoRequestDTO;
import raizes.nordeste.dto.PedidoResponseDTO;
import raizes.nordeste.exception.ErrorResponse;
import raizes.nordeste.model.StatusPedido;
import raizes.nordeste.service.PedidoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pedidos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento dos pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido cadastrado com sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos na requisição",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um novo pedido."
        )
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(
            @Valid @RequestBody PedidoRequestDTO request,
            Authentication authentication) {
        PedidoResponseDTO response = pedidoService.criarPedido(request, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
        @ApiResponse(
            responseCode = "404",
            description = "Peido não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @Operation(
            summary = "Cancelar um pedido",
            description = "Cancela um pedido escolhido pelo Id"
        )
 // Cancelar o pedido
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @Operation(
            summary = "Consulta um pedido",
            description = "Consulta o status de um pedido através do Id"
        )
    // consultar status do pedido
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> consultar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.consultarPedido(id));
    }
    
    
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Status alterado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
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

    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Pedidos da unidade listados com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Unidade não encontrada",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @Operation(
        summary = "Listar pedidos por unidade",
        description = "Lista todos os pedidos associados a uma unidade."
    )
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorUnidade(
            @PathVariable Long unidadeId) {

        return ResponseEntity.ok(
                pedidoService.listarPorUnidade(unidadeId)
        );
    }

    
}

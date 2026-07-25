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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import raizes.nordeste.exception.ErrorResponse;

@RestController
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Operações relacionadas ao gerenciamento das Unidades(Lojas)")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unidade cadastrada com sucesso"),
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
            summary = "Cadastrar Unidade",
            description = "Realiza o cadastro de uma nova Unidade."
        )
    @PostMapping
    public ResponseEntity<UnidadeResponseDTO> cadastrar(@Valid @RequestBody UnidadeRequestDTO request) {
        UnidadeResponseDTO response = unidadeService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unidades listadas com sucesso"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @Operation(
            summary = "Listar todas Unidades",
            description = "Solicita Lista das Unidades."
        )
    @GetMapping
    public ResponseEntity<List<UnidadeResponseDTO>> listar() {
        return ResponseEntity.ok(unidadeService.listarTodas());
    }

    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Status da unidade alterado com sucesso"),
        @ApiResponse(
            responseCode = "404",
            description = "Unidade não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Status informado é inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
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
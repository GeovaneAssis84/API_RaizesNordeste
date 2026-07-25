package raizes.nordeste.controller;


import jakarta.validation.Valid;
import raizes.nordeste.dto.EstoqueRequestDTO;
import raizes.nordeste.dto.EstoqueResponseDTO;
import raizes.nordeste.exception.ErrorResponse;
import raizes.nordeste.service.EstoqueService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/estoques")
@Tag(name = "Estoques", description = "Operações relacionadas ao gerenciamento dos Estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estoque cadastrado com sucesso"),
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
            summary = "Iniciar ou atualizar o estoque",
            description = "Iniciar ou atualizar o estoque de um Produto em uma Unidade"
        )
    // Rota para alimentar ou ajustar a quantidade de um produto em uma unidade
    @PutMapping
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@Valid @RequestBody EstoqueRequestDTO request) {
        EstoqueResponseDTO response = estoqueService.salvarOuAtualizar(request);
        return ResponseEntity.ok(response);
    }
    
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estoque encontrado com sucesso"),
        @ApiResponse(
            responseCode = "404",
            description = "Estoque não encontrado para esta Unidade",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @Operation(
            summary = "Consultar o estoque",
            description = "Consultar o estoque de um Produto em uma Unidade"
        )
    //Rota para consultar estoque da unidade e poder montar o cardapio
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<Map<String, Object>>> consultarEstoque(@PathVariable Long unidadeId) {
        List<Map<String, Object>> estoque = estoqueService.consultarEstoqueUnidade(unidadeId);
        return ResponseEntity.ok(estoque);
    }
    
}

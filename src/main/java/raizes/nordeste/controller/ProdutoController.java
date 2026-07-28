package raizes.nordeste.controller;
import raizes.nordeste.dto.*;
import raizes.nordeste.exception.ErrorResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import raizes.nordeste.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Produtos", description = "Operações relacionadas ao gerenciamento dos produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
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
            summary = "Cadastrar Produto",
            description = "Realiza o cadastro de um novo produto."
        )
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO request){
		ProdutoResponseDTO novoProduto = produtoService.cadastrar(request);
		return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
	}

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso"),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @Operation(
            summary = "Listar Produtos",
            description = "Solicita a lista de todos produtos."
        )
	@GetMapping
	public ResponseEntity<List<ProdutoResponseDTO>> listarProdutosCardapio() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodosAtivos();
        return ResponseEntity.ok(produtos);
    }
	
}

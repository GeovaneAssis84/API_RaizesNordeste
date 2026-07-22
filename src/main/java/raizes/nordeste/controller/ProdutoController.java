package raizes.nordeste.controller;
import raizes.nordeste.dto.*;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import raizes.nordeste.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas ao gerenciamento dos produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
    @Operation(
            summary = "Cadastrar Produto",
            description = "Realiza o cadastro de um novo produto."
        )
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO request){
		ProdutoResponseDTO novoProduto = produtoService.cadastrar(request);
		return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
	}

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

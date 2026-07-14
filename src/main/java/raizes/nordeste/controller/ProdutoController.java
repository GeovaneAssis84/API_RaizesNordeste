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

import jakarta.validation.Valid;
import raizes.nordeste.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO request){
		ProdutoResponseDTO novoProduto = produtoService.cadastrar(request);
		return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ProdutoResponseDTO>> listarProdutosCardapio() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodosAtivos();
        return ResponseEntity.ok(produtos);
    }
	
}

package raizes.nordeste.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import raizes.nordeste.dto.ProdutoRequestDTO;
import raizes.nordeste.dto.ProdutoResponseDTO;
import raizes.nordeste.model.Produto;
import raizes.nordeste.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
		Produto produto = new Produto();
		produto.setDescricao(dto.getDescricao());
		produto.setPreco(dto.getPreco());
		produto.setAtivo(dto.isAtivo());
		
		Produto produtoSalvo = produtoRepository.save(produto);
		return converteParaResponseDTO(produtoSalvo);
	}

	public List<ProdutoResponseDTO> listarTodosAtivos() {
        return produtoRepository.findByAtivoTrue().stream()
                .map(this::converteParaResponseDTO)
                .collect(Collectors.toList());
    }
	
	private ProdutoResponseDTO converteParaResponseDTO(Produto produto) {
		return new ProdutoResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.isAtivo()
        );
    }
	
}

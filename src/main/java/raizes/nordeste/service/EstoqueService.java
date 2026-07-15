package raizes.nordeste.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raizes.nordeste.dto.EstoqueRequestDTO;
import raizes.nordeste.dto.EstoqueResponseDTO;
import raizes.nordeste.model.Estoque;
import raizes.nordeste.model.Produto;
import raizes.nordeste.model.Unidade;
import raizes.nordeste.repository.EstoqueRepository;
import raizes.nordeste.repository.ProdutoRepository;
import raizes.nordeste.repository.UnidadeRepository;


@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private UnidadeRepository unidadeRepository; 
    @Autowired
    private ProdutoRepository produtoRepository; 

    @Transactional
    public EstoqueResponseDTO salvarOuAtualizar(EstoqueRequestDTO request) {
        // Verifica se Unidade e Produto existem
        Unidade unidade = unidadeRepository.findById(request.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada com o ID: " + request.getUnidadeId()));

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + request.getProdutoId()));

        // Se já existir esse produto na loja, apenas atualiza a quantidade
        Estoque estoque = estoqueRepository.findByUnidadeIdAndProdutoId(request.getUnidadeId(), request.getProdutoId())
                .orElse(new Estoque());

        estoque.setUnidade(unidade);
        estoque.setProduto(produto);
        estoque.setQuantidade(request.getQuantidade());

        return converterParaResponseDTO(estoqueRepository.save(estoque));
    }

    private EstoqueResponseDTO converterParaResponseDTO(Estoque estoque) {
        EstoqueResponseDTO dto = new EstoqueResponseDTO();
        dto.setId(estoque.getId());
        dto.setUnidadeId(estoque.getUnidade().getId());
        dto.setProdutoId(estoque.getProduto().getId());
        dto.setProdutoDescricao(estoque.getProduto().getDescricao());
        dto.setQuantidade(estoque.getQuantidade());
        return dto;
    }
        
}
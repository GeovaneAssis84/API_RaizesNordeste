package raizes.nordeste.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raizes.nordeste.dto.EstoqueRequestDTO;
import raizes.nordeste.dto.EstoqueResponseDTO;
import raizes.nordeste.exception.BusinessException;
import raizes.nordeste.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o ID: " + request.getUnidadeId()));

        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + request.getProdutoId()));

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
        
    @Transactional
    public void baixarEstoque(Long unidadeId, Long produtoId, int quantidadeVendida) {
        //Busca o registro de estoque da loja e produto
        Estoque estoque = estoqueRepository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto " + produtoId + " na unidade " + unidadeId));

        // Verifica se há estoque suficiente antes de vender
        if (estoque.getQuantidade() < quantidadeVendida) {
            throw new BusinessException("Estoque insuficiente para o produto: " + estoque.getProduto().getDescricao() + 
                                            ". Disponível: " + estoque.getQuantidade() + ", Solicitado: " + quantidadeVendida);
        }

        //Subtrai a quantidade do estoque
        int novaQuantidade = estoque.getQuantidade() - quantidadeVendida;
        estoque.setQuantidade(novaQuantidade);

        estoqueRepository.save(estoque);
    }
    
    @Transactional
    public List<Map<String, Object>>consultarEstoqueUnidade(Long unidadeId){
    //Lista itens ativos e maiores que zero - valor mínimo pode ser ajustado depois
    	List<Estoque> estoques = estoqueRepository
                .findByUnidadeIdAndProdutoAtivoTrueAndQuantidadeGreaterThan(unidadeId, 0);
    	
    	return estoques.stream().map(estoque -> {
    		Map<String, Object> dadosCardapio = new HashMap<>();
    		dadosCardapio.put("produtoId",estoque.getProduto().getId());
    		dadosCardapio.put("descricao",estoque.getProduto().getDescricao());
    		dadosCardapio.put("preco",estoque.getProduto().getPreco());
    		dadosCardapio.put("quantidadeDisponivel",estoque.getQuantidade());
            return dadosCardapio;
    	}).collect(Collectors.toList());
    }	
}
package raizes.nordeste.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import raizes.nordeste.dto.ItemPedidoRequestDTO;
import raizes.nordeste.dto.ItemPedidoResponseDTO;
import raizes.nordeste.exception.BusinessException;
import raizes.nordeste.exception.ResourceNotFoundException;
import raizes.nordeste.model.ItemPedido;
import raizes.nordeste.model.Produto;
import raizes.nordeste.repository.ProdutoRepository;
import java.math.BigDecimal;

@Service
public class ItemPedidoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Método auxiliar que o PedidoService usará para construir a lista de entidades
    public ItemPedido criarEntidadeItem(ItemPedidoRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + dto.getProdutoId()));

        if (!produto.isAtivo()) {
            throw new BusinessException("O produto '" + produto.getDescricao() + "' não está disponível para venda.");
        }

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        
        // Preço do momento da venda
        item.setPrecoUnitario(produto.getPreco());
        
        // Calcula o subtotal: preco * quantidade
        BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(dto.getQuantidade()));
        item.setSubtotal(subtotal);

        return item;
    }

    public ItemPedidoResponseDTO converterParaResponseDTO(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoDescricao(item.getProduto().getDescricao());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}

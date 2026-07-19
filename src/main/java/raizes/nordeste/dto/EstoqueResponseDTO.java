package raizes.nordeste.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EstoqueResponseDTO {
    private Long id;
    private Long unidadeId;
    private Long produtoId;
    private String produtoDescricao; //Incluído descrição para identificar produto
    private int quantidade;
	public void setPreco(BigDecimal preco) {
		
	}
}

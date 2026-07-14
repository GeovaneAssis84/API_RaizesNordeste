package raizes.nordeste.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
	private Long id;
	private String descricao;
	private BigDecimal preco;
	private boolean ativo;
	
}

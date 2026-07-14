package raizes.nordeste.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {

	@NotBlank(message = "A descrição do produto é obrigatório")
	private String descricao;
	
	@NotNull(message = "O preço é obrigatório")
	@Positive(message = "O preço deve ser maior que zero")
	private BigDecimal preco;
	
	private boolean ativo = true;	
}

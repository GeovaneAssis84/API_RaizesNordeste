package raizes.nordeste.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemPedidoRequestDTO {

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @Positive(message = "A quantidade de itens deve ser maior que zero.")
    private int quantidade;
}

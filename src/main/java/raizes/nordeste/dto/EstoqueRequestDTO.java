package raizes.nordeste.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class EstoqueRequestDTO {

    @NotNull(message = "O ID da unidade é obrigatório.")
    private Long unidadeId;

    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;

    @PositiveOrZero(message = "A quantidade deve ser zero ou um valor positivo.")
    private int quantidade;
}
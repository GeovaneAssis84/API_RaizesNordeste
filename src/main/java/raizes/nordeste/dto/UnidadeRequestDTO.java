package raizes.nordeste.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import raizes.nordeste.model.StatusUnidade;

@Data
public class UnidadeRequestDTO {

    @NotBlank(message = "O endereco é obrigatório.")
    private String endereco;

    @NotNull(message = "O status da unidade é obrigatório.")
    private StatusUnidade status;
}
package raizes.nordeste.dto;


import lombok.Data;
import raizes.nordeste.model.StatusUnidade;

@Data
public class UnidadeResponseDTO {
    private Long id;
    private String endereco;
    private StatusUnidade status;
}

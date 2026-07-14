package raizes.nordeste.dto;

import lombok.Data;

@Data
public class EstoqueResponseDTO {
    private Long id;
    private Long unidadeId;
    private Long produtoId;
    private String produtoDescricao; //Incluído descrição para identificar produto
    private int quantidade;
}

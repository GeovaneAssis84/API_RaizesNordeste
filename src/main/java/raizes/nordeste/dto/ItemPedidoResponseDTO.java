package raizes.nordeste.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoDescricao;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}

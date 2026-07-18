package raizes.nordeste.dto;


import lombok.Data;
import raizes.nordeste.model.CanalCompra;
import raizes.nordeste.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private Long unidadeId;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private BigDecimal valorBruto;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;
    private List<ItemPedidoResponseDTO> itens;
    private CanalCompra plataforma;
    private Long usuarioId;         
    private String usuarioNome;
}

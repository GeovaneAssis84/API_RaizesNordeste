package raizes.nordeste.dto;


import lombok.Data;
import raizes.nordeste.model.StatusPagamento;
import raizes.nordeste.model.TipoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoResponseDTO {
    private Long id;
    private Long pedidoId;
    private TipoPagamento tipoPag;
    private StatusPagamento statusPag;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
}

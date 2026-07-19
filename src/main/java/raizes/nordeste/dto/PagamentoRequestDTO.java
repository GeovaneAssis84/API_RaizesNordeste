package raizes.nordeste.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import raizes.nordeste.model.TipoPagamento;

@Data
public class PagamentoRequestDTO {

    @NotNull(message = "O ID do pedido é obrigatório.")
    private Long pedidoId;

    @NotNull(message = "O tipo de pagamento (PIX, CARTAO, DINHEIRO) é obrigatório.")
    private TipoPagamento tipoPag;
}
package raizes.nordeste.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import raizes.nordeste.model.CanalCompra;

import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "O ID da unidade/loja é obrigatório.")
    private Long unidadeId;

    @NotEmpty(message = "O pedido deve conter pelo menos um item.")
    @Valid 
    private List<ItemPedidoRequestDTO> itens;
    
    @NotNull(message = "A plataforma/canal de compra é obrigatória.")
    private CanalCompra plataforma;
    
    @NotNull(message = "O ID do usuário (Cliente/Toten/Funcionário) é obrigatório.")
    private Long usuarioId;
}
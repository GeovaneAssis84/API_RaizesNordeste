package raizes.nordeste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raizes.nordeste.dto.PagamentoRequestDTO;
import raizes.nordeste.dto.PagamentoResponseDTO;
import raizes.nordeste.model.ItemPedido;
import raizes.nordeste.model.Pagamento;
import raizes.nordeste.model.Pedido;
import raizes.nordeste.model.StatusPagamento;
import raizes.nordeste.model.StatusPedido;
import raizes.nordeste.repository.PagamentoRepository;
import raizes.nordeste.repository.PedidoRepository;

import java.time.LocalDateTime;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public PagamentoResponseDTO iniciarPagamento(PagamentoRequestDTO request) {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + request.getPedidoId()));

        // Só permite pagar se o pedido estiver CRIADO
        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus()!= StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Este pedido não está disponível para iniciar pagamento. Status atual: " + pedido.getStatus());
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setTipoPag(request.getTipoPag());
        pagamento.setStatusPag(StatusPagamento.PENDENTE);
        pagamento.setValor(pedido.getValorTotal()); // Puxa o valor líquido do pedido

        // Avança o status do pedido para AGUARDANDO_PAGAMENTO usando a lógica do seu Service
        pedidoService.atualizarStatus(pedido.getId(), StatusPedido.AGUARDANDO_PAGAMENTO);

        return converterParaResponseDTO(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public PagamentoResponseDTO confirmarPagamento(Long pagamentoId, boolean aprovado) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com o ID: " + pagamentoId));

        if (pagamento.getStatusPag() != StatusPagamento.PENDENTE) {
            throw new IllegalStateException("Este pagamento já foi processado anteriormente.");
        }

        Pedido pedido = pagamento.getPedido();

        if (aprovado) {
            pagamento.setStatusPag(StatusPagamento.APROVADO);
            pagamento.setDataPagamento(LocalDateTime.now());

            // Baixar item por item do pedido
            Long unidadeId = pedido.getUnidade().getId();
            for (ItemPedido item : pedido.getItens()) {
                estoqueService.baixarEstoque(unidadeId, item.getProduto().getId(), item.getQuantidade());
            }

            // Atualiza o pedido para a cozinha (AGUARDANDO_PRODUCAO)
            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.AGUARDANDO_PRODUCAO);

        } else {
            pagamento.setStatusPag(StatusPagamento.RECUSADO);
            // Se o pagamento falhar, o pedido volta a ficar CRIADO para o cliente tentar outra forma
            pedido.setStatus(StatusPedido.CRIADO);
            pedidoRepository.save(pedido);
        }

        return converterParaResponseDTO(pagamentoRepository.save(pagamento));
    }

    private PagamentoResponseDTO converterParaResponseDTO(Pagamento pagamento) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(pagamento.getId());
        dto.setPedidoId(pagamento.getPedido().getId());
        dto.setTipoPag(pagamento.getTipoPag());
        dto.setStatusPag(pagamento.getStatusPag());
        dto.setValor(pagamento.getValor());
        dto.setDataPagamento(pagamento.getDataPagamento());
        return dto;
    }
       
    @Transactional
    public PagamentoResponseDTO cancelarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com o ID: " + pagamentoId));

        if (pagamento.getStatusPag() == StatusPagamento.CANCELADO) {
            throw new IllegalStateException("Este pagamento já está cancelado.");
        }

        // Se o pagamento já estava aprovado e foi cancelado depois, devolvemos o estoque e cancelamos o pedido
        if (pagamento.getStatusPag() == StatusPagamento.APROVADO) {
            Pedido pedido = pagamento.getPedido();
            
            // Chama a regra de cancelamento do pedido que já cuida do estoque
            pedidoService.cancelarPedido(pedido.getId());
        }

        pagamento.setStatusPag(StatusPagamento.CANCELADO);
        return converterParaResponseDTO(pagamentoRepository.save(pagamento));
    }
    
    
}
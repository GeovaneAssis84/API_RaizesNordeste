package raizes.nordeste.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raizes.nordeste.dto.PedidoRequestDTO;
import raizes.nordeste.dto.PedidoResponseDTO;
import raizes.nordeste.exception.BusinessException;
import raizes.nordeste.exception.ResourceNotFoundException;
import raizes.nordeste.model.ItemPedido;
import raizes.nordeste.model.Pedido;
import raizes.nordeste.model.StatusPedido;
import raizes.nordeste.model.StatusUnidade;
import raizes.nordeste.model.Unidade;
import raizes.nordeste.model.Usuario;
import raizes.nordeste.repository.PedidoRepository;
import raizes.nordeste.repository.UnidadeRepository;
import raizes.nordeste.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
        // Verifica se a unidade existe e se está ABERTA
        Unidade unidade = unidadeRepository.findById(request.getUnidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o ID: " + request.getUnidadeId()));

        if (unidade.getStatus() == StatusUnidade.FECHADA) {
            throw new BusinessException("Não é possível realizar pedidos nesta unidade pois ela está FECHADA.");
            
	    }
        
        // VALIDAÇÃO DO USUÁRIO
	    Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
	            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + request.getUsuarioId()));

        // Pedido com os dados básicos
        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setUsuario(usuario);
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setPlataforma(request.getPlataforma());

        // Converte os itens recebidos e calcula os subtotais 
        BigDecimal valorBruto = BigDecimal.ZERO;
        for (var itemDto : request.getItens()) {
            ItemPedido item = itemPedidoService.criarEntidadeItem(itemDto);
            item.setPedido(pedido);
            pedido.getItens().add(item);
            
            valorBruto = valorBruto.add(item.getSubtotal());
        }

        //Estrutura de cálculo de valores (depois definir lógica de Fidelidade e Promoções)
        BigDecimal valorDesconto = BigDecimal.ZERO; // Deixando fixado em 0 até definir métodos
        BigDecimal valorTotal = valorBruto.subtract(valorDesconto);

        pedido.setValorBruto(valorBruto);
        pedido.setValorDesconto(valorDesconto);
        pedido.setValorTotal(valorTotal);

        //Salva pedido e itens juntos
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return converterParaResponseDTO(pedidoSalvo);
    }

    private PedidoResponseDTO converterParaResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setUnidadeId(pedido.getUnidade().getId());
        dto.setStatus(pedido.getStatus());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setValorBruto(pedido.getValorBruto());
        dto.setValorDesconto(pedido.getValorDesconto());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setPlataforma(pedido.getPlataforma());
        dto.setUsuarioId(pedido.getUsuario().getId());           
        dto.setUsuarioNome(pedido.getUsuario().getNome());
        
        
        dto.setItens(pedido.getItens().stream()
                .map(itemPedidoService::converterParaResponseDTO)
                .collect(Collectors.toList()));
        return dto;
    }
    
    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));

        // Não pode cancelar se já estiver em produção, pronto ou entregue
        if (pedido.getStatus() == StatusPedido.EM_PRODUCAO || 
            pedido.getStatus() == StatusPedido.PRONTO || 
            pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível cancelar um pedido que já está " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        return converterParaResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));

        // Não pode pular etapa
        validarTransicaoStatus(pedido.getStatus(), novoStatus);

        pedido.setStatus(novoStatus);
        return converterParaResponseDTO(pedidoRepository.save(pedido));
    }

   
   
    //Método para não pular etapa
    private void validarTransicaoStatus(StatusPedido atual, StatusPedido proximo) {
        boolean transicaoValida = switch (atual) {
            case CRIADO -> proximo == StatusPedido.AGUARDANDO_PAGAMENTO || proximo == StatusPedido.CANCELADO;
            case AGUARDANDO_PAGAMENTO -> proximo == StatusPedido.AGUARDANDO_PRODUCAO || proximo == StatusPedido.CANCELADO;
            case AGUARDANDO_PRODUCAO -> proximo == StatusPedido.EM_PRODUCAO || proximo == StatusPedido.CANCELADO;
            case EM_PRODUCAO -> proximo == StatusPedido.PRONTO; 
            case PRONTO -> proximo == StatusPedido.ENTREGUE;
            case ENTREGUE, CANCELADO -> false; // Estados finais não mudam mais
        };

        if (!transicaoValida) {
            throw new BusinessException("Transição de status inválida: Não é permitido mudar de " + atual + " para " + proximo);
        }
    }

	public PedidoResponseDTO consultarPedido(Long id) {
	       Pedido pedido = pedidoRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));

		return converterParaResponseDTO(pedido);
	}
    
}

package raizes.nordeste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raizes.nordeste.dto.UnidadeRequestDTO;
import raizes.nordeste.dto.UnidadeResponseDTO;
import raizes.nordeste.exception.ResourceNotFoundException;
import raizes.nordeste.model.StatusUnidade;
import raizes.nordeste.model.Unidade;
import raizes.nordeste.repository.UnidadeRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Transactional
    public UnidadeResponseDTO cadastrar(UnidadeRequestDTO request) {
        Unidade unidade = new Unidade();
        unidade.setEndereco(request.getEndereco());
        unidade.setStatus(request.getStatus());

        return converterParaResponseDTO(unidadeRepository.save(unidade));
    }

    @Transactional
    public void atualizarStatus(Long id, StatusUnidade novoStatus) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o ID: " + id));
        
        unidade.setStatus(novoStatus);
        
        unidadeRepository.save(unidade);
    }

    @Transactional(readOnly = true)
    public List<UnidadeResponseDTO> listarTodas() {
        return unidadeRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    private UnidadeResponseDTO converterParaResponseDTO(Unidade unidade) {
        UnidadeResponseDTO dto = new UnidadeResponseDTO();
        dto.setId(unidade.getId());
        dto.setEndereco(unidade.getEndereco());
        dto.setStatus(unidade.getStatus());
        return dto;
    }
}

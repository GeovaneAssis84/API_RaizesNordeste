package raizes.nordeste.service;

import raizes.nordeste.dto.UsuarioRequestDTO;
import raizes.nordeste.dto.UsuarioResponseDTO;
import raizes.nordeste.model.Usuario;
import raizes.nordeste.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setTipoUsuario(request.getTipoUsuario());
        usuario.setParticipaFidelidade(request.isParticipaFidelidade());
        usuario.setStatusLogin(request.getStatusLogin());

        return converterParaResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        return converterParaResponseDTO(usuario);
    }

    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setParticipaFidelidade(usuario.isParticipaFidelidade());
        dto.setStatusLogin(usuario.getStatusLogin());
        return dto;
    }
}

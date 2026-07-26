package raizes.nordeste.service;

import raizes.nordeste.dto.UsuarioRequestDTO;
import raizes.nordeste.dto.UsuarioResponseDTO;
import raizes.nordeste.dto.RegistroClienteRequestDTO;
import raizes.nordeste.exception.ResourceNotFoundException;
import raizes.nordeste.exception.BusinessException;
import raizes.nordeste.model.Usuario;
import raizes.nordeste.model.TipoUsuario;
import raizes.nordeste.model.StatusLogin;
import raizes.nordeste.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUsuario(request.getTipoUsuario());
        usuario.setParticipaFidelidade(request.isParticipaFidelidade());
        usuario.setStatusLogin(request.getStatusLogin());

        return converterParaResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO cadastrarCliente(RegistroClienteRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        usuario.setParticipaFidelidade(request.isParticipaFidelidade());
        usuario.setStatusLogin(StatusLogin.ATIVO);

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
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return converterParaResponseDTO(usuario);
    }

    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setParticipaFidelidade(usuario.isParticipaFidelidade());
        dto.setStatusLogin(usuario.getStatusLogin());
        return dto;
    }
}

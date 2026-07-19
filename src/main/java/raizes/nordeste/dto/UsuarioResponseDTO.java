package raizes.nordeste.dto;

import lombok.Data;
import raizes.nordeste.model.StatusLogin;
import raizes.nordeste.model.TipoUsuario;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private TipoUsuario tipoUsuario;
    private boolean participaFidelidade;
    private StatusLogin statusLogin;
}
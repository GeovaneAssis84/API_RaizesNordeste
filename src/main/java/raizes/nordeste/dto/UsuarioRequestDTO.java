package raizes.nordeste.dto;

import raizes.nordeste.model.TipoUsuario;
import raizes.nordeste.model.StatusLogin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nome;

    @NotNull(message = "O tipo de usuário (CLIENTE, FUNCIONARIO, TOTEN) é obrigatório.")
    private TipoUsuario tipoUsuario;

    private boolean participaFidelidade;

    @NotNull(message = "O status de login (ATIVO, INATIVO) é obrigatório.")
    private StatusLogin statusLogin; //Talvez não vai precisar com Spring Security
}
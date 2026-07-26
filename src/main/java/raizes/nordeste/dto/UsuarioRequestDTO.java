package raizes.nordeste.dto;

import raizes.nordeste.model.TipoUsuario;
import raizes.nordeste.model.StatusLogin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail do usuário é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String senha;

    @NotNull(message = "O tipo de usuário (CLIENTE, FUNCIONARIO, TOTEN) é obrigatório.")
    private TipoUsuario tipoUsuario;

    private boolean participaFidelidade;

    @NotNull(message = "O status de login (ATIVO, INATIVO) é obrigatório.")
    private StatusLogin statusLogin; //Talvez não vai precisar com Spring Security
}

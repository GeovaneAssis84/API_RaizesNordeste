package raizes.nordeste.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "participa_fidelidade", nullable = false)
    private boolean participaFidelidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_login", nullable = false)
    private StatusLogin statusLogin;
}

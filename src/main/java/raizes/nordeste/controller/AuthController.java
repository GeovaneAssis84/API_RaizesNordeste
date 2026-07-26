package raizes.nordeste.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import raizes.nordeste.dto.LoginRequestDTO;
import raizes.nordeste.dto.LoginResponseDTO;
import raizes.nordeste.dto.RegistroClienteRequestDTO;
import raizes.nordeste.dto.UsuarioResponseDTO;
import raizes.nordeste.security.JwtService;
import raizes.nordeste.security.UsuarioPrincipal;
import raizes.nordeste.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Registro e autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Retorna um token JWT para e-mail e senha válidos.")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        return ResponseEntity.ok(new LoginResponseDTO(
                token, "Bearer", 3600, principal.getUsuario().getTipoUsuario()));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar cliente", description = "Cria somente usuários do perfil CLIENTE.")
    public ResponseEntity<UsuarioResponseDTO> registrarCliente(
            @Valid @RequestBody RegistroClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarCliente(request));
    }
}

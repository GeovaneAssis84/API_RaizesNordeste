package raizes.nordeste.controller;

import jakarta.validation.Valid;
import raizes.nordeste.dto.UsuarioRequestDTO;
import raizes.nordeste.dto.UsuarioResponseDTO;
import raizes.nordeste.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operações relacionadas ao gerenciamento dos Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Cadastrar um usuário",
            description = "Cadastrar um Usuário(Cleinte, Funcionário ou Toten)"
        )
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar usuários",
            description = "Listar Usuários(Cleinte, Funcionário ou Toten)"
        )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(
            summary = "Consultar um usuário",
            description = "Consultar um Usuário pelo Id"
        )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
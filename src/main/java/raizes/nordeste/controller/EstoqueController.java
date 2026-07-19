package raizes.nordeste.controller;


import jakarta.validation.Valid;
import raizes.nordeste.dto.EstoqueRequestDTO;
import raizes.nordeste.dto.EstoqueResponseDTO;
import raizes.nordeste.service.EstoqueService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    // Rota para alimentar ou ajustar a quantidade de um produto em uma unidade
    @PutMapping
    public ResponseEntity<EstoqueResponseDTO> atualizarEstoque(@Valid @RequestBody EstoqueRequestDTO request) {
        EstoqueResponseDTO response = estoqueService.salvarOuAtualizar(request);
        return ResponseEntity.ok(response);
    }
    
    //Rota para consultar estoque da unidade e poder montar o cardapio
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<Map<String, Object>>> consultarEstoque(@PathVariable Long unidadeId) {
        List<Map<String, Object>> estoque = estoqueService.consultarEstoqueUnidade(unidadeId);
        return ResponseEntity.ok(estoque);
    }
    
}

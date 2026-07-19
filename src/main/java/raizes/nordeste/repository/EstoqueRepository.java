package raizes.nordeste.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import raizes.nordeste.model.Estoque;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    
    // Para buscar produto em uma loja específica
    Optional<Estoque> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
    
    //Consultar cardapio disponivel para unidade
    List<Estoque> findByUnidadeIdAndProdutoAtivoTrueAndQuantidadeGreaterThan(Long unidadeId, int quantidadeMinima);
}
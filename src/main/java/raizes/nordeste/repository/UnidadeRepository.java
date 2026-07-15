package raizes.nordeste.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raizes.nordeste.model.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
   
}
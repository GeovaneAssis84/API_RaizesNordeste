package raizes.nordeste.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "tb_unidades")
@Data
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String endereco; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUnidade status;

    
    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Estoque> estoques;
}




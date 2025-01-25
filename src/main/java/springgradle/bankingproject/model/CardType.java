package springgradle.bankingproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "card_types")
@Entity
public class CardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 15, nullable = false ,unique = true)
    private String name;
    private Boolean isDeleted;
    @OneToMany(mappedBy = "cardType")
    private List<Card> cards;
}

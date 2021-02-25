import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.PrimaryKey;
import fr.thomas.orm.annotations.Table;

/**
 * Classe de test
 * @author tpeyr
 */
@Table(name = "t_produit")
public class Produit{
	
	@PrimaryKey
	@Column(name = "id_produit")
	private Long id; 
	
	@Column(name = "libelle_produit")
	private String libelle;
	
	@Column(name = "prix_produit")
	private Float prix;
}

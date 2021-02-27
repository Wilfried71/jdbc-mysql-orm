import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.PrimaryKey;
import fr.thomas.orm.annotations.Table;

@Table(name = "t_type_produit")
public class TypeProduit {
	
	@PrimaryKey
	@Column(name = "id_type_produit")
	private Long id;
	
	@Column(name = "libelle_type_produit")
	private String libelle;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public TypeProduit(Long id, String libelle) {
		super();
		this.id = id;
		this.libelle = libelle;
	}

	public TypeProduit() {
		super();
	}
	
	@Override
	public String toString() {
		return " { " +id + " : " + libelle + " } ";
	}
}

import java.util.Date;

import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.PrimaryKey;
import fr.thomas.orm.annotations.Table;

/**
 * Classe de test
 * 
 * @author tpeyr
 */
@Table(name = "t_produit")
public class ProduitLight {

	@PrimaryKey
	@Column(name = "id_produit")
	private Long id;

	@Column(name = "libelle_produit")
	private String libelle;

	@Column(name = "prix_produit")
	private Float prix;

	@Column(name = "date_creation")
	private Date dateCreation;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
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

	public Float getPrix() {
		return prix;
	}

	public void setPrix(Float prix) {
		this.prix = prix;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Long getTypeProduit() {
		return typeProduit;
	}

	public void setTypeProduit(Long typeProduit) {
		this.typeProduit = typeProduit;
	}

	@Column(name = "id_type_produit")
	private Long typeProduit;

	public ProduitLight() {
		super();
	}

	public ProduitLight(Long id, String libelle, Float prix, Date dateCreation, Boolean isDeleted, Long typeProduit) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.prix = prix;
		this.dateCreation = dateCreation;
		this.isDeleted = isDeleted;
		this.typeProduit = typeProduit;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return id + " : " + libelle + " - " + prix + "€ - " + dateCreation.toString() + " ///// " + typeProduit;
	}
}

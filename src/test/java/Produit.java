import java.util.Date;

import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.ForeignKey;
import fr.thomas.orm.annotations.PrimaryKey;
import fr.thomas.orm.annotations.Table;

/**
 * Classe de test
 * 
 * @author tpeyr
 */
@Table(name = "t_produit")
public class Produit {

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

	@ForeignKey(field = "id_type_produit", table = "t_type_produit")
	@Column(name = "id_type_produit")
	private TypeProduit typeProduit;

	public Produit() {
		super();
	}


	public Produit(Long id, String libelle, Float prix, Date dateCreation, Boolean isDeleted, TypeProduit typeProduit) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.prix = prix;
		this.dateCreation = dateCreation;
		this.isDeleted = isDeleted;
		this.typeProduit = typeProduit;
	}


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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	@Override
	public String toString() {
		return id + " : " + libelle + " - " + prix + "€ - " + dateCreation.toString() + " ///// " + typeProduit;
	}

	public ProduitLight mapToProduitLight() {
		return new ProduitLight(id, libelle, prix, dateCreation, isDeleted, typeProduit.getId());
	}
}

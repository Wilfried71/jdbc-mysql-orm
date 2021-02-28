import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import fr.thomas.orm.Model;
import fr.thomas.orm.ORMConfig;

public class ORMTests {
	
	// Modèle du produit
	Model<Produit> produitModel = new Model<Produit>(Produit.class);
	
	Model<ProduitLight> produitLightModel = new Model<ProduitLight>(ProduitLight.class);

	@Test
	public void test() throws Exception{
		/**
		 * Configure database
		 */
		ORMConfig.database = "bd-test";
		ORMConfig.username = "lambda";
		ORMConfig.server = "localhost";
		ORMConfig.password = "lambda";
		ORMConfig.serverTimeZone = "Europe/Paris";
		ORMConfig.port = "3306";
		
		
		/*List<Produit> produits = produitModel.findAll();
		System.out.println(produits);
		
		
		ProduitLight newProduit = new ProduitLight();
		newProduit.setLibelle("Produit test");
		newProduit.setPrix(9.99f);
		newProduit.setDateCreation(new Date());
		newProduit.setTypeProduit(1L);
		newProduit = produitLightModel.create(newProduit);
		System.out.println(produitModel.findById(newProduit.getId()));*/
		/*Produit p = new Produit();
		p.setId(16L);
		produitModel.delete(p);*/
		//produitModel.deleteById(15L);
		//System.out.println(produitModel.query("SELECT * FROM t_produit WHERE id_produit IN (?,?)", Arrays.asList(1L, 17L)));
	}

}

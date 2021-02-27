import java.util.List;

import org.junit.Test;

import fr.thomas.orm.Model;
import fr.thomas.orm.ORMConfig;

public class ORMTests {
	
	// Modèle du produit
	Model<Produit> produitModel = new Model<Produit>(Produit.class);
	
	

	@Test
	public void test() throws Exception{
		/**
		 * Configure database
		 */
		ORMConfig.database = "bd-test";
		ORMConfig.username = "lambda";
		ORMConfig.server = "localhost";
		ORMConfig.password = "lambda";
		ORMConfig.serverTimeZone = "UTC";
		ORMConfig.port = "3306";
		
		
		
		
		
		//System.out.println(produitModel.getColumns()); 
		List<Produit> produits = produitModel.findAll();
		System.out.println(produits);
		System.out.println(produitModel.findById(2L));
		
	}

}

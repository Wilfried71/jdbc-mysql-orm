import org.junit.Test;

import fr.thomas.orm.Model;

public class ORMTests {
	
	// Mod�le du produit
	Model<Produit> produitModel = new Model<Produit>(Produit.class);

	@Test
	public void test() {
		System.out.println(produitModel.getColumns(Produit.class)); 
		
	}

}

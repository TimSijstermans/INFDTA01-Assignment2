package models;

import java.util.HashMap;

//Map<ArticleId, Map<ArticleId, Deviation>>
public class DeviationMatrix extends HashMap<Integer, HashMap<Integer, Deviation>> {
	/**
	 * Dev(u1,u2) to update
	 * @param a1 Article 1 (the first one to go in every calc)
	 * @param a2 Article 2 (the second one to in every calc)
	 * @param add The deviation value to add
	 */
	public void updateADeviation(int a1, int a2, float add){
		this.get(a1).get(a2).updateDeviation(add);
		this.get(a2).get(a1).updateDeviation((add*-1));
	}
	
	public Deviation getDeviation(int uj, int ui){
		
		return this.get(uj).get(ui);
	}
	
}

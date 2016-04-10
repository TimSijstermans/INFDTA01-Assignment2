import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import models.ArticleRatings;
import models.Deviation;
import models.DeviationMatrix;
import models.User;

public class Application {
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Application App = new Application();
	}
	
	Map<Integer, ArticleRatings> articles = new HashMap<Integer, ArticleRatings>(); 
	DeviationMatrix deviationMatrix = new DeviationMatrix();
	HashMap<Integer, User> users = new HashMap<Integer, User>();
	
	public Application(){
		File filePath = new File("resources/u.data");
//		File filePath = new File("resources/userItem.data");
		
//		Implemented exercise from week4.pdf with this simple set to verify results. uncomment logTestSet() lower in the code to show related info
//		File filePath = new File("resources/test.data");
		
		Date startDate = new Date();
		
		try {
			articles = ArticleMapper.readFile(filePath, ",");
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		deviationMatrix = calcDeviationsFromArticles(articles);
		
//		Show the DeviationMatrix
//		System.out.println("Deviation matrix: ");
//		for(Entry<Integer, HashMap<Integer, Deviation>> val : deviationMatrix.entrySet()){
//			System.out.println("\t"+val);
//		}
		
		users = createUsersFromArticles(articles);
		
		Date middleDate = new Date();
		
//		Only enable if using test.data
//		implemented exercise from week 4.pdf to verify results.
//		logTestSet();
		
		LinkedHashMap<Integer, Float> topTen = getTopRecommendationsForUser(predictAllMissingRatingsForUser(186), 5);
		
		System.out.println("Top 5 recommendations for user 186: ");
		for (Entry<Integer, Float> r : topTen.entrySet()){
			System.out.println("\tItem "+ r.getKey() +"\twith predicted rating of "+ r.getValue());
		}
				
//		Map<Integer, Float> userSeven = predictAllMissingRatingsForUser(7);
//		
//		System.out.println("\npredictions for user 7: ");
//		for (Entry<Integer, Float> r : userSeven.entrySet()){
//			System.out.println("\tItem "+ r.getKey() +"\twith predicted rating of "+ r.getValue());
//		}
//		
//		Map<Integer, Float> userThree = predictAllMissingRatingsForUser(3);
//		
//		System.out.println("\npredictions for user 3: ");
//		for (Entry<Integer, Float> r : userThree.entrySet()){
//			System.out.println("\tItem "+ r.getKey() +"\twith predicted rating of "+ r.getValue());
//		}
//		
//		System.out.println("\nUser 3 added rating 4.0 for item 105\n");
//		addRatingToUser(3, 105, (float) 4.0);
//		
//		userSeven = predictAllMissingRatingsForUser(7);
//		
//		System.out.println("predictions for user 7: ");
//		for (Entry<Integer, Float> r : userSeven.entrySet()){
//			System.out.println("\tItem "+ r.getKey() +"\twith predicted rating of "+ r.getValue());
//		}
		
		Date endDate = new Date();
		
		//Log timing stuff
		System.out.println("\nTime of starting: \n\t" + startDate.toString() + "\nTime after creating deviationMatrix:\n\t" + middleDate.toString() + "\nTime of end:\n\t" + endDate.toString());
		
		System.out.println("\nTime between start and initializing all data (Including deviation matrix) in MS:\n\t" 
				+ (middleDate.getTime()-startDate.getTime()) + "\nAfter all initialisation up to end:\n\t" 
				+ (endDate.getTime()-middleDate.getTime())+ "\nTotal MS:\n\t" 
				+ (endDate.getTime()-startDate.getTime()));
	}
	
	/**
	 * Creates a DeviationMatrix from all articles.
	 * @param articles 			Map of all articles, with ratings by users
	 * @return DeviationMatrix 	with all deviations
	 */
	private DeviationMatrix calcDeviationsFromArticles(Map<Integer, ArticleRatings> articles){
		DeviationMatrix result = new DeviationMatrix();

		for(ArticleRatings ratings : articles.values()){
			List<Integer> articlesToCheck = new ArrayList<>();
			
			articlesToCheck.addAll(articles.keySet());
			articlesToCheck.remove(ratings.getOwnerId());
			for(Entry<Integer, ArticleRatings> article : articles.entrySet()){
				if(article.getKey() == ratings.getOwnerId()){
					continue;
				}
				
				Set<Integer> intersect = new HashSet<Integer>(article.getValue().keySet());
				intersect.retainAll(ratings.keySet());
				
				int n = intersect.size();

				float totalDev = 0;
				for (Integer userId : intersect){
					totalDev += (ratings.get(userId) - (article.getValue().get(userId)));
				}
				
				Deviation dev1 = new Deviation(n, totalDev);
				Deviation dev2 = new Deviation(n, (totalDev*-1));
				
				HashMap<Integer, Deviation> r1 = new HashMap<Integer, Deviation>();
				if(result.containsKey(ratings.getOwnerId())){
					r1 = result.get(ratings.getOwnerId());
				}
				HashMap<Integer, Deviation> r2 = new HashMap<Integer, Deviation>();
				if(result.containsKey(article.getKey())){
					r2 = result.get(article.getKey());
				}					
				
				r1.put(article.getKey(), dev1);
				r2.put(ratings.getOwnerId(), dev2);
				
				result.put(ratings.getOwnerId(), r1);
				result.put(article.getKey(), r2);
			}
		}		
		return result;
	}
	
	/**
	 * Turns the map of articles into a map of users
	 * @param articles					Map of all articles, with ratings by users
	 * @return HashMap<Integer, User> 	Map of all Users, with articles they rated
	 */
	private HashMap<Integer, User> createUsersFromArticles(Map<Integer, ArticleRatings> articles){
		HashMap<Integer, User> result = new HashMap<Integer, User>();
		
		for (Entry<Integer, ArticleRatings> article : articles.entrySet()){
			for(Entry<Integer, Float> rating : article.getValue().entrySet()){
				if (result.containsKey(rating.getKey())){
					User u = result.get(rating.getKey());
					u.put(article.getKey(), rating.getValue());
					result.put(rating.getKey(), u);
				} else {
					User u = new User();
					u.setUserId(rating.getKey());
					u.put(article.getKey(), rating.getValue());
					result.put(rating.getKey(), u);
				}
			}
			
		}
		
		return result;
	}
	
	/**
	 * Adds a rating to the user and performs the necessary operations for updating the rest of the data. (deviationmatrix, articles Map)
	 * 
	 * @param userId			
	 * @param articleId			
	 * @param rating			
	 */
	private void addRatingToUser(Integer userId, Integer articleId, Float rating){
		User u = users.get(userId);
		if (!u.containsKey(articleId)){
			u.put(articleId, rating);
			
			for (Entry<Integer, Float> entry : u.entrySet()){
				if (entry.getKey() == articleId){
					continue;
				}
				
				deviationMatrix.updateADeviation(articleId, entry.getKey(), rating-entry.getValue());
			}
			ArticleRatings a = articles.get(articleId);
			a.put(userId, rating);
			articles.replace(articleId, a);
			users.replace(userId, u);
		}
	}
	
	/**
	 * Create a set of all missing articles for this user, loop through them to create predictions for all of them.
	 * @param targetUserId
	 * @return TreeMap<Integer, Float> predictedRatings
	 */
	private TreeMap<Integer, Float> predictAllMissingRatingsForUser(Integer targetUserId){
		Set<Integer> diff = new HashSet<Integer>(articles.keySet());
		diff.removeAll(users.get(targetUserId).keySet());
		
		TreeMap<Integer, Float> predictedRatings = new TreeMap<>();
		
		for(Integer aId : diff){
			predictedRatings.put(aId, createPrediction(targetUserId, aId));
		}
		return predictedRatings;
	}
	
	/**
	 * Creates a single prediction
	 * 
	 * @param targetUserId
	 * @param targetArticleId
	 * @return float prediction
	 */
	private float createPrediction(Integer targetUserId, Integer targetArticleId){
		float numerator = 0;
		float denominator = 0;
		for(Entry<Integer, Float> rating : users.get(targetUserId).entrySet()){
			Deviation deviation = deviationMatrix.getDeviation(targetArticleId, rating.getKey());
			if (deviation.getUserCount() == 0)
				continue;
			numerator += (rating.getValue() + deviation.getDeviation()) * deviation.getUserCount();
			denominator += deviation.getUserCount();
		}
		return numerator/denominator;
	}
	
	/**
	 * Sort predictions Map in descending order and return top n items in a sorted map
	 * 
	 * @param predictions 						map with all predictions for the user
	 * @param n
	 * @return LinkedHashMap<Integer, Float> 	topRecommendations - sorted descending by value
	 */
	private LinkedHashMap<Integer, Float> getTopRecommendationsForUser(
			TreeMap<Integer, Float> predictions, int n) {
		
		Map<Integer, Float> sorted = sortByValue(predictions);
		LinkedHashMap<Integer, Float> result = new LinkedHashMap<>();
		int i = 0;
		for(Entry<Integer, Float> entry : sorted.entrySet()){
			result.put(entry.getKey(), entry.getValue());
			i++;
			if (i == n)
				break;
		}
		return result;
	}
		
	/**
	 * Special for testSet
	 */
	private void logTestSet() {
		System.out.println("'Week 4.pdf' page 27");
		System.out.println("");
		System.out.println("Users: \tArticles");
		System.out.println("1=John \t10=ItemA");
		System.out.println("2=Amy \t11=ItemB");
		System.out.println("3=Mark \t12=ItemC");
		System.out.println("4=Lucy");
		System.out.println("");
		System.out.println("Deviation matrix");
		for(Entry<Integer, HashMap<Integer, Deviation>> val : deviationMatrix.entrySet()){
			System.out.println(val);
		}
		System.out.println("");
		System.out.println("Prediction for Lucy with Item A: "+ createPrediction(4,10));
		System.out.println("Prediction for Mark with Item C: "+ createPrediction(3,12));
		
		HashMap<Integer, TreeMap<Integer, Float>> predictionsForAllUsers = new HashMap<Integer, TreeMap<Integer, Float>>();
		for(int uId : users.keySet()){
			predictionsForAllUsers.put(uId, predictAllMissingRatingsForUser(uId));
		}
			
		System.out.println("\n"+predictionsForAllUsers);
		
		System.exit(0);
	}
	
	
	/**
	 * Comperator for map values
	 * 
	 * @param map
	 * @return
	 */
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
	    
	    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
	        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    });
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
}

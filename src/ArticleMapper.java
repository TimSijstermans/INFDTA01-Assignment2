import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import models.ArticleRatings;

public class ArticleMapper {
	
	//MAP<ARTICLEID, MAP<USERID, RATING>
	//MAP<ARTICLEID, RATINGS>
	public static Map<Integer, ArticleRatings> readFile(File path, String delimiter) throws FileNotFoundException, IOException {
		
		Map<Integer, ArticleRatings> articles = new TreeMap<Integer, ArticleRatings>();

        BufferedReader bf = new BufferedReader(new FileReader(path));
        String line = "";

        while ((line = bf.readLine()) != null) {
            String parts[] = line.split(delimiter);
            //part[0] = user id, part[1] = article id, part[2] = rating
            
            if (articles.get(Integer.parseInt(parts[1])) == null) {
            	ArticleRatings ratings = new ArticleRatings();
            	ratings.put(Integer.parseInt(parts[0]), Float.parseFloat(parts[2]));
            	ratings.setOwnerId(Integer.parseInt(parts[1]));
            	articles.put(Integer.parseInt(parts[1]), ratings);
            } else {
            	ArticleRatings ratings = articles.get(Integer.parseInt(parts[1]));
            	ratings.put(Integer.parseInt(parts[0]), Float.parseFloat(parts[2]));
                articles.put(Integer.parseInt(parts[1]), ratings);
            }
        }
        bf.close();
        return articles;
	}
}

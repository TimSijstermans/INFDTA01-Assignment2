package models;

import java.util.HashMap;

//HashMap<USERID, RATING>
public class ArticleRatings extends HashMap<Integer, Float> {
	private Integer ownerId;

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
}

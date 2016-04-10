package models;

import java.util.HashMap;

//Map<ArticleId, Rating>
public class UserRatings extends HashMap<Integer, Float> {
	private Integer ownerId;

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
}

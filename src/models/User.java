package models;

import java.util.HashMap;

public class User extends HashMap<Integer, Float> {
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}

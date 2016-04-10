package models;

public class Deviation {
	private float deviation;
	private float userCount;
	private float totalDev;
	
	public Deviation(Integer userCount, Float totalDev){
		this.userCount = userCount;
		this.totalDev = totalDev;
		this.deviation = calcDeviation();
	}
	
	public void updateDeviation(Float add){
		this.totalDev += add;
		userCount += 1;
		deviation = calcDeviation();
	}
	
	@Override
	public String toString(){
		return Float.toString(deviation);
	}
	
	private float calcDeviation(){
		return totalDev/userCount;
	}
	
	public float getDeviation() {
		return deviation;
	}

	public float getUserCount() {
		return userCount;
	}

	public float getTotalDev() {
		return totalDev;
	}

}

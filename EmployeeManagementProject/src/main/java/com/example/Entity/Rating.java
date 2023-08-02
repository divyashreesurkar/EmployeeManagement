package com.example.Entity;

public class Rating {

	private int ratingId;
	private int empId;
	private String managerName;
	private int rating;
	private String feedback;

	public Rating() {
		super();
	}

	public Rating(int ratingId, int empId, String managerName, int rating, String feedback) {
		super();
		this.ratingId = ratingId;
		this.empId = empId;
		this.managerName = managerName;
		this.rating = rating;
		this.feedback = feedback;
	}

	public int getRatingId() {
		return ratingId;
	}

	public void setRatingId(int ratingId) {
		this.ratingId = ratingId;
	}

	

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	@Override
	public String toString() {
		return "Rating [ratingId=" + ratingId + ", empId=" + empId + ", managerName=" + managerName + ", rating="
				+ rating + ", feedback=" + feedback + "]";
	}

	
}

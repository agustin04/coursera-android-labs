package com.example.moderartui;

public class SquareColor {
	private int id,initialColor, finalColor, currentColor;
	
	public SquareColor(int id, int initialColor, int finalColor){
		this.id = id;
		this.initialColor = initialColor;
		currentColor = initialColor;
		this.finalColor = finalColor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInitialColor() {
		return initialColor;
	}

	public void setInitialColor(int initialColor) {
		this.initialColor = initialColor;
	}

	public int getFinalColor() {
		return finalColor;
	}

	public void setFinalColor(int finalColor) {
		this.finalColor = finalColor;
	}

	public int getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(int currentColor) {
		this.currentColor = currentColor;
	}
	
	
	
	

}

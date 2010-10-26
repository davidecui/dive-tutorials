package org.whymca.dive.model;

//very simple Model object, just two floats
public class Dive {
	private final float coeff;
	private final float averageMark;
	
	public Dive(float coeff, float averageMark) {
		super();
		this.coeff = coeff;
		this.averageMark = averageMark;
	}

	public float getCoeff() {
		return coeff;
	}

	public float getAverageMark() {
		return averageMark;
	}
}

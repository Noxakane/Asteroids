package de.robo.felix.render;

import java.awt.Color;

public class ColorSpot {
	float when;
	Color color;
	
	public ColorSpot(float when, Color color) {
		this.when = when;
		this.color = color;
	}
	@Override
	public String toString() {
		return when+": "+color;
	}
}

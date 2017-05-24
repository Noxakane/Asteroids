package de.robo.felix;

public class Vector {

	public static final Vector ZERO = new Vector(0,0);
	public static final Vector E_X = new Vector(1, 0);
	public static final Vector E_Y = new Vector(0, 1);
	
	public double x, y;
	
	public Vector(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public Vector mul(double d) {
		return new Vector(x*d, y*d);
	}
	
	public Vector add(Vector v) {
		return new Vector(x+v.x, y+v.y);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	
	public Vector normalized() {
		if(length() == 0)
			return new Vector(0,0);
		Vector c = this.mul(1d / length());
		return c;
	}
	
	public double length() {
		return Math.sqrt(x*x + y*y);
	}
	
	@Override
	public String toString() {
		return "x="+x+" | y="+y;
	}
	
	public static Vector between(Vector from, Vector to) {
		return to.subtract(from);
	}
	
}

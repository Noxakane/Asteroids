package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import de.robo.felix.Vector;
import de.robo.felix.render.TextureCollection;

public class Laser extends GameObject implements Collidable {

	private double speed = 15;
	private Vector dir = new Vector(0, 1);
	private double length = 10;
	private ShooterSource shooter;
	private Image tex;
	
	public Laser(ShooterSource shooter) {
		this.shooter = shooter;
		tex = TextureCollection.lazorrrrr;
	}
	
	public ShooterSource getShooter() {
		return shooter;
	}
	
	@Override
	public void onCollideWith(Collidable other) {
		if(other instanceof Asteroid) {
			delete();
		}
	}

	@Override
	public void render(Graphics2D g) {
		/*AffineTransform transform = new AffineTransform();
		transform.translate(x, y);
		//transform.scale(2, 1);
		transform.rotate(2*Math.PI-Math.atan(dir.x / dir.y), 0, 0);
		
		g.drawImage(tex, transform, null);*/
		
		
		g.setColor(Color.RED);
		for(int i = 0; i < length; i++) {
			g.fillOval((int) Math.round(x-radius-dir.x*i), (int) Math.round(y-radius-dir.y*i), 
					(int) Math.round(2*radius), (int) Math.round(2*radius));
		}
	}
	
	@Override
	public void update() {
		x += dir.x * speed;
		y += dir.y * speed;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public void setDirection(Vector v) {
		this.dir = v.normalized();
	}

	@Override
	public boolean collidesWith(Collidable other) {
		return super.collides(other);
	}

}

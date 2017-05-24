package de.robo.felix.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.robo.felix.Vector;
import de.robo.felix.render.game.GameObject;

public class Particle extends GameObject {

	private Vector direction = Vector.ZERO;
	private double lifeTime = 2; //in sekunden;
	private double factor = .9;
	private double speed = 8;
	private Color color = Color.BLUE;
	public ColorBlender blender = null;
	
	public Particle(Vector direction, double lifeTime, double factor) {
		this.direction = direction.normalized();
		this.lifeTime = lifeTime;
		this.factor = factor;
		this.radius = 5;
	}
	
	public void setFactor(double fac) {
		this.factor = fac;
	}
	
	long spawnTime = 0;
	@Override
	public void onRegister() {
		spawnTime = System.currentTimeMillis();
	}
	
	@Override
	public void render(Graphics2D g) {
		long life = System.currentTimeMillis() - spawnTime;
		double perc = (double) life / (double) (lifeTime * 1000);

		Color color = this.color;
		if(blender != null)
			color = blender.get((float)perc*0.98f+0.01f);
		g.setColor(color); //TODO anpassbar machen
		g.fillOval((int) Math.round(x-radius), (int) Math.round(y-radius), 
				(int) Math.round(2*radius), (int) Math.round(2*radius));
	}
	
	public static Set<Particle> spawn(int amount, Vector mid, Vector spread, double speed, double lifeTime) {
		Set<Particle> set = new HashSet<>();
		Random random = new Random(System.nanoTime());
		for(int i = 0; i < amount; i++) {
			
			Vector dir = new Vector(random.nextDouble()-0.5, random.nextDouble()-0.5);
			Vector offset = new Vector(random.nextDouble()*spread.x-0.5*spread.x, random.nextDouble()*spread.y+0.5*spread.y);
			Particle p = new Particle(dir, lifeTime, 0.9);
			p.speed = speed;
			Vector pos = mid.add(offset);
			p.setPosition(pos.x, pos.y);
			
			set.add(p);
			RenderManager.add(p);
		}
		return set;
	}
	
	public void setDirection(Vector c) {
		this.direction = c;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	
	@Override
	public void update() {
		radius *= factor;
		x += direction.x * speed;
		y += direction.y * speed;
		
		if(System.currentTimeMillis() - spawnTime >= lifeTime * 1000)
			delete();
	}
}

package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import de.robo.felix.Vector;
import de.robo.felix.render.ColorBlender;
import de.robo.felix.render.ColorSpot;
import de.robo.felix.render.Particle;
import de.robo.felix.render.RenderManager;

public class ExplosionPowerUp extends PowerUp {

	private int explosionRadius = 250;
	private int explosionSpeed = 10;
	private double particlePerU = 0.05;
	private double positionScatter = 2;
	private double scaleScatter = 1;
	private double explosionDelay = 0.02;
	private int checkAll = 4;
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int) Math.round(x-radius), (int) Math.round(y-radius), 
				(int) Math.round(2*radius), (int) Math.round(2*radius));
	}

	private int currentRadius = 1;
	@Override
	public void onCollect(SpaceShuttle shuttle) {
		new Thread(() -> {
			
			int curr = 0;
			while(currentRadius < explosionRadius) {
				double U = 2 * Math.PI*currentRadius;
				double parAmount = U*particlePerU;
				double deltaAngle = 360/parAmount;
				
				Random random = new Random(System.nanoTime());
				for(double angle = 0; angle < 360; angle += deltaAngle) {
					double psc = random.nextDouble()*2*positionScatter-positionScatter;
					double ssc = random.nextDouble()*2*scaleScatter-scaleScatter;
					double dx = x+currentRadius * Math.cos(Math.toRadians(angle+psc));
					double dy = y+currentRadius * Math.sin(Math.toRadians(angle+psc));
					Particle p = new Particle(Vector.ZERO, 1, 0.8);
					p.setColor(Color.YELLOW);
					p.setRadius(5-ssc);
					p.setFactor(0.95);
					p.blender = new ColorBlender(Color.YELLOW, new Color(0.8f, 0, 0));
					p.blender.insert(new ColorSpot(0.4f, Color.RED));
					
					//new Vector(x,y).add(new Vector(dx, dy)).normalized().mul(2)
					p.setPosition(dx, dy);
					p.setDirection(Vector.between(new Vector(x, y), new Vector(dx, dy)).normalized());
					p.setSpeed(2);
					RenderManager.add(p);
					
				}
				
				curr++;
				currentRadius += explosionSpeed;
				
				if(curr == checkAll) {
					for(GameObject o : RenderManager.objectsAround(new Vector(x, y), currentRadius)) {
						if(o instanceof Asteroid) {
							((Asteroid)o).breakDown(true, false, shuttle);
						}
					}
					curr = 0;
				}
				
				try {
					Thread.sleep((long) (1000*explosionDelay));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			for(GameObject o : RenderManager.objectsAround(new Vector(x, y), explosionRadius, Asteroid.class)) {
				((Asteroid)o).breakDown(true, false, shuttle);
			}
			
		}).start();
	}

}

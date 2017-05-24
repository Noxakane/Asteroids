package de.robo.felix.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.DoubleToLongFunction;

import de.robo.felix.Vector;
import de.robo.felix.Window;
import de.robo.felix.render.game.Collidable;
import de.robo.felix.render.game.GameObject;
import de.robo.felix.render.game.SpaceShuttle;

public class RenderManager {

	private static List<Renderable> renderables = new ArrayList<Renderable>();
	private static List<GameObject> gameObjects = new ArrayList<GameObject>();
	private static List<Collidable> collidables = new ArrayList<Collidable>();
	private static List<Updateable> updateables = new ArrayList<Updateable>();
	private static List<GameObject> lazyDelete = new ArrayList<GameObject>();
	private static List<Renderable> lazyAdd = new ArrayList<Renderable>();
	
	public static SpaceShuttle shuttle;

	public static List<GameObject> objectsAround(Vector mid, double radius, Class<?> clazz) {
		List<GameObject> list = new ArrayList<GameObject>();
		try {
			for(GameObject go: gameObjects){
				double distance = go.getPosition().add(mid.mul(-1)).length();
				if(distance <= radius && clazz.isAssignableFrom(go.getClass()))
					list.add(go);
			}
		} catch(Exception e) {
			return objectsAround(mid, radius, clazz);
		}
		return list;
	}
	
	public static List<GameObject> objectsAround(Vector mid, double radius) {
		return objectsAround(mid, radius, GameObject.class);
	}
	
	
	
	public static void add(Renderable r) {
		if(r instanceof SpaceShuttle) {
			if(shuttle != null)
				return;
			shuttle = (SpaceShuttle) r;
		}

		lazyAdd.add(r);
	}
	
	public static List<Collidable> getCollidables() {
		return collidables;
	}
	
	private static void lazyAdd(Renderable r) {
		renderables.add(r);
		if(r instanceof GameObject) {
			((GameObject)r).onRegister();
			gameObjects.add((GameObject) r);
		}
		if(r instanceof Updateable) {
			updateables.add((Updateable) r);
		}
		if(r instanceof Collidable) {
			collidables.add((Collidable)r);
		}
		renderables.sort(new Comparator<Renderable>() {

			@Override
			public int compare(Renderable r1, Renderable r2) {
				if(r1 == null || r2 == null)
					return -1;
				if(r1.getZIndex() > r2.getZIndex())
					return 1;
				if(r1.getZIndex() < r2.getZIndex())
					return -1;
				return 0;
			}
			
		});
	}
	
	public static void renderAll(Graphics2D g) {
		BufferedImage image = new BufferedImage(Window.WIDTH, Window.HEIGHT, BufferedImage.TYPE_INT_BGR);
		Graphics2D gg = (Graphics2D) image.getGraphics();
		for(Renderable renderable : renderables) {
			renderable.render(gg);
		}
		g.drawImage(image, Camera.getXOffset(), Camera.getYOffset(), null);
	}
	
	public static void updateAll() {
		try {
			for(Updateable u : updateables) {
				u.update();
			}
			Camera.update();
		} catch(Exception e) {
			updateAll();
		}
	}
	
	public static List<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	private static boolean clearing = false;
	public static void clear() {
		if(clearing)
			return;
		
		clearing = true;
		try {
			for(GameObject o : lazyDelete) {
				gameObjects.remove(o);
				renderables.remove(o);
				updateables.remove(o);
				collidables.remove(o);
			}
			for(Renderable r : lazyAdd) {
				lazyAdd(r);
			}
			lazyAdd.clear();
			lazyDelete.clear();
		} catch(ConcurrentModificationException e) {
			clear();
		}
		clearing = false;
	}
	
	public static void delete(GameObject o) {
		try {
			lazyDelete.add(o);
		} catch(Exception e) {}
	}
	
}

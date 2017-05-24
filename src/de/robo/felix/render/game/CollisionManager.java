package de.robo.felix.render.game;

import de.robo.felix.render.RenderManager;

public class CollisionManager {

	public static void handleCollisions() {
		//Set<GameObject> used = new HashSet<GameObject>();
		for(Collidable a : RenderManager.getCollidables()) {
			for(Collidable b : RenderManager.getCollidables()) {
				if(a == b)
					continue;
				
				//if(used.contains(a) || used.contains(b))
				//	continue;
				
				if(a.collidesWith(b)) {
					a.onCollideWith(b);
				}
			}
		}
	}
	
}

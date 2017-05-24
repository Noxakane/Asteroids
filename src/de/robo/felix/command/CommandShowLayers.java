package de.robo.felix.command;

import de.robo.felix.render.RenderManager;
import de.robo.felix.render.Renderable;

public class CommandShowLayers implements Command {

	@Override
	public void execute(String[] args) {
		System.out.println("These are all registered GameObjects and its Layers:");
		for(Renderable r : RenderManager.getGameObjects()) {
			System.out.println(" - "+r.toString()+": "+r.getZIndex());
		}
	}

}

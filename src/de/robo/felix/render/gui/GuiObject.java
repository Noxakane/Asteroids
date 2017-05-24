package de.robo.felix.render.gui;

import de.robo.felix.render.Renderable;
import de.robo.felix.render.Updateable;

public abstract class GuiObject implements Renderable, Updateable {

	@Override
	public int getZIndex() {
		return 10000;
	}
	
}

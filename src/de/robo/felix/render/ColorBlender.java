package de.robo.felix.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColorBlender {
	private List<ColorSpot> spots = new ArrayList<ColorSpot>();
	
	public ColorBlender(Color start, Color end) {
		spots.add(new ColorSpot(0, start));
		spots.add(new ColorSpot(1, end));
		sortList();
	}	
	
	public void insert(ColorSpot spot) {
		spots.add(spot);
		sortList();
	}
	
	public ColorSpot getSpot(float when) {
		for(ColorSpot spot : spots) {
			if(spot.when == when)
				return spot;
		}
		return null;
	}
	
	public Color get(float dot) {
		dot = Math.min(dot, 1);
		dot = Math.max(dot, 0);

		ColorSpot exact = getSpot(dot);
		if(exact != null)
			return exact.color;
		
		ColorSpot[] interval = findColors(dot);
		ColorSpot xs = interval[0];
		ColorSpot ys = interval[1];
		
		Color x = xs.color;
		Color y = ys.color;
		
		float blending = 1 - (dot-xs.when);
		float inverse_blending = 1 - blending;

		float red =   x.getRed()   * blending   +   y.getRed()   * inverse_blending;
		float green = x.getGreen() * blending   +   y.getGreen() * inverse_blending;
		float blue =  x.getBlue()  * blending   +   y.getBlue()  * inverse_blending;
		red = Math.min(red, 255); red = Math.max(red, 0);
		green = Math.min(green, 255); green = Math.max(green, 0);
		blue = Math.min(blue, 255); blue = Math.max(blue, 0);
		return new Color(red/255, green/255, blue/255);
	}
	
	private ColorSpot[] findColors(float blending) {
		ColorSpot x = spots.get(0), y=null;;
		ColorSpot[] c = new ColorSpot[2];
		for(int i = 0; i < spots.size(); i++) {
			ColorSpot spot = spots.get(i);
			float when = spot.when;
			if(when >= blending) {
				y = spot;
				break;
			}
			x = spot;
		}
		c[0] = x;
		c[1] = y;
		return c;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(ColorSpot spot : spots)
			s+=spot.toString()+"\n";
		return s;
	}
	
	private void sortList() {
		spots.sort(new Comparator<ColorSpot>() {

			@Override
			public int compare(ColorSpot o1, ColorSpot o2) {
				if(o1 == null || o2 == null)
					return 1;
				if(o1.when > o2.when)
					return 1;
				if(o1.when < o2.when)
					return -1;
				return 0;
			}
		});
	}
}

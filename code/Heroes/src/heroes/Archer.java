package heroes;

import java.awt.Graphics;
import java.awt.Polygon;

public class Archer extends Hero{

	private int ARCHER_DRAW_MARGIN = 15;

	public Archer(PlayerID player_id) {
		super(HeroType.ARCHER, player_id);
	}

	@Override
	void draw(Graphics g, int off_x, int off_y) {
		// TODO Auto-generated method stub
		Polygon p = new Polygon();		
		p.addPoint(off_x+GUI.FIELD_SIZE/2, off_y+ARCHER_DRAW_MARGIN);
		p.addPoint(off_x+ARCHER_DRAW_MARGIN, off_y+GUI.FIELD_SIZE-ARCHER_DRAW_MARGIN);
		p.addPoint(off_x+GUI.FIELD_SIZE-ARCHER_DRAW_MARGIN, off_y+GUI.FIELD_SIZE-ARCHER_DRAW_MARGIN);	    
	    g.fillPolygon(p);
		draw_eq(g, off_x, off_y);
	}

}

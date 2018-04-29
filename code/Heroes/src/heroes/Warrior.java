package heroes;

import java.awt.Graphics;

public class Warrior extends Hero{
	private int WARRIOR_DRAW_RADIUS = 30;

	public Warrior(PlayerID player_id) {
		super(HeroType.WARRIOR, player_id);
		// TODO Auto-generated constructor stub
	}

	
	private void drawCenteredCircle(Graphics g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillOval(x,y,r,r);
		}
	
	@Override
	void draw(Graphics g, int off_x, int off_y) {
		drawCenteredCircle(g,off_x+GUI.FIELD_WIDTH/2, off_y+GUI.FIELD_HEIGHT/2, 
				WARRIOR_DRAW_RADIUS);
		draw_eq(g, off_x, off_y);
	}

}

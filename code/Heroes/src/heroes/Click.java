package heroes;

import java.io.Serializable;

import heroes.Hero.PlayerID;

public class Click implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x=-1;
	public int y=-1;
	public PlayerID sent_by;
	
	public Click(int x, int y, PlayerID sent_by){
		this.x=x;
		this.y=y;
		this.sent_by=sent_by;
	}
}

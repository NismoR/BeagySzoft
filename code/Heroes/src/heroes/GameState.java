package heroes;

import java.io.Serializable;

public class GameState implements Serializable{

	public enum GameTurn {
		NOT_STARTED,
		INITING_MAP,
		PLAYER_CLIENT,
		PLAYER_SERVER
	}
	
	public enum FieldType {
		NOT_AVAILABLE,
		FREE,
		ATTACKABLE,
		CURRENT
	}
	
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public GameTurn turn;
	public boolean[][] steppable;
	public FieldType[][] board_bg;
	
	public GameState(){
		time = 0;
		turn = GameTurn.NOT_STARTED;
		board_bg = new FieldType[board_size][board_size];
		steppable = new boolean[board_size][board_size];
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = FieldType.NOT_AVAILABLE;
				steppable[i][j] = false;
			}
		}
	}
	
	public void copy(GameState gs){
		time = gs.time;
		turn = gs.turn;
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = gs.board_bg[i][j];
				steppable[i][j] = gs.steppable[i][j];
			}
		}
	}
}

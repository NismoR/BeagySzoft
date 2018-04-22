package heroes;

import java.io.Serializable;

public class GameState implements Serializable{

	public enum GameTurn {
		NOT_STARTED,
		PLAYER_CLIENT,
		PLAYER_SERVER
	}
	
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public GameTurn turn;
	public int[][] board_bg;
	
	public GameState(){
		time = 0;
		turn = GameTurn.NOT_STARTED;
		board_bg = new int[board_size][board_size];
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = 0;
			}
		}
	}
	
	public void copy(GameState gs){
		time = gs.time;
		turn = gs.turn;
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = gs.board_bg[i][j];
			}
		}
	}
}

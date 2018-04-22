package heroes;

import java.io.Serializable;

public class GameState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public int turn;
	public int[][] board_bg;
	
	public GameState(){
		time = 0;
		turn = 0;
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

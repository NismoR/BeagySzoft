package heroes;

import java.io.Serializable;
import java.util.Random;

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
	
	private static float perc_if_valid_field = 0.9f;
	
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
	
	public void init_map(){
		Random r = new Random();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if(r.nextFloat() < perc_if_valid_field){
					board_bg[i][j] = FieldType.FREE;
					steppable[i][j] = true;					
				}
				else{
					board_bg[i][j] = FieldType.NOT_AVAILABLE;
					steppable[i][j] = false;					
				}
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heroes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author ABence
 */
public class GUI extends JFrame implements IGameState, MouseListener{

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	private final int WINDOW_WIDTH = 950, WINDOW_HEIGHT = 650;
	public static int FIELD_SIZE = 60;
	public static int TABLE_OFFSET_X = 30, TABLE_OFFSET_Y = 30;
	private final int TABLE_SIZE_X = 8, TABLE_SIZE_Y = 8;
	private final int STROKE_WIDTH = 3;
	private JPanel p;
	private int arr_background[][] = null;
	private int arr_char[][] = null;
	private boolean TESTING = true;
	
	private GameState gui_gs;
	
	private int ARCHER_DRAW_MARGIN = 15;
	private int MAGE_DRAW_MARGIN = 30;

	private static Color col_bg = new Color(0xF5F2DC);
	private static Color col_field_bg = new Color(0x7A797A);
	private static Color col_current_hero_mark= new Color(0xF0CA4D);
	private static Color col_hero_client = new Color(0xFF5729);	
	private static Color col_hero_server = new Color(0x009494);
	
	private GamePanel gamePanel;

	GUI(Control c) {
		super("Heroes");
		this.addMouseListener(this);
		arr_background = new int[TABLE_SIZE_X][TABLE_SIZE_Y];
		arr_char = new int[TABLE_SIZE_X][TABLE_SIZE_Y];
		
		if(TESTING){
			Random r = new Random();
			for (int i = 0; i < TABLE_SIZE_X; i++) {
				for (int j = 0; j < TABLE_SIZE_Y; j++) {
					arr_background[i][j]=r.nextInt(4);
					arr_char[i][j]=r.nextInt(4+3)-3;
				}
			}
		}
		
		
		ctrl = c;
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(null);
		gui_gs = new GameState();

		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Start");

		JMenuItem menuItem = new JMenuItem("Client");
		menuItem.addActionListener(new MenuListener());
		menu.add(menuItem);

		menuItem = new JMenuItem("Server");
		menuItem.addActionListener(new MenuListener());
		menu.add(menuItem);

		menuBar.add(menu);

		menuItem = new JMenuItem("Clear");
		menuItem.addActionListener(new MenuListener());
		menuBar.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new MenuListener());
		menuBar.add(menuItem);

		setJMenuBar(menuBar);
		setVisible(true);

		gamePanel = new GamePanel();
		gamePanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		//gamePanel.setBorder(BorderFactory.createTitledBorder("Game"));
		add(gamePanel);
	}
	
	private void drawCenteredSquare(Graphics g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillRect(x,y,r,r);
		}
	
	private void draw_archer(Graphics g, int off_x, int off_y){
		Polygon p = new Polygon();		
		p.addPoint(off_x+FIELD_SIZE/2, off_y+ARCHER_DRAW_MARGIN);
		p.addPoint(off_x+ARCHER_DRAW_MARGIN, off_y+FIELD_SIZE-ARCHER_DRAW_MARGIN);
		p.addPoint(off_x+FIELD_SIZE-ARCHER_DRAW_MARGIN, off_y+FIELD_SIZE-ARCHER_DRAW_MARGIN);	    
	    g.fillPolygon(p);
	}
	
	private void draw_mage(Graphics g, int off_x, int off_y){
		drawCenteredSquare(g,off_x+FIELD_SIZE/2, off_y+FIELD_SIZE/2, 
				MAGE_DRAW_MARGIN);
	}

	

	private class MenuListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Clear")) {
			}
			if (e.getActionCommand().equals("Exit")) {
				System.exit(0);
			}
			if (e.getActionCommand().equals("Server")) {
				ctrl.startServer();
			}
			if (e.getActionCommand().equals("Client")) {
				ctrl.startClient();
			}
		}
	}
	
	
	
	private class GamePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private void set_color_according_to_bg(Graphics g, int bg_id){
			switch (bg_id) {
			case 0:
				g.setColor(Color.magenta);
				break;
			case 1:
				g.setColor(Color.cyan);
				break;
			case 2:
				g.setColor(Color.orange);
				break;
			case 3:
				g.setColor(Color.green);
				break;
			default:
				break;
			}
		}
		

		private void set_color_according_to_char(Graphics g, int char_id){
			if(char_id>0){
				g.setColor(Color.red);			
			}
			else{
				if(char_id<0){
					g.setColor(Color.blue);			
				}
				else{
					g.setColor(Color.black);				
				}
			}
		}
		
		private void draw_one_square(Graphics g, int i, int j){
			int off_x = TABLE_OFFSET_X + i*FIELD_SIZE;
			int off_y = TABLE_OFFSET_Y + j*FIELD_SIZE;
			

			// Draw border of square
			g.setColor(Color.black);
			g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);
			// Draw background of square
			set_color_according_to_bg(g,arr_background[i][j]);
			g.fillRect(off_x+STROKE_WIDTH, off_y+STROKE_WIDTH,
					FIELD_SIZE-2*STROKE_WIDTH, FIELD_SIZE-2*STROKE_WIDTH);
			//Draw player
			int char_id = arr_char[i][j];
			set_color_according_to_char(g,char_id);
			//draw_character(g, off_x, off_y,Math.abs(char_id));
		}
		
		private void draw_steppables(Graphics g) {
			for (int i = 0; i < TABLE_SIZE_X; i++) {
				for (int j = 0; j < TABLE_SIZE_Y; j++) {
					int off_x = TABLE_OFFSET_X + i*FIELD_SIZE;
					int off_y = TABLE_OFFSET_Y + j*FIELD_SIZE;
					if(gui_gs.valid_field[i][j]){
						switch (gui_gs.board_bg[i][j]) {
						case START_CLIENT:
							g.setColor(Color.blue);							
							break;
						case START_SERVER:
							g.setColor(Color.red);							
							break;
						case OCCUPIED_C:
						case OCCUPIED_S:	
							g.setColor(col_field_bg.darker());							
							break;
						case STEPABLE:
							g.setColor(Color.blue.darker());							
							break;
						case ATTACKABLE:
							g.setColor(Color.red);							
							break;

						default:
							g.setColor(col_field_bg);
							break;
						}
						g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);						
					}
				}
			}
		}
		
		private void draw_small_triangle(Graphics g, int x, int y, int size, int rot_clckwise_90){
			Polygon p = new Polygon();
			switch (rot_clckwise_90) {
			case 0:
				p.addPoint(x+size, y);
				p.addPoint(x, y+size);
				p.addPoint(x, y);    
			    g.fillPolygon(p);
				break;
			case 1:
				p.addPoint(x-size, y);
				p.addPoint(x, y+size);
				p.addPoint(x, y);    
			    g.fillPolygon(p);
				break;
			case 2:
				p.addPoint(x-size, y);
				p.addPoint(x, y-size);
				p.addPoint(x, y);    
			    g.fillPolygon(p);
				break;
			case 3:
				p.addPoint(x+size, y);
				p.addPoint(x, y-size);
				p.addPoint(x, y);    
			    g.fillPolygon(p);
				break;

			default:
				break;
			}
			
		}
		
		protected void draw_defense(Graphics g, Hero h){
			int off_x = TABLE_OFFSET_X + h.get_x()*FIELD_SIZE;
			int off_y = TABLE_OFFSET_Y + h.get_y()*FIELD_SIZE;			
			int size = GUI.FIELD_SIZE/4;
			switch (h.get_current_defense()) {
			case 2:
				draw_small_triangle(g, off_x+GUI.FIELD_SIZE, off_y, size, 1);
				draw_small_triangle(g, off_x, off_y+GUI.FIELD_SIZE, size, 3);
				/*FALLTHRU*/
			case 1:	
				draw_small_triangle(g, off_x, off_y, size, 0);
				draw_small_triangle(g, off_x+GUI.FIELD_SIZE, off_y+GUI.FIELD_SIZE, size, 2);
				break;
			default:
				break;
			}
		}
		
		private void draw_heroes(Graphics g){
			for(Hero h : gui_gs.heroes){
				int off_x = TABLE_OFFSET_X + h.get_x()*FIELD_SIZE;
				int off_y = TABLE_OFFSET_Y + h.get_y()*FIELD_SIZE;
				switch (h.get_player_id()) {
				case CLIENT:
					g.setColor(col_hero_client);					
					break;
				case SERVER:
					g.setColor(col_hero_server);					
					break;

				default:
					g.setColor(Color.black);
					break;
				}
				draw_defense(g,h);
				h.draw(g, off_x,  off_y);
			}
		}
		
		private void draw_current_hero_mark(Graphics g){
			Hero h = gui_gs.get_current_hero();
			int off_x = TABLE_OFFSET_X + h.get_x()*FIELD_SIZE;
			int off_y = TABLE_OFFSET_Y + h.get_y()*FIELD_SIZE;
			g.setColor(col_current_hero_mark);
			g.drawRect(off_x, off_y, FIELD_SIZE-1, FIELD_SIZE-1);
			g.drawRect(off_x+1, off_y+1, FIELD_SIZE-3, FIELD_SIZE-3);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(col_bg);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 24));
			/*g.fillRect(TABLE_OFFSET_X-STROKE_WIDTH, TABLE_OFFSET_Y-STROKE_WIDTH, 
					TABLE_SIZE_X*FIELD_WIDTH+2*STROKE_WIDTH, 
					TABLE_SIZE_X*FIELD_HEIGHT+2*STROKE_WIDTH);*/
			draw_steppables(g);
			draw_heroes(g);
			draw_current_hero_mark(g);
			/*for (int i = 0; i < TABLE_SIZE_X; i++) {
				for (int j = 0; j < TABLE_SIZE_Y; j++) {
					draw_one_square(g,i,j);
				}
			}*/
		}
	}



	@Override
	public void onNewGameState(GameState gs) {
		// TODO Auto-generated method stub
		gui_gs.copy(gs);
		gamePanel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {	

		int x = (e.getX() - TABLE_OFFSET_X - 10 + FIELD_SIZE) / FIELD_SIZE -1;
		if(x <0 || x>= TABLE_SIZE_X){
			return;
		}
		int y = (e.getY() - TABLE_OFFSET_Y - 54 + FIELD_SIZE) / FIELD_SIZE -1;	
		if(y <0 || y>= TABLE_SIZE_Y){
			return;
		}
		//System.out.println("X:" + x + " Y:" + y+ "    e-X:" + e.getX() + " Y:" + e.getY());
		

		ctrl.onNewClick(x, y);
	}
}

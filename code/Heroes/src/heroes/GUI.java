/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heroes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
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
	private int WINDOW_WIDTH = 950, WINDOW_HEIGHT = 650;
	public static int FIELD_SIZE = 60;
	public static int TABLE_OFFSET_X = 30, TABLE_OFFSET_Y = 30;
	private int TABLE_SIZE_X = 8, TABLE_SIZE_Y = 8;
	
	private GameState gui_gs;

	private static Color col_bg = new Color(0xF5F2DC);
	private static Color col_field_bg = new Color(0x7A797A);
	private static Color col_current_hero_mark= new Color(0xF0CA4D);
	private static Color col_hero_client = new Color(0xFF5729);	
	private static Color col_hero_server = new Color(0x009494);
	private static Color col_start_client = new Color(0xFF5729);	
	private static Color col_start_server = new Color(0x009494);
	
	public static int hero_death_decr = 11;
	
	private GamePanel gamePanel;

	GUI(Control c) {
		super("Heroes");
		this.addMouseListener(this);
		
		
		
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
	
	private Color get_col_with_alpha(Color col, int alpha){
		return new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha);
	}
	
	private int get_x_offset(int x){
		return TABLE_OFFSET_X + x*FIELD_SIZE;
	}
	
	private int get_y_offset(int y){
		return TABLE_OFFSET_Y + y*FIELD_SIZE;
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
		
		private void draw_valid_fields(Graphics g) {
			for (int i = 0; i < TABLE_SIZE_X; i++) {
				for (int j = 0; j < TABLE_SIZE_Y; j++) {
					if(gui_gs.valid_field[i][j]){
						int off_x = get_x_offset(i);
						int off_y = get_y_offset(j);
						g.setColor(col_field_bg);
						g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);	
					}					
				}
			}			
		}

		private void draw_starting_positions(Graphics g) {
			List<Click> start_pos=gui_gs.start_pos;
			if(start_pos!=null){
				for(Click c:start_pos){
					int off_x = get_x_offset(c.x);
					int off_y = get_y_offset(c.y);
					switch (c.playerID) {
					case CLIENT:
						g.setColor(col_start_client);
						break;
					case SERVER:
						g.setColor(col_start_server);
						break;
					default:
						g.setColor(Color.black);
						break;
					}
					g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);
				}
			}
		}

		private void draw_steppable(Graphics g) {
			Click st = gui_gs.wanna_step;
			if(st!=null){
				int off_x = get_x_offset(st.x);
				int off_y = get_y_offset(st.y);
				g.setColor(Color.blue.darker());
				g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);					
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
			int off_x = get_x_offset(h.get_x());
			int off_y = get_y_offset(h.get_y());			
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
				if(h.get_health() % 6 >2){
					int off_x = get_x_offset(h.get_x());
					int off_y = get_y_offset(h.get_y());
					g.setColor(col_field_bg.darker());	
					if(h.get_attackable()){
						g.setColor(Color.red);	
					}
					if(gui_gs.wanna_step!=null){
						if(h.get_x()==gui_gs.wanna_step.x){
							if(h.get_y()==gui_gs.wanna_step.y){
								g.setColor(Color.blue.darker());								
							}
						}
					}
					g.fillRect(off_x, off_y, FIELD_SIZE, FIELD_SIZE);
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
		}
		
		private void draw_current_hero_mark(Graphics g){
			Hero h = gui_gs.get_current_hero();
			int off_x = get_x_offset(h.get_x());
			int off_y = get_y_offset(h.get_y());
			g.setColor(col_current_hero_mark);
			g.drawRect(off_x, off_y, FIELD_SIZE-1, FIELD_SIZE-1);
			g.drawRect(off_x+1, off_y+1, FIELD_SIZE-3, FIELD_SIZE-3);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			Instant start = Instant.now();
			long startTime = System.nanoTime();
			g.setColor(col_bg);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 24));
			draw_valid_fields(g);
			draw_starting_positions(g);
			draw_steppable(g);
			draw_heroes(g);
			draw_current_hero_mark(g);
			if(gui_gs.should_step_again){
				g.setColor(Color.black);
				g.fillRect(get_x_offset(8), get_y_offset(8), 3*FIELD_SIZE, 3*FIELD_SIZE);				
			}
			Instant end = Instant.now();
			Duration diff = Duration.between(start, end);
			long diff_in_nano = System.nanoTime() - startTime;
			if(diff.toMillis()>10){
				//System.out.println("Time taken: "+ diff.toMillis() +" milliseconds");
			}
			else{
				//System.out.println("Time taken: "+ diff_in_nano +" nanoseconds");				
			}
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
		
		ctrl.onNewClick(new Click(x, y, gui_gs.get_current_hero().get_player_id()));
	}
}

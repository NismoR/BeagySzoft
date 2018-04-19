/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heroes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class GUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	private final int WINDOW_WIDTH = 950, WINDOW_HEIGHT = 650;
	private final int FIELD_WIDTH = 60, FIELD_HEIGHT = 60;
	private final int TABLE_OFFSET_X = 30, TABLE_OFFSET_Y = 30;
	private final int TABLE_SIZE_X = 8, TABLE_SIZE_Y = 8;
	private final int STROKE_WIDTH = 3;
	private JFrame j;
	private int arr_background[][] = null;
	private int arr_char[][] = null;
	private boolean TESTING = true;

	GUI(Control c) {
		arr_background = new int[TABLE_SIZE_X][TABLE_SIZE_Y];
		arr_char = new int[TABLE_SIZE_X][TABLE_SIZE_Y];
		
		if(TESTING){
			Random r = new Random();
			for (int i = 0; i < TABLE_SIZE_X; i++) {
				for (int j = 0; j < TABLE_SIZE_Y; j++) {
					arr_background[i][j]=r.nextInt(4);
					arr_char[i][j]=r.nextInt(3);
				}
			}
		}
		
		
		j = new JFrame("Heroes");
		ctrl = c;
		j.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		j.setLayout(null);

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

		j.setJMenuBar(menuBar);
		j.setContentPane(this);
		j.setVisible(true);
		repaint();
	}

	public JFrame getJ() {
		return j;
	}
	
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
			g.setColor(Color.red);
			break;
		default:
			break;
		}
	}
	
	private void draw_one_square(Graphics g, int i, int j){
		int off_x = TABLE_OFFSET_X + i*FIELD_WIDTH;
		int off_y = TABLE_OFFSET_Y + j*FIELD_HEIGHT;
		

		// Draw border of square
		g.setColor(Color.black);
		g.fillRect(off_x, off_y, FIELD_WIDTH, FIELD_HEIGHT);
		// Draw background of square
		set_color_according_to_bg(g,arr_background[i][j]);
		g.fillRect(off_x+STROKE_WIDTH, off_y+STROKE_WIDTH,
				FIELD_WIDTH-2*STROKE_WIDTH, FIELD_HEIGHT-2*STROKE_WIDTH);

	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.BOLD, 24));
		g.fillRect(TABLE_OFFSET_X-STROKE_WIDTH, TABLE_OFFSET_Y-STROKE_WIDTH, 
				TABLE_SIZE_X*FIELD_WIDTH+2*STROKE_WIDTH, 
				TABLE_SIZE_X*FIELD_HEIGHT+2*STROKE_WIDTH);
		for (int i = 0; i < TABLE_SIZE_X; i++) {
			for (int j = 0; j < TABLE_SIZE_Y; j++) {
				draw_one_square(g,i,j);
			}
		}
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
}

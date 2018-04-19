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
	private final int FIELD_WIDTH = 70, FIELD_HEIGHT = 70;
	private final int TABLE_OFFSET_X = 50, TABLE_OFFSET_Y = 50;
	private final int TABLE_SIZE_X = 8, TABLE_SIZE_Y = 8;
	private JFrame j;

	GUI(Control c) {
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

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, 950, 650);
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.BOLD, 24));
		g.setColor(Color.blue);
		g.fillRect(50, 50, 560, 510);
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

package heroes;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import heroes.GUI;
/**
 * Absztrakt osztály a hálózatot kezelõ osztályok számára.
 * @author Misi
 *
 */
abstract class Network {
	/**
	 * 
	 * A hálozatti interfész  <code> start </code> függvény meghívásától <code> stop </code> 
	 * függvény meghívásáig folyamatosan probál csatlakozni a mások hálozati elemhez
	 * 
	 * @param ip A cél IP cím.
	 */
	abstract void start(String ip);
	/**
	 * A hálozatti interfész  <code> start </code> függvény meghívásától <code> stop </code> 
	 * függvény meghívásáig folyamatosan probál csatlakozni a mások hálozati elemhez
	 */
	abstract void stop();	
	
	/**
	 * A <code> stop </code> függvény meghívásáig blokkolja a konstruktorban megadott ablakot. A szerver és a
	 * kliens guiját blokolja a csatlakozás ideje alatt
	 * 
	 * @author Misi
	 *
	 */
	protected class WinBlocker implements Runnable{
		private JDialog d;
		
		/**
		 * A létrejövõ dialogus ablak a paraméterben megadott GUI objektumot blokolja,
		 *  ezzenfelül tartalmaze egy gombot a program leállítására.
		 * 
		 * @param g
		 */
		public WinBlocker(GUI g) {
			
			JPanel pan = new JPanel(new GridLayout(2, 1));

			JButton button = new JButton("Finish");
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					stop();
					System.out.println("Program is closed by the user.");
					System.exit(-1);
				}
			});
			
		//	pan.add(label);
			pan.add(button);

			d = new JDialog(g, "Waiting", Dialog.ModalityType.DOCUMENT_MODAL);
			d.add(pan);
			d.pack();
			d.setLocationRelativeTo(g);
			Thread blocker = new Thread(this);
			blocker.start();			
		}
		public void run() {
			d.setVisible(true);
			System.out.println("Ready");
		}
		
		/**
		 * dialogus ablakok bezárására szolgáló függvény.
		 */
		public void stop() {
			d.dispose();
			 d.setVisible(false);
             d.dispatchEvent(new WindowEvent(d, WindowEvent.WINDOW_CLOSING));
		}
	}
	
}

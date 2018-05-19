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
 * Absztrakt oszt�ly a h�l�zatot kezel� oszt�lyok sz�m�ra.
 * @author Misi
 *
 */
abstract class Network {
	/**
	 * 
	 * A h�lozatti interf�sz  <code> start </code> f�ggv�ny megh�v�s�t�l <code> stop </code> 
	 * f�ggv�ny megh�v�s�ig folyamatosan prob�l csatlakozni a m�sok h�lozati elemhez
	 * 
	 * @param ip A c�l IP c�m.
	 */
	abstract void start(String ip);
	/**
	 * A h�lozatti interf�sz  <code> start </code> f�ggv�ny megh�v�s�t�l <code> stop </code> 
	 * f�ggv�ny megh�v�s�ig folyamatosan prob�l csatlakozni a m�sok h�lozati elemhez
	 */
	abstract void stop();	
	
	/**
	 * A <code> stop </code> f�ggv�ny megh�v�s�ig blokkolja a konstruktorban megadott ablakot. A szerver �s a
	 * kliens guij�t blokolja a csatlakoz�s ideje alatt
	 * 
	 * @author Misi
	 *
	 */
	protected class WinBlocker implements Runnable{
		private JDialog d;
		
		/**
		 * A l�trej�v� dialogus ablak a param�terben megadott GUI objektumot blokolja,
		 *  ezzenfel�l tartalmaze egy gombot a program le�ll�t�s�ra.
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
		 * dialogus ablakok bez�r�s�ra szolg�l� f�ggv�ny.
		 */
		public void stop() {
			d.dispose();
			 d.setVisible(false);
             d.dispatchEvent(new WindowEvent(d, WindowEvent.WINDOW_CLOSING));
		}
	}
	
}

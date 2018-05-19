package heroes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

/**
 * Szerver hálózati interfész.
 * Network osztályból öröklödik, emelett implementálja az IGameState interfészt.
 * Egyszerre képes gamestatek fogadására és küldésére.
 * 
 * A szervert létrehozza egy külön szálban. Server soketet nyit a megadott porton,
 *  majd várakozik a kliens csatlakozásához. Abban az esetben ha megszakadna a kapcsolat
 *  ujra várakozó állapotba kerül és eközben a serveroldali GUI-t blokolja a <code> Network GUI_Blocker</code>
 *  osztálya segítségével.A kapcsolat felépítése után fogadja a beérkezõ 
 *  adatokat, amit Click objektumokká alakít,és továbküldi az IClick interfészen keresztül a Control felé.
 *  A játékállapotokat (<code> GameState </code>) objektumokat az IGameState interfészen keresztül
 *  küldi tovább a kliensnek.
 *  
 * 
 * @author Misi
 * 
 */
public class Server extends Network implements IGameState{
	
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	/**
	 * A kliens Server közötti kapcsolat megszakítását jelzõ flag, igaz értékre állítás esetén
	 *  megszakítja a kapcsolatott és leállítja a szálat.
	 */
	private boolean exit_flag;
	private ObjectOutputStream out = null;
	/**
	 * párhuzamos hozáférések elleni védelemmet valosít meg.
	 */
	private ReentrantLock lock = null;
	
	private ObjectInputStream in = null;
	
	private Control clickInterface;
	private GUI gui;
	/**
	 * A szervert egy külön szálba helyezõ , és ott mülködtetõ <code> Runnable </code> objektum.
	 */
	private ListenerWorker worker;
	/**
	 * A worker-t futtató szál.
	 */
	private Thread thread;
	
	/**
	 * Konstruktor
	 * @param ci A szerver oldali Control.
	 * @param g A szerver oldali GUI. (A csatlakozás közbeni blokkolás miatt szükséges.)
	 */
	Server(Control ci, GUI g)
	{
		worker = new ListenerWorker();
		clickInterface = ci;
		this.gui = g;
		lock = new ReentrantLock();
	}
	/**
	 * Strimek bezárása
	 */
	 private void cleanup()
	{
		 lock.lock();
			try{
				if (out != null){
					out.close();
					out = null;
				}
			} catch (IOException ex){
				System.err.println("Error while closing out.");
			}
			try{
				if (in != null){
					in.close();
					in = null;
				}
			} catch (IOException ex){
				System.err.println("Error while closing in.");
			}
			try{
				if (clientSocket != null){
					clientSocket.close();
					clientSocket = null;
				}
			} catch (IOException ex){
				System.err.println("Error while closing socket.");
			}
			lock.unlock();
	}
	
	 /**
	  * Szerver indítás. Meglévõ kapcsolatok bezárrása, majd worker szál készítése.
	  *	A szerver létrehozza egy külön szálban. Végtelen ciklusban várakozik a kliensre.
	  *  Miután felépíteték a kapcsolatott, a bejövõ adatokat fogadja, Az esetleges szétkapcsolások után újra
	  *  visszatér a végtelen ciklusban és várakozik egy esetleges csatlakozásra
	  */
	 @Override
	void start(String ip) {
		stop();
		exit_flag=false;
		thread = new Thread(worker);
		thread.start();
	}
	
	/**
	 * Szerver leállítás.
	 * <code> exit_flag=true</code> beállításával illetve a szerver kliens socket bezárásával
	 * egy kivétel dobást generál, ami segítségével kilép a várakozó állapotból.
	 * Végül megsemísiti a worker szálat.
	 */
	@Override
	void stop() {
		lock.lock();
		exit_flag=true;
		try{
			if(serverSocket != null){
				serverSocket.close();
				serverSocket = null;
			}
		} catch (Exception ex){
			System.out.println("Cannot close server socket");
		}
		try{
			if(clientSocket != null){
				clientSocket.close();
				clientSocket = null;
			}
		} catch (Exception ex) {
			System.out.println("Cannot close client socket");
		}
		lock.unlock();
		
		
		try {
			if(thread!=null)
				thread.join();
		} catch (InterruptedException e) {
			System.out.println("Cannot stop the worker thread.");
		}
	}
	
	/**
	 *	A szerver létrehozza egy külön szálban. Végtelen ciklusban várakozik a kliensre.
	 *  Miután felépíteték a kapcsolatott, a bejövõ adatokat fogadja, Az esetleges szétkapcsolások után újra
	 *  visszatér a végtelen ciklusban és várakozik egy esetleges csatlakozásra
	 *
	 *@author Misi
	 */
        private class ListenerWorker implements Runnable {
		public void run() {
			// Create server socket
			ServerSocket ss = null;
			while(ss==null)
				try{
					ss =  new ServerSocket(10007);
				}catch(IOException ex){
					System.out.println("Failed to create server socket");
					JOptionPane.showMessageDialog(gui, "Cannot create server socket on port 10007.","Fatal error",JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
			lock.lock();
			serverSocket = ss;
			lock.unlock();

			// Start listening
			while(true)
			{
				boolean f = true;
				Socket cs=null;
				ObjectOutputStream os=null;
				// Waiting for clients
				while(cs==null)
				{

					lock.lock();
					f = exit_flag;
					lock.unlock();
					if(f==true) return;
					
					WinBlocker blocker = null;
					try{
						// Block main window
						blocker = new WinBlocker(gui);
						cs = serverSocket.accept();
						// Release blocking
						blocker.stop();
						try{
							os = new ObjectOutputStream(cs.getOutputStream());
							in = new ObjectInputStream(cs.getInputStream());
							os.flush();
						} catch(IOException ex){
							cs.close();
							cs = null;
						}
					} catch (Exception ex) {
						if(blocker != null) blocker.stop();
					}
				}

				// share objects
				lock.lock();
				try{
					f = exit_flag;
					if(f){
						os.close();
						in.close();
						cs.close();
					} else {
						clientSocket = cs;
						out = os;
					}
				}catch (IOException ex){
					System.out.println("Close error");
				} finally{
				lock.unlock();
				}
				if(f) return;
				
				// CONNECTION ESTABLISHED
				System.out.println("Client connected.");
				clickInterface.startScheduler();
				// COMMUNICATING	
				try {
					while (true) {
						Click c = (Click) in.readObject();
						if(c instanceof Click)
							clickInterface.onNewClick(c);
					}
				} catch (Exception ex) {
					System.out.println("Disconnected");
				} finally {
					cleanup();
				}
			} // while
		}//run
	}// worker
	
        /**
         * A server által küldött játékállapotok küldését valósítja meg a kliens felé.
         */
	public void onNewGameState(GameState gs)
	{
		lock.lock();
		try {
			if(out != null)
			{
				out.reset();
				out.writeObject(gs);
				out.flush();
				
			}
		} catch (IOException ex) {
			System.err.println("Send error.");
		} finally {
			lock.unlock();
			
		}
	}
}

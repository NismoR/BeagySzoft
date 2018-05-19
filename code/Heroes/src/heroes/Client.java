package heroes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Kliens hálózati interfész.
 * Network osztályból öröklödik, emelett implementálja az IClick interfészt.
 * Egyszerre képes Click objektunok fogadására és küldésére.
 *  A klienst létrehozzása egy külön szálban. A kliens végtelen ciklusban probál csatlakozni a szerverhez.
 *  Miután felépíteték a kapcsolatott, a bejövõ adatokból GameState objektumot generál, majd 
 *  továbbítja a kliens GUI felé. Csatlakozás alatt a GUI_Blockerrel blokolja a kliens GUI-t.
 *  leállási feltételmaz exit_flage igaz értéke
 *
 * @author Misi
 */
public class Client extends Network implements IClick{
	
	private Socket socket = null;
	/**
	 * A kliens Server közötti kapcsolat megszakítását jelzõ flag, igaz értékre állítás esetén
	 * megszakítja a kapcsolatott és leállítja a szálat.
	 */
	private boolean exit_flag = false;
	private ObjectOutputStream out = null;
	/**
	 * párhuzamos hozáférések elleni védelemmet valosít meg..
	 */
	private ReentrantLock lock = null;
	
	private ObjectInputStream in = null;
	/**
	 * Szerver ip címe
	 */
	private String ip = "localhost";//bear
	/**
	 * Kliens oldali GUI.
	 */
	private GUI gui;

	/**
	 *  A klienst egy külön szálba helyezõ , és ott mülködtetõ <code> Runnable </code> objektum.
	 */
	private ListenerWorker worker;
	/**
	 * A worker-t futtató szál.
	 */
	private Thread thread;
	
	/**
	 * Konstruktor
	 * @param gui Kliens oldali GUI.
	 */
	Client(GUI gui)
	{
		//gsInterface = g;
		this.gui = gui;
		gui.setClick(this);
		worker = new ListenerWorker();
		lock = new ReentrantLock();
	}

	/**
	 * Socketek, strimek felszabadítása
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
			if (socket != null){
				socket.close();
				socket = null;
			}
		} catch (IOException ex){
			System.err.println("Error while closing socket.");
		}
		lock.unlock();
	}
	/** 
	 *  Client indítás. Meglévõ kapcsolatok bezárrása, majd worker szál készítése.
	 *  A klienst létrehozza egy külön szálban. Végtelen ciklusban probál csatlakozni a szerverhez.
	 *  Miután felépíteték a kapcsolatott, a bejövõ adatokból GameState objektumot generál, majd 
	 *  továbbítja a kliens GUI felé. Csatlakozás alatt a GUI_Blockerrel blokolja a kliens GUI-t.
	 *  leállási feltételmaz exit_flage igaz értéke
	 */
	public void start(String ip)
	{
		this.ip = ip;
		stop();
		exit_flag=false;
		thread = new Thread(worker);
		thread.start();
	}
	/**
	 * Client leállítás.
	 * <code> exit_flag=true</code> beállításával illetve a szerver kliens socket bezárásával
	 * egy kivétel dobást generál, ami segítségével kilép a várakozó állapotból.
	 * Végül megsemísiti a worker szálat.
	 */
	public void stop()
	{
		lock.lock();
		try{
			exit_flag = true;
			if(socket != null)
				socket.close();
		} catch(IOException ex){
			System.out.println("Cannot close socket");
		}finally{
			lock.unlock();
		}
		try{
		if(thread!=null)
		{
			thread.join();
			thread=null;
		}
		} catch(InterruptedException ie){
			System.out.println("Join interrupted");
		}

	}
	/**
	 *  A klienst létrehozza egy külön szálban. Végtelen ciklusban probál csatlakozni a szerverhez.
	 *  Miután felépíteték a kapcsolatott, a bejövõ adatokból GameState objektumot generál, majd 
	 *  továbbítja a kliens GUI felé. Csatlakozás alatt a GUI_Blockerrel blokolja a kliens GUI-t.
	 *  leállási feltételmaz exit_flage igaz értéke
	 *   
	 * @author Misi
	 *
	 */
	private class ListenerWorker implements Runnable {
		public void run() {
			while(true){
				boolean f= true;
				Socket s = null;
				ObjectOutputStream o=null;
				// Connecting
				
				lock.lock();
				f = exit_flag;
				lock.unlock();
				if(f) {
					return;
				}
				
				WinBlocker blocker = null;
				// Block main window
				blocker = new WinBlocker(gui);
				while(s == null)
				{

					try{
						lock.lock();
						f = exit_flag;
						lock.unlock();
						if(f) {
							blocker.stop();
							return;
						}
						
						s = new Socket(ip,10007);
						try{
							o = new ObjectOutputStream(s.getOutputStream());
							o.flush();
							in = new ObjectInputStream(s.getInputStream());
						} catch(IOException ex){
							s.close();
							s = null;
						}
					} catch (UnknownHostException he){
						System.out.println("Cannot reach host");
					} catch (IOException ie) {
						System.out.println("Connection error");
					} 
				}
				if(blocker!=null) blocker.stop();
				// share objects
				lock.lock();
				try{
					f = exit_flag;
					if(f){
						o.close();
						in.close();
						s.close();
					} else {
						socket = s;
						out = o;
					}
				}catch (IOException ex){
					System.out.println("Close error");
				} finally{
				lock.unlock();
				}
				if(f) return;
	
				// CONNECTION ESTABLISHED
				System.out.println("Connected to server.");

				try {
					while (true) {
						GameState gs = (GameState) in.readObject();
						if(gs instanceof GameState)
							gui.onNewGameState(gs);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Disconnected");
				} finally {
					cleanup();
				}
			}// while
		} // run
	} //
	
	/**
	 * Command objektumok küldésére szolgál a szerver felé.
	 */
	public void onNewClick(Click c)
	{
		lock.lock();
		try {
			if(out != null){
				out.writeObject(c);
				out.flush();
			}
		} catch (IOException ex) {
			System.out.println("Send error");
		} finally {
			lock.unlock();
		}

	}

}

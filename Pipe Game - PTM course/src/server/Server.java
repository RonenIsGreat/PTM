/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author ronen
 *
 */
public class Server implements IServer{

	//Data Members
	private boolean m_stop;
	private int m_port;
	private IClientHandler m_ClientHandler;
	
	/**
	 * Ctor
	 */
	public Server(int port, IClientHandler clientHandler) {
		m_stop = false;
		m_port = port;
		m_ClientHandler = clientHandler;
	}
	
	// Methods
	
	@Override
	public void start() {
		new Thread(()->runServer()).start();
	}
	
	private void runServer() {
		try {
			ServerSocket server = new ServerSocket(m_port);
			server.setSoTimeout(1000);
			
			while(!m_stop) {
				try {
				Socket aClient = server.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(aClient.getInputStream()));
				PrintWriter outToClient = new PrintWriter(aClient.getOutputStream());
				m_ClientHandler.handleClient(inFromClient, outToClient, "done");
				inFromClient.close();
				outToClient.close();
				aClient.close();
				} catch (SocketTimeoutException e) {
					/*...*/
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void stop() {
		m_stop = true;
	}
	
	
}

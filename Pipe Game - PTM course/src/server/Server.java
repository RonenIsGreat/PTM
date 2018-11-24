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
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ronen
 *
 */
public class Server implements IServer{

	//Data Members
	private boolean m_stop;
	private int m_port;
	private IClientHandler m_ClientHandler;
	BlockingQueue<Runnable> m_ClientPriorityQueue;
	int m_parallelClientsNumber;
	ExecutorService m_threadPool;
	
	/**
	 * Ctor
	 */
	public Server(int port, IClientHandler clientHandler, int parallelClientsNumber) {
		m_stop = false;
		m_port = port;
		m_ClientHandler = clientHandler;
		m_parallelClientsNumber = parallelClientsNumber;
	}
	
	// Methods
	
	@Override
	public void start() {
		m_ClientPriorityQueue = new PriorityBlockingQueue<>();
		m_threadPool = new ThreadPoolExecutor(m_parallelClientsNumber, m_parallelClientsNumber, 0L, TimeUnit.MILLISECONDS, m_ClientPriorityQueue);
		new Thread(()->runServer()).start();
	}
	
	private void runServer() {
		try {
			ServerSocket server = new ServerSocket(m_port);
			server.setSoTimeout(1000);
			
			while(!m_stop) {
				try {
					Socket aClient = server.accept();
					final String doneStr = "done";
					ClientTask newClientTask = new ClientTask(m_ClientHandler, aClient, doneStr);
					m_threadPool.execute(newClientTask);
				} catch (SocketTimeoutException e) {
					/*Socket timeout*/
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
		m_threadPool.shutdown();
	}
	
	
}

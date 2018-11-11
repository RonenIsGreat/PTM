/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author ronen
 *
 */
public class ClientTask implements Runnable, Comparable<ClientTask> {

	private Problem m_problem;
	private Socket m_client;
	private String  m_doneStr;
	private IClientHandler m_ClientHandler;
	private BufferedReader m_inFromClient;
	
	public ClientTask(Problem problem, Socket aClient, String doneStr, IClientHandler ClientHandler) {
		m_problem = problem;
		m_client = aClient;
		m_doneStr = doneStr;
		m_ClientHandler = ClientHandler;
	}
	
	public ClientTask(IClientHandler clientHandler, Socket aClient, String doneStr) throws IOException {
		m_ClientHandler = clientHandler;
		m_client = aClient;
		m_doneStr = doneStr;
		m_inFromClient = new BufferedReader(new InputStreamReader(m_client.getInputStream()));
		m_problem = m_ClientHandler.getClientProblem(m_inFromClient, m_doneStr);
	}
	
	@Override
	public void run() {
		try {
			PrintWriter outToClient = new PrintWriter(m_client.getOutputStream());
			m_ClientHandler.solveClientProblem(m_problem, outToClient, m_doneStr);
			outToClient.close();
			m_inFromClient.close();
			m_client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(ClientTask other) {
		int myBoardSize = this.m_problem.GetProblemStringLines().size();
		int otherBoardSize = other.m_problem.GetProblemStringLines().size();
		return  (myBoardSize - otherBoardSize);
	}

}

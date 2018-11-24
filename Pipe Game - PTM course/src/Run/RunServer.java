package Run;

import server.CacheManager;
import server.ClientHandler;
import server.ICacheManager;
import server.IClientHandler;
import server.IServer;
import server.ISolver;
import server.PipeGameSolver;
import server.Server;

public class RunServer {

	public static void main(String[] args) {
		int port = 6400;
		ICacheManager cacheManager = new CacheManager();
		ISolver solver = new PipeGameSolver();
		IClientHandler clientHandler = new ClientHandler(cacheManager, solver);
		int parallelClientsNumber = 10;
		IServer s=new Server(port, clientHandler, parallelClientsNumber);
		s.start();
	}

}

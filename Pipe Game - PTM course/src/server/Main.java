package server;

public class Main {

	public static void main(String[] args) {
		ICacheManager cacheManager = new CacheManager();
		ISolver solver = new PipeGameSolver();
		int port = 6400;
		IClientHandler clientHandler = new ClientHandler(cacheManager, solver);
		IServer server = new Server(port, clientHandler);
		server.start();
	}

}

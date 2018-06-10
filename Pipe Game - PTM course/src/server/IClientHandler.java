/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * @author ronen
 *
 */
public interface IClientHandler {

	void handleClient(BufferedReader inFromClient, PrintWriter outToClient, String doneStr);
	
}

package server;

import java.io.*;
import java.net.*;

/**
 * Class to create a new server
 * @author tomas
 * 
 */
public class ServerTCP {
	int port;
	
	/**
	 * Class which creates a server in the port = [param port]
	 * @param port - port which the server will be created
	 * 
	 */
	public ServerTCP(int port) {
		this.port = port;
        boolean run = true;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server established on port " + port);
 
            while (run) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                
                new ServerThread(socket).start();
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}


package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import database.Siga;
import server.Handler;

/**
 * Class which has a thread that will receive the sockets with the requests, and send to the handler class.
 * @author tomas
 * 
 */
public class ServerThread extends Thread {
    private Socket socket;
 
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
    	
    	Handler handler = new Handler();
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
 
            String pedido;
 
            do {
                pedido = reader.readLine();
                String retorno = "wasabi";
                System.out.println("recebeu pedido " + pedido);
                if(pedido!=null && !pedido.equals("closeSocket")) {
                	retorno = handler.getResponse(pedido);
                }
                System.out.println();
                writer.println(retorno);
 
            } while (!pedido.equals("closeSocket"));
            System.out.println();System.out.println();
            System.out.println("Beijos e abracos");
            System.out.println();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
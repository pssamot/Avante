package launcher;

import server.*;
import user.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import database.Siga;
import leilao.Auction;
import scrapper.*;

/**
 * Class to launch the server code
 * @author tomas
 * 
 */
public class ServerLauncher {
	
	public static int port = 2000;
	
	/**
	 * Method which starts all the required functions for the server to work
	 * @param args main args
	 */
	public static void main(String[] args) {

		try {
			//Inicia a ligacao -> myConn variavel estatica para ser acedida por todos os objetos
			new Siga().bamboraBino();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateobj = new Date();
			
			System.out.println("Bom dia mestre222 " + df.format(dateobj));System.out.println();System.out.println();
			
			new HtmlParser().importar();
			
			//Handler h = new Handler();
			//String r = h.getResponse("onSearch{luizao}");
			//System.out.println(r);
			
			//Limpar
			//new leilao.AuctionThreadCleaner().start();
			
			//Server
			//new server.ServerTCP(port);
			

			
			//Fecha a ligacao
			new Siga().adeusBino();
		}
		catch (Exception e) {
		}
	}
	
}

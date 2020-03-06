package server;

import chat.*;
import leilao.*;
import news.*;

import java.text.ParseException;

import scrapper.Slug;
import user.*;
/**
 * Class that connects all the requests from the server to application logic and database
 * @author tomas
 * 
 */
public class Handler {

	/**
	 * Method that processes a Request. Parse the request, and retrieve the arguments and the code. 
	 * Then calls the methods from other classes to execute the application logic
	 * @param pedido request with a structure
	 * @return response of the request
	 */
	public String getResponse(String pedido) {
		
		System.out.println("comecar " + pedido);
		String code = getRequestCode(pedido);
		String arguments = getRequestArguments(pedido);
		String retorno = null;
		System.out.println("Codigo " + code + " argumentos : " + arguments);
		
		switch (code) {
			case "getMyContacts":
				retorno = Clube.getContacts(arguments.split(",")[0]);
				break;
			case "onSearch":
				retorno = Auction.onSearch(arguments.split(",")[0]);
				break;
				
			case "postPlayerToClub":
				Slug slug = new Slug();
				Player p = new Player(arguments.split(",")[0],
									slug.makeSlug(arguments.split(",")[0]),
									slug.makeSlug(arguments.split(",")[0]),
									false,
									arguments.split(",")[1],
									arguments.split(",")[2],
									arguments.split(",")[3]);
  			  	retorno = p.save();
			break;
			case "getAllMyChats":
				 retorno = Chat.getAllMyChats(arguments.split(",")[0]);
				break;
			case "postMessage":
				 Message m = new Message(arguments.split(",")[0], arguments.split(",")[1],arguments.split(",")[2]);
				 retorno = m.save();
				break;
			case "createChat":
				 Chat ch = new Chat(arguments.split(",")[0], arguments.split(",")[1]);
				 retorno = ch.save();
				break;
			case "getClub":
				retorno = Clube.getClub(arguments.split(",")[0], arguments.split(",")[1]);
				break;
				
			case "getClubByID":
				retorno = Clube.getClubByID(arguments);
				break;

			case "postClub":
				Clube c = new Clube(arguments.split(",")[0],arguments.split(",")[1],arguments.split(",")[2],false,arguments.split(",")[3],Integer.parseInt(arguments.split(",")[4]));
				retorno = c.save();
				retorno = Clube.getClubByID(retorno);
				break;
				
			case "postPlayerToAuction":
				Auction a = null;
				try {
					a = new Auction(arguments.split(",")[0],
							Integer.parseInt(arguments.split(",")[1]),
							Integer.parseInt(arguments.split(",")[2]),
							-1,
							Integer.parseInt(arguments.split(",")[3]),
							1,
							Integer.parseInt(arguments.split(",")[4]),
							Integer.parseInt(arguments.split(",")[5]),
							Integer.parseInt(arguments.split(",")[6]));
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				retorno = a.save();
				retorno = Auction.getAuctionByID(Integer.parseInt(retorno));
				break;
				
			case "patchConfigurations":				
				retorno = Clube.updateConfigurations(arguments.split(",")[0],arguments.split(",")[1],arguments.split(",")[2]) ;
				break;
				
			case "postBid":
				Bid b = new Bid(Integer.parseInt(arguments.split(",")[0]),Integer.parseInt(arguments.split(",")[1]),Integer.parseInt(arguments.split(",")[2]),arguments.split(",")[3]);
				retorno = b.save();
				retorno = Auction.getAuctionByID(Integer.parseInt(retorno));
				break;
			
			case "postBuyNow":
				Bid buyNow = new Bid(Integer.parseInt(arguments.split(",")[0]),Integer.parseInt(arguments.split(",")[1]),Integer.parseInt(arguments.split(",")[2]),arguments.split(",")[3]);
				retorno = buyNow.buyNow();
				break;
				
			case "getAllAuctions":
				retorno = Auction.getAllAuctions();
				break;
			case "getAllNews":
				retorno = News.getAllNews();
				break;
			default:
				retorno = "codigo nao existe";
				break;
			
		}
		
		//Segundo a api a seguir ao codigo vai 0 ou 1 caso seja nulo ou nao
		int pu = 0;
		
		if(retorno.indexOf("{") > 0 || (code.equals("createChat")) || (code.equals("patchConfigurations") && retorno.equals("1") ) )
			pu = 1;
		else
			pu = 0;
		
		System.out.println(code + ":" + pu + "//" + retorno);
		return code + ":" + pu + "//" + retorno;
	}	
	
	/**
	 * Method to get the code from request
	 * @param s - request
	 * @return code that was in the request
	 */
	public String getRequestCode(String s) {
		return (s.indexOf("{") > 1 ) ? s.substring(0,s.indexOf("{")) : " ";
	}
	
	/**
	 * Method to get the arguments from a request
	 * @param s - request
	 * @return  arguments that were in the request
	 */
	public String getRequestArguments(String s) {
		return (s.indexOf("{") > 1 && s.indexOf("}") > 1 ) ? s.substring(s.indexOf("{") + 1,s.indexOf("}")) : " ";
	}
}

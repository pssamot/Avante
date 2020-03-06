package leilao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import database.Siga;
import news.*;
import user.Clube;
import user.Player;
/**
 * Class that closes the auctions when his time finishes.
 * @author tomas
 *  
 */
public class AuctionThreadCleaner extends Thread {
	
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
    	
    	Timer timer = new Timer(); 
    	timer.schedule( new TimerTask() 
    	{ 
    	    public void run() {
    	    	
    	    	System.out.println();System.out.println("*****  Vou comecar a limpar  *****");
    	    	Connection myConn = Siga.myConn;
    	    	
    	    	Statement myStmt = null,myStmt2 = null,myStmt3 = null,myStmt4=null,myStmt5=null;
    			ResultSet myRs = null,myRs2 = null;
    			
    			try {
    				
    				
    				//Create a statement
    				myStmt = myConn.createStatement();
    				myStmt2 = myConn.createStatement(); 
    				myStmt3 = myConn.createStatement(); 
    				myStmt4 = myConn.createStatement(); 
    				myStmt5 = myConn.createStatement(); 
    				
    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    				
    				Date now = new Date();
    				sdf.format(now);
    				
    				//Result Set 
    				myRs = myStmt.executeQuery("SELECT * FROM Auctions WHERE is_active = 1 AND end_date < \""+ sdf.format(now) +"\" ORDER BY end_date");
    				
    				while (myRs.next()) {
    					
    					if(sdf.format(now).compareTo(myRs.getString("end_date").toString()) > 0 && myRs.getInt("is_active") == 1) {
    						
    						System.out.println("bid id  " + myRs.getInt("current_winner_bid_id"));
    						
    						myStmt2.executeUpdate("UPDATE Auctions SET is_active = 0 WHERE id = " + myRs.getInt("id"));
    						
    						if(myRs.getInt("current_winner_bid_id")>0) {
    							int id = myRs.getInt("player_id");
	    						myRs2 = myStmt3.executeQuery("SELECT * FROM Bids WHERE id = " + myRs.getInt("current_winner_bid_id") );
	    						
	    						if(myRs2.next()) {
	    							System.out.println("UPDATE Players SET club_id = "+ myRs2.getInt("user_id") +" WHERE id = " + id);
	    							
	    							//Aumenta o balance
	    							String clube_s2 = Clube.getClubByID(""+myRs.getInt("user_owner_id"));
	    							JSONArray clube_j2 = new JSONArray(clube_s2);
	    							myStmt5.executeUpdate("UPDATE Clubs SET balance = ( balance + "+ myRs2.getInt("amount") +") WHERE id = " + clube_j2.getJSONObject(0).getInt("id"));
	    							
	    							
	    							//Reduz o balance
	    							String clube_s = Clube.getClubByID(""+myRs2.getInt("user_id"));
	    							JSONArray clube_j = new JSONArray(clube_s);
	    							myStmt5.executeUpdate("UPDATE Clubs SET balance = ( balance - "+ myRs2.getInt("amount") +") WHERE id = " + clube_j.getJSONObject(0).getInt("id"));
	    							
	    							//Vai buscar o jogador
	    							String player_s = Player.getPlayerByID(""+id);
	    							JSONArray player_j = new JSONArray(player_s);	
	    							
	    							//cria noticia
	    							News n = new News("Compra",clube_j.getJSONObject(0).getString("name") + " comprou o jogador "+ player_j.getJSONObject(0).getString("name") + " por " + myRs2.getInt("amount")+" €!");
	    							n.save();
	    							
	    							myStmt4.executeUpdate("UPDATE Players SET club_id = "+ myRs2.getInt("user_id") +" WHERE id = " + id);
	    						}
	    						
    						}
    					}
    					
    				}
    				
    				System.out.println("*****  Acabei de Limpar  *****");System.out.println();
    				
    				
    				
    			} catch (SQLException | JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    	
    	    } 
    	}, 0, 30*(1000*1));
    	

    }

    
    /**
     * method to set balance to all clubs
     */
    public void updateBalance() {
    	Connection myConn = Siga.myConn;
    	
    	Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			
	    	for (int i = 1; i < 21; i++) {
	    		myStmt.executeUpdate("UPDATE Clubs SET balance = 1000000 WHERE id = " + i);
			}
	    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

    }
    
}

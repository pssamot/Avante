package leilao;

import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;

import database.Siga;
import news.News;
import user.Clube;
import user.Player;


/**
 * Class to deal with bid logic
 * @author tomas
 * 
 */
public class Bid {
	
	private int amount;
	private int user_id;
	private int auction_id;
	private java.util.Date created_at;
	
	public Bid( int amount, int user_id, int auction_id, String data) {
		
		this.amount = amount;
		this.user_id = user_id;
		this.auction_id = auction_id;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			java.util.Date d = sdf.parse(data);
			this.created_at = d ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * get the amount of bid
	 * @return amount of bid
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * set amount of this bid
	 * @param amount price amount of the bid
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * get the id of the user who did this bid
	 * @return value id of the user who did this bid
	 */
	public int getUser_id() {
		return user_id;
	}
	
	/**
	 * set the if of the user who did this bid
	 * @param user_id  id of the user
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * get the if of auction
	 * @return the id of the auction
	 */
	public int getAuction_id() {
		return auction_id;
	}
	
	/**
	 * set auction_id of the bid
	 * @param auction_id value of auction id
	 */
	public void setAuction_id(int auction_id) {
		this.auction_id = auction_id;
	}
	
	/**
	 * get the create at date
	 * @return the create at date
	 */
	public Date getCreated_at() {
		return created_at;
	}
	
	/**
	 * set the bid created_at date
	 * @param created_at - created_at date
	 */
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * Method to save the bid to the database
	 * @return auction id if everything went well, 0 if error
	 */
	public String save() {
		
		String leilaoS = Auction.getAuctionByID(this.auction_id);
		Siga ligacao = new Siga();
		String bid_id = null, j= null;
		JSONArray leilaoJ = null;
		
		try {
			leilaoJ = new JSONArray(leilaoS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			System.out.println("current_bid   "+leilaoJ.getJSONObject(0));
			
			if(!leilaoJ.getJSONObject(0).has("current_bid")) {
					
				bid_id = ligacao.sigaMaria(true, "INSERT INTO Bids (amount,user_id,auction_id) VALUES (" + this.getAmount() + "," + this.getUser_id() + "," + this.getAuction_id()+")");
				
				j = ligacao.sigaMaria(true,"UPDATE Auctions SET current_winner_bid_id = "+ bid_id +" WHERE id = " + this.getAuction_id() );
				return ""+this.getAuction_id();
			}
			
			if(this.amount > leilaoJ.getJSONObject(0).getJSONArray("current_bid").getJSONObject(0).getInt("amount") &&  
					sdf.parse(leilaoJ.getJSONObject(0).getString("end_date")).compareTo(this.created_at) > 0 
					 ) {
				
				bid_id = ligacao.sigaMaria(true, "INSERT INTO Bids (amount,user_id,auction_id) VALUES (" + this.getAmount() + "," + this.getUser_id() + "," + this.getAuction_id()+")");
				
				j = ligacao.sigaMaria(true,"UPDATE Auctions SET current_winner_bid_id = "+ bid_id +" WHERE id = " + this.getAuction_id() );
				return ""+this.getAuction_id();
			}
			
		} catch (SQLException | JSONException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "\""+0+ "\"";
	}
	
	/**
	 * get a bid from his id
	 * @param id of the bid to retrieve
	 * @return bid with the id = [param id]
	 */
	public static String getBidByID(String id) {
		
		Siga ligacao = new Siga();
		String json = null;
		
		try {
			json = ligacao.sigaMaria(false,"SELECT * FROM Bids WHERE id = " + id );		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}
	
	/**
	 * Method which deals with buy nows requests.
	 * Updating balance. Changing player club.
	 * @return empty array
	 */
	public String buyNow(){
		
		Siga ligacao = new Siga();
		String leilaoS = Auction.getAuctionByID(this.auction_id), updatePlayer = null;
		System.out.println("entrar");
		JSONArray leilaoJ = null;
		
		try {
			leilaoJ = new JSONArray(leilaoS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			System.out.println(leilaoJ.getJSONObject(0).getBoolean("is_active") );
			if(this.amount == leilaoJ.getJSONObject(0).getInt("buy_now_price") &&  
					sdf.parse(leilaoJ.getJSONObject(0).getString("end_date")).compareTo(this.created_at) > 0 &&
					leilaoJ.getJSONObject(0).getBoolean("is_active")) {
				
				
				//Aumenta o balance
				String clube_s2 = Clube.getClubByID(""+leilaoJ.getJSONObject(0).getInt("user_owner_id"));
				JSONArray clube_j2 = new JSONArray(clube_s2);
				ligacao.sigaMaria(true,"UPDATE Clubs SET balance = ( balance + "+ this.amount +") WHERE id = " + leilaoJ.getJSONObject(0).getInt("user_owner_id"));
				
				
				//Reduz o balance
				String clube_s = Clube.getClubByID(""+this.user_id);
				JSONArray clube_j = new JSONArray(clube_s);
				ligacao.sigaMaria(true,"UPDATE Clubs SET balance = ( balance - "+ this.amount +") WHERE id = " + this.user_id);
				
				//Vai buscar o jogador
				String player_s = Player.getPlayerByID(""+leilaoJ.getJSONObject(0).getJSONArray("jogador").getJSONObject(0).getInt("id"));
				JSONArray player_j = new JSONArray(player_s);	
				
				
				//cria noticia
				News n = new News("Compra",clube_j.getJSONObject(0).getString("name") + " comprou o jogador "+ player_j.getJSONObject(0).getString("name") + " por " + this.amount + " €!" );
				n.save();
				
				updatePlayer = ligacao.sigaMaria(true,"UPDATE Players SET club_id = "+ this.user_id +" WHERE id = " + leilaoJ.getJSONObject(0).getJSONArray("jogador").getJSONObject(0).getInt("id") );
				updatePlayer = ligacao.sigaMaria(true,"UPDATE Auctions SET is_active = 0 WHERE id = " + this.getAuction_id() );
				return Clube.getClubByID(""+this.user_id);
			}
		} catch (JSONException | ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[]";
		
	}
	

}

package leilao;

import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import date.Data;
import user.Player;
//import java.util.ArrayList;
import database.Siga;

/**
 * Class to manage Auctions
 * @author tomas
 * 
 */
public class Auction {
	
	private String end_date;
	private String start_date;
	
	
	private int user_owner_id;
	private int user_current_winner_id;
	
	private int current_bid;
	private int buy_now_price;
	private int player_id;
	private int is_active;
	private int minimum_diff;
	private int starting_price;
	
	private int duration; //*

	
	/**
	 * Constructor of auction, will create a new auctions with the parameters below
	 * 
	 * @param start_date - auction starting date
	 * @param segundos - duration of the auctions
	 * @param user_owner_id - id of the auction owner
	 * @param current_bid_id - id of the current bid id
	 * @param player_id - id of the player being sold in the auction
	 * @param is_active - is the auction active
	 * @param minimum_diff - minimum bid difference 
	 * @param starting_price - starting price of an auction
	 * @param buy_now - price to the option buy now
	 * @throws ParseException error in transformation seconds to int
	 */
	public Auction( String start_date, int segundos, int user_owner_id, int current_bid_id, int player_id, int is_active,int minimum_diff, int starting_price, int buy_now) throws ParseException {
		
		System.out.println("Estamos a criar um novo leilao");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		java.util.Date data = sdf.parse(start_date);
		
		java.util.Date data_fim = Data.addSeconds(data, segundos);
		
		this.end_date = sdf.format(data_fim);
		
		this.start_date = start_date;
		this.user_owner_id = user_owner_id;
		this.current_bid = current_bid_id;
		this.player_id = player_id;
		this.is_active = is_active;
		this.minimum_diff = minimum_diff;
		this.starting_price = starting_price;
		this.setBuyNowPrice(buy_now);
		System.out.println("Saiu a criar um novo leilao");
		
//		this.Auctions = new ArrayList<Auction>();  NAO PODE SER NESTA CLASSE
	}
	
	/**
	 * set the end date of an auction
	 * @param end - auction ending date
	 */
	public void setEndDate(String end) {
		this.end_date=end;
	}
	
	/**
	 * set the starting date of an auction
	 * @param start - auction starting date
	 */
	public void setStartDate(String start) {
		 this.start_date=start;
	 }
	  
	 
	/**
	 * set the auction owner id 
	 * @param id id of the user owner of the auction 
 	 */
	public void setUserOwner(int id) {
		this.user_owner_id=id;
	 }
	 
	
	/**
	 * set the id of the highest bid
	 * @param bid_id id of the highest bid
	 */
	public void setCurrentBid(int bid_id ) {
		 if(bid_id>this.current_bid) {
			 this.current_bid=bid_id;
		 }
	 }
	 
	 /**
	  * set the id of the player that is being sold
	  * @param player_id id of the player that is being sold
	  */
	public void setPlayer(int player_id) {
		 this.player_id=player_id;
	 }
	 
	 /**
	  * set id of the user that has the highest bid
	  * @param user_id id of the user that has the highest bid
	  */
	public void setCurrentWinner(int user_id) {
		this.user_current_winner_id=user_id;
	 }
	 
	 /**
	  * set the minimum price difference between bids
	  * @param minDiff minimum price difference between bids
	  */
	public void setMinimumPrice(int minDiff) {
		 this.minimum_diff=minDiff;
	 }
	 
	 /**
	  * set starting price of auction
	  * @param startingPrice for the auction
	  */
	public void setStartingPrice(int startingPrice) {
		 this.starting_price=startingPrice;
	 }
	 
	 /**
	  * get ending date of auction
	  * @return ending date of auction
	  */
	public String getEndDate() {
		 return this.end_date;
	 }
	 
	 /**
	  * get the starting date
	  * @return the starting date of auction
	  */
	public String getStartDate() {
		 return this.start_date;
	 }
	 
	 /**
	  * get id of the current user that has highest bid
	  * @return id of the current user that has highest bid
	  */
	public int getUserOwner() {
		 return this.user_owner_id;
	 }
	 
	 /**
	  * get id of the bid that is currently winning
	  * @return id of the bid that is currently winning
	  */
	public int getCurrentBid() {
		return this.current_bid;
	 }
	 
	 /**
	  * get the id of this auction player 
	  * @return id of this auction player 
	  */
	public int getPlayer() {
		 return this.player_id;
	 }
	 
	 /**
	  * get the id of the current user that is winning
	  * @return the id of the current user that is winning
	  */
	public int getCurrentWinner() {
		 return this.user_current_winner_id;
	 }
	 
	 /**
	  * get the minimum difference of auction
	  * @return minimum difference of auction
	  */
	public float getMinimumDiff() {
		 return this.minimum_diff;
	 }
	 
	 /**
	  * get starting price of auction
	  * @return the starting price of auction
	  */
	public float getStartingPrice() {
		 return this.starting_price;
	 }
	 
	 
	/**
	 * get the duration of the auction
	 * @return duration of the auction
	 */
	public int getDuration() {
		return duration;
	}
	 
	/**
	 * set the duration of the auctions
	 * @param duration - duration of the auction
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Save auction to the database
	 * @return the auction id that was created
	 */
	public String save() {
			System.out.println("Estamos a guardar um novo leilao");
			Siga ligacao = new Siga();
			String json = null, check1stAuction = null;
			JSONArray old = null;
			
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateobj = new Date();
				
				check1stAuction = ligacao.sigaMaria(false,"SELECT * FROM Auctions WHERE is_active = 1 AND player_id = " + this.getPlayer() + " AND end_date > " + "\"" + df.format(dateobj) + "\"" );
				old = new JSONArray(check1stAuction);
				
				if(old.length() == 0) {
					json = ligacao.sigaMaria(true,"INSERT INTO Auctions (starting_price, minimum_bid_difference, start_date,end_date, user_owner_id, player_id,buy_now_price,current_winner_bid_id) VALUES ("+ this.getStartingPrice() + ","+ this.getMinimumDiff() + ", \""+ this.getStartDate() + "\",\""+ this.getEndDate() + "\","+ this.getUserOwner() + ","+ this.getPlayer()+ ","+ this.getBuyNowPrice() + ",NULL)" );	
				}else {
					json = "-1";
				}
				
			} catch (SQLException | JSONException e){ 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return json;
	}
	
	/**
	 * Method to get auction by id
	 * @param auction_id - id of the auctions
	 * @return json array with all data of an auction
	 */
	public static String getAuctionByID(int auction_id) {
		
		Siga ligacao = new Siga();
		String json = null, player = null, bid = null, response= null;
		JSONArray j = null;
		
		if(auction_id > 0) {
			try {
				json = ligacao.sigaMaria(false,"SELECT Auctions.*, Users.id as club_user_owner_id FROM Auctions JOIN Clubs ON (Auctions.user_owner_id = Clubs.id) JOIN Users ON (Clubs.user_id = Users.id) WHERE Auctions.id = " + auction_id );

				try {
					
					
					j = new JSONArray(json);
					
					if(j.length() > 0) {
						player = Player.getPlayerByID("\""+ j.getJSONObject(0).getInt("player_id")+"\"");
						j.getJSONObject(0).append("jogador", new JSONArray(player).getJSONObject(0));
						
						if(j.getJSONObject(0).optInt("current_winner_bid_id") > 0) {
							bid = Bid.getBidByID("\""+ j.getJSONObject(0).getInt("current_winner_bid_id")+"\"");
							j.getJSONObject(0).append("current_bid", new JSONArray(bid).getJSONObject(0));
						}
					}
					response = j.toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			response = "2";
		}
		
		
		return response;
	}
	
	/**
	 * Method to get All the active auctions
	 * @return json array with all active auctions
	 */
	public static String getAllAuctions() {
			
		Siga ligacao = new Siga();
		String json = null, player = null, bid = null;
		JSONArray j = null;
		
		try {
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateobj = new Date();
			df.format(dateobj);
			json = ligacao.sigaMaria(false,"SELECT Auctions.*, Users.id as club_user_owner_id FROM Auctions JOIN Clubs ON (Auctions.user_owner_id = Clubs.id) JOIN Users ON (Clubs.user_id = Users.id) WHERE Auctions.is_active = 1 AND end_date > \"" + df.format(dateobj) + "\"");
			
			try {
				
				j = new JSONArray(json);
				
				if(j.length() > 0) {
					for (int i = 0; i < j.length(); i++) {
						
						player = Player.getPlayerByID("\""+ j.getJSONObject(i).getInt("player_id")+"\"");
						j.getJSONObject(i).append("jogador", new JSONArray(player).getJSONObject(0));
						
						if(j.getJSONObject(i).optInt("current_winner_bid_id") > 0) {
							bid = Bid.getBidByID("\""+ j.getJSONObject(i).getInt("current_winner_bid_id")+"\"");
							j.getJSONObject(i).append("current_bid", new JSONArray(bid).getJSONObject(0));
						}

					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j.toString();
		
	}
	
	/**
	 * Get the buy now price
	 * @return the buy now price
	 */
	public int getBuyNowPrice() {
		return buy_now_price;
	}
	
	/**
	 * Set the buy now price 
	 * @param buy_now_price - price that the auction can be bought without bids
	 */
	public void setBuyNowPrice(int buy_now_price) {
		this.buy_now_price = buy_now_price;
	}

	
	/**
	 * Method to search for a player that is on sale
	 * @param t - string to search
	 * @return all the auction where found the string to search t
	 */
	public static String onSearch(String t) {
		
		Siga ligacao = new Siga();
		JSONArray j = new JSONArray();
		String c = null;
		
		try {
			
			c = ligacao.sigaMaria(false,"SELECT Auctions.id FROM Auctions "
										+"JOIN Players ON Auctions.player_id = Players.id "
										+ "JOIN Users ON Users.id = Players.user_id "
										+ "WHERE Auctions.is_active = 1 AND Users.name LIKE \"%"+ t +"%\"");
			
			JSONArray o = new JSONArray(c);
			
			for (int i = 0; i < o.length(); i++) {
				j.put(new JSONArray(getAuctionByID(o.getJSONObject(i).getInt("id"))).getJSONObject(0) );
			}

		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j.toString();
	}

}

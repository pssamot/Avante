package chat;

import java.sql.Date;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Siga;

/**
 * Class to deal with chat interactions
 * @author Tomas
 *
 */
public class Chat {
	private int id;
	private String user_id;
	private String user_2_id;

	/**
	 * Constructor of Chat Class
	 * @param user_id value of one user of the chat
	 * @param user_2_id value other user of the chat
	 */
	public Chat(String user_id, String user_2_id) {
		this.user_id = user_id;
		this.user_2_id = user_2_id;
	}
	
	/**
	 * Method that creates a new chat or returns a old one with the same users
	 * 
	 * @return chat_id
	 */
	public String save() {
		
		Siga ligacao = new Siga();
		String chat_id = null, old = null;

		JSONArray chat= null; 
		try {
			old = ligacao.sigaMaria(false, "Select * FROM Chats WHERE ("+
													"user_id1 = " + this.getUser_id()+
													" AND user_id2 = " + this.getUser_2_id()+ 
													") OR ( user_id1 = " + this.getUser_2_id() + 
													" AND user_id2 = " + this.getUser_id() + ")" );
			System.out.println(old);
			chat = new JSONArray(old);
			
			if(old.equals("[]")) {
				chat_id = ligacao.sigaMaria(true, "INSERT INTO Chats(user_id1,user_id2) VALUES (" + this.getUser_id() + "," + this.getUser_2_id()+")");
			}else {
				chat_id = ""+chat.getJSONObject(0).getInt("id");
			}
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return chat_id;
	}
	
	/**
	 * get this chat id
	 * @return id of chat
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * set the id of the chat
	 *  
	 * @param id value of this chat id
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * get id from one user of this chat
	 * 
	 * @return user_id
	 * 
	 */
	public String getUser_id() {
		return user_id;
	}
	
	
	/**
	 * set one user id of this chat
	 * 
	 * @param user_id id of the user
	 * 
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * get id from the other user of this chat
	 * 
	 * @return user_2_id id of other user
	 * 
	 */
	public String getUser_2_id() {
		return user_2_id;
	}
	
	/**
	 * set the other user id  of this chat
	 * @param user_2_id  id of the user
	 */
	public void setUser_2_id(String user_2_id) {
		this.user_2_id = user_2_id;
	}
	
	
	/**
	 * @param id value of a chat id
	 * 
	 * @return string the chat and all messages
	 */
	public static String getChatById( String id) {
		String c = null, messages = null;
		Siga ligacao = new Siga();
		JSONArray j = null;
		
		try {
			
			c = ligacao.sigaMaria(false,"SELECT * FROM Chats WHERE Chats.id = "+id);
			j = new JSONArray(c);
			
			messages = ligacao.sigaMaria(false,"SELECT * FROM Messages WHERE Messages.chat_id = " + id + " ORDER BY Messages.create_at");

			j.getJSONObject(0).append("messages", new JSONArray(messages));
			
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j.toString();
	}
	
	/**
	 * get all chats from one user
	 *  
	 * @param user_id id of the user
	 * 
	 * @return string of a json array with all chats belonging to the user
	 */
	public static String getAllMyChats(String user_id) {
		Siga ligacao = new Siga();
		JSONArray j = new JSONArray();
		String c = null;
		
		try {
			
			c = ligacao.sigaMaria(false,"SELECT * FROM Chats WHERE Chats.user_id1 = "+user_id+" OR Chats.user_id2 = "+ user_id);
			JSONArray o = new JSONArray(c);
			
			for (int i = 0; i < o.length(); i++) {
				j.put(new JSONArray(getChatById(""+o.getJSONObject(i).getInt("id"))).getJSONObject(0) );
			}

		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j.toString();
	}
	
}

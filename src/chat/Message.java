package chat;

import java.sql.SQLException;

import database.Siga;

/**
 * Class to deal with new messages
 * @author tomas
 *
 */
public class Message {
	
	private int id;
	private String user_id;
	private String chat_id;
	private String text;
	
	/**
	 * create a new message
	 * 
	 * @param user_id  id of the user
	 * @param chat_id id of the chat
	 * @param text text od the message
	 */
	public Message(String user_id, String chat_id, String text) {

		this.user_id = user_id;
		this.chat_id= chat_id;
		this.text = text;
		
	}
	
	/**
	 * method to save a new message in database.
	 * @return chat and all his messages
	 */
	public String save() {
		
		Siga ligacao = new Siga();

		try {
			ligacao.sigaMaria(true, "INSERT INTO Messages(user_id,chat_id,texto) VALUES (" + this.getUser_id() + "," + this.getChat_id()+ ",\""+this.getText() + "\")");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return Chat.getChatById(this.chat_id);
	}
	
	/**
	 * get id of this message
	 * @return id of this message
	 */
	public int getId() {
		return id;
	}
	/**
	 * set a message with id = [param id]
	 * @param id message id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * get user_id that wrote the message
	 * 
	 * @return user_id 
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * set the user_id that wrote this message 
	 * @param user_id  id of the user
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * get the text of this message
	 * 
	 * @return text of this message
	 */
	public String getText() {
		return text;
	}
	/**
	 * set the text = [param text] of this message
	 * @param text text of the message
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * get id from the chat, which this message belongs to
	 * @return chat_id id of the chat
	 */
	public String getChat_id() {
		return chat_id;
	}
	/**
	 * set id=[param chat_id] from the chat, which this message belongs to
	 * @param chat_id if of the chat
	 */
	public void setChat_id(String chat_id) {
		this.chat_id = chat_id;
	}
	
	

}

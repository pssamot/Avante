package user;
import java.sql.*;


import database.Siga;
import org.json.JSONArray;
import org.json.JSONException;

 
/**
 * Class which manage all interactions with the clubs
 * @author tomas
 * 
 */
public class Clube extends User{
	
	protected int balance;
	protected String liga;
	
	
	public Clube(String username,String name , String pass, boolean status, String Liga, int balance) {
		
		super(name,username,pass,status);
		this.balance=balance;
		this.liga=Liga;
		
	}

	public int getBalance() {
		return this.balance;
	}
	
	/* (non-Javadoc)
	 * @see User.User#getName()
	 */
	@Override
	public String getName() {
	return this.name;
	}
	
	/* (non-Javadoc)
	 * @see User.User#getPass()
	 */
	@Override
	public String getPass() {
	return this.pass;
	}
	

	/* (non-Javadoc)
	 * @see User.User#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name=name;
	}
	
	/**
	 * Get Liga of this clube
	 * @return liga of the clube
	 */
	public String getLiga(){
		return this.liga;
	}
	
	/**
	 * set the liga of this club
	 * @param liga liga of the clube
	 */
	public void setLiga(String liga) {
		this.liga=liga;
	}

	/* (non-Javadoc)
	 * @see User.User#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see User.User#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id=id;
	}

	/* (non-Javadoc)
	 * @see User.User#getStatus()
	 */
	@Override
	public boolean getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see User.User#setStatus(boolean)
	 */
	@Override
	public void setStatus(boolean status) {
		this.status=status;
	}

	/**
	 * Mehtod to save club to database
	 * @return the club id
	 */
	public String save() {
		System.out.println("Vamos Guardar");
		
		Siga ligacao = new Siga();
		String user_id = null, club_id = null, checkIfExists = null;
		JSONArray old = null;
		
		try {
			
			checkIfExists = ligacao.sigaMaria(false,"SELECT * FROM TemCalma.Users WHERE username = \"" + this.getUserName() + "\"" );
			old = new JSONArray(checkIfExists);
			System.out.println(old);
			if(old.length()==0) {
				user_id = ligacao.sigaMaria(true,"INSERT INTO TemCalma.Users (username,password,name) VALUES (\"" +this.getUserName()+"\",\"" +this.getPass() + "\",\"" + this.getName()+ "\")");
				club_id = ligacao.sigaMaria(true,"INSERT INTO TemCalma.Clubs (user_id,liga) VALUES (" + user_id +",\""+this.getLiga() +"\""+")" );
			}else {
				club_id = "-1";
			}
			
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Guardou");
		return club_id;
	}
	
	/**
	 * Method to get club with user and pass
	 * @param user - username of the user
	 * @param pass - pass of the user
	 * @return user with the credentials
	 */
	public static String getClub(String user, String pass) {
		
		Siga ligacao = new Siga();
		String json = null, players = null;
		JSONArray j = null;
		
		try {
			
			json = ligacao.sigaMaria(false,"SELECT Clubs.*, Users.username,Users.password, Users.name FROM Clubs JOIN Users ON Clubs.user_id = Users.id WHERE username = \"" + user + "\" and password = \""+ pass+ "\"");
			
			try {
				j = new JSONArray(json);
				
				if(j.length() > 0 ) {
		
					players = ligacao.sigaMaria(false,"SELECT Players.idade, Players.posicao,Players.id, Users.name FROM Players JOIN Users ON Players.user_id = Users.id WHERE Players.club_id = " + j.getJSONObject(0).getInt("id") );
					j.getJSONObject(0).append("jogadores", new JSONArray(players));
				}

				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Json" + json);
		return j.toString();
	}
	
	/**
	 * get club with id = [param id]
	 * @param id of the club
	 * @return club with all his players
	 */
	public static String getClubByID(String id) {
		
		Siga ligacao = new Siga();
		String json = null, players = null, response = null;
		JSONArray j = null;
		
		
		if(Integer.parseInt(id) > 0) {
			try {
				json = ligacao.sigaMaria(false,"SELECT Clubs.*, Users.username,Users.password, Users.name FROM Clubs JOIN Users ON Clubs.user_id = Users.id  WHERE Clubs.id = " + id );		
				
				try {
					j = new JSONArray(json);
					
					if(json.length() > 0) {
						players = ligacao.sigaMaria(false,"SELECT Players.idade, Players.posicao,Players.id, Users.name FROM Players JOIN Users ON Players.user_id = Users.id WHERE Players.club_id = " + j.getJSONObject(0).getInt("id") );
						j.getJSONObject(0).append("jogadores", new JSONArray(players));
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
		
		//System.out.println("Json" + j);
		return response;
	}
	/**
	 * get other clubs users ids expect my own. Useful in the application chat.
	 * @param my_user_id - my user id
	 * @return all clubs except mine
	 */
	public static String getContacts(String my_user_id) {
		Siga ligacao = new Siga();
		String  q = null;

		 try {
			q = ligacao.sigaMaria(false,"SELECT Users.id, Users.name FROM Clubs JOIN Users ON Clubs.user_id = Users.id WHERE Users.id != " + my_user_id );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return q;
	}
	/**
	 * change my configurations with the params
	 * @param name new users name
	 * @param pass new users pass 
	 * @param user_id  id of the user
	 * @return 1 if success || 0  if not
	 */
	public static String updateConfigurations(String name, String pass, String user_id) {
		
		Siga ligacao = new Siga();
		String  q = null;
		
		if(name!=null || pass!=null ) {
			try {
				if(name!=null)
					q = "username = \"" + name + "\" ";
				
				if (pass!=null)
					q = q + ", password = \"" + pass + "\" ";
				
				ligacao.sigaMaria(true,"UPDATE Users SET "+ q +"WHERE id = " + user_id );
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "0";
			}
			
		}

		return "1";
		
	}

	/* (non-Javadoc)
	 * @see User.User#getUserName()
	 */
	@Override
	public String getUserName() {		
		return this.username;
	}
	

	


}

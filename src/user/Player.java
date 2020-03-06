package user;

import java.sql.SQLException;

import database.Siga;


/**
 * Class which manage players interactions
 * @author tomas
 *
 */
public class Player extends User{
	protected String club_Id;
	protected String age;
	protected String position;
	
	/**
	 * Constructor of player need all params below
	 * @param name  player name
	 * @param username  player username
	 * @param pass  player password
	 * @param status player is activated or not
	 * @param club_Id club_id of the player
	 * @param age  player's age
	 * @param position player's position
	 */
	public Player(String name,String username,String pass, boolean status, String club_Id, String age,String position) {
		super(name,username ,pass,status);
		System.out.println("Created new Player");
		this.club_Id=club_Id;
		this.age=age;
		this.position=position;
		
	}
	
	/* (non-Javadoc)
	 * @see User.User#getUserName()
	 */
	@Override
	public String getUserName() {		
		return this.username;
	}
	
	/* (non-Javadoc)
	 * @see User.User#getPass()
	 */
	public String getPass() {
		return this.pass;
	}
	
	/**
	 * get player's age 
	 * @return player's age
	 */
	public String getAge() {
		return this.age;
	}
	/**
	 * set player's age = [param age]
	 * @param age player's age
	 */
	public void setAge(String age) {
		this.age=age;
	}
	
	/* (non-Javadoc)
	 * @see User.User#getName()
	 */
	@Override
	public String getName() {
	return this.name;
	}
	
	/* (non-Javadoc)
	 * @see User.User#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name=name;
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
	 * get club_id of player
	 * @return club_id
	 */
	public String getClub_Id() {
		return club_Id;
	}


	/**
	 * set the club_id = [param club_Id] of player
	 * @param club_Id club_id of player
	 */
	public void setClub_Id(String club_Id) {
		this.club_Id = club_Id;
	}


	/**
	 * get the position of the player
	 * @return position of the player
	 */
	public String getPosition() {
		return position;
	}


	/**
	 * set position = [param postiion]
	 * @param position position of the player
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	
	/**
	 * Saves a player to database
	 * @return player just created
	 */
	public String save() {
		
		Siga ligacao = new Siga();
		String user_id = null,player_id=null;
		
		try {
			user_id = ligacao.sigaMaria(true,"INSERT INTO TemCalma.Users (username,password,name) VALUES (\"" +this.getUserName()+"\",\"" +this.getPass() + "\",\"" + this.getName()+ "\")");
			player_id = ligacao.sigaMaria(true, "INSERT INTO TemCalma.Players (user_id,club_id, idade, posicao) VALUES (" + user_id +
																														"," + this.getClub_Id() +
																														"," + this.getAge()+ 
																														"," +"\"" + this.getPosition()+"\"" + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Saved Player");
		return getPlayerByID(player_id);
	}
	
	/**
	 * Method to get player from an id
	 * @param id of the player that you want
	 * @return all of the player data
	 */
	public static String getPlayerByID(String id) {
		
		Siga ligacao = new Siga();
		String json = null;
		try {
			json = ligacao.sigaMaria(false,"SELECT Players.*, Users.username,Users.password, Users.name FROM Players JOIN Users ON Players.user_id = Users.id  WHERE Players.id = " + id );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * Method to update the name user name and pass word
	 * @param name new name / or empty if the same
	 * @param pass new pass / or empty if the same
	 * @param user_id  id of the user
	 * @return the id of the user
	 */
	public static String updateConfigurations(String name, String pass, int user_id) {
		System.out.println("name "+name+ " pass " + pass);
		Siga ligacao = new Siga();
		String json = null, q = null;
		
		if(name!=null || pass!=null ) {
			try {
				if(name!=null)
					q = "username = " + name + " ";
				
				if (pass!=null)
					q = q + "pass = " + name + " ";
				
				
				json = ligacao.sigaMaria(true,"UPDATE * FROM Users SET "+ q +"WHERE id = " + user_id );
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Json" + json);
		}

		return json;
		
	}


}

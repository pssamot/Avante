package user;

/**
 * Abstact Class that is the login data of the clubs and the players
 * @author tomas
 * 
 */
public abstract class User {
	
	protected String name;
	protected String username;
	protected String pass;
	protected int id;
	protected boolean status;
	
	/**
	 * Constructor of a new user 
	 * @param name users name
	 * @param username users username
	 * @param pass users pass
	 * @param status user status
	 */
	public User(String name, String username, String pass, boolean status){
		this.username=username;
		this.name=name;
		this.pass=pass;
		this.status=status;
		System.out.println("Created new User");
	}
	
	
	/**
	 * get name of the user
	 * @return name of the suer
	 */
	public abstract String getName();
	
	/**
	 * get username of the user
	 * @return username of the user
	 */
	public abstract String getUserName();
	
	/**
	 * get pass of the user
	 * @return pass of the user
	 */
	public abstract String getPass();
	
	/**
	 * set name of the user
	 * @param name name of the player
	 */
	public abstract void setName(String name);
	
	/**
	 * get id of the user
	 * @return id of the user
	 */
	public abstract int getId();
	
	/**
	 * set the id of the user
	 * @param id id of the user
	 */
	public abstract void setId(int id);
	
	/**
	 * get status of the user
	 * @return status of the user
	 */
	public abstract boolean getStatus();
	
	/**
	 * set status of the user
	 * @param status if the user is active or not
	 */
	public abstract void setStatus(boolean status);
	

}

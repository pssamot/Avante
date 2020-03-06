package news;

import java.sql.SQLException;

import org.json.JSONArray;

import database.Siga;

/**
 * Class to deal with the news
 * @author tomas
 * 
 */
public class News {

	private String title;
	private String description;
	
	/**
	 * Constructor to create a new New
	 * @param title - news title
	 * @param description - news description
	 */
	public News(String title, String description) {
		this.title=title;
		this.description=description;
	}
	
	/**
	 * Method to save New to database
	 */
	public void save() {
		
		Siga ligacao = new Siga();

		try {
			ligacao.sigaMaria(true, "INSERT INTO News(title,description) VALUES (\"" + this.getTitle() + "\",\"" + this.getDescriprion()+"\")");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	
	
	/**
	 * set the News description
	 * @param description News description
	 */
	public void setDescription(String description) {
		this.description=description;
	}
	
	
	/**
	 * get News description
	 * @return News description
	 */
	public String getDescriprion() {
		return this.description;
	}
	
	/**
	 * get the News title
	 * @return News title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * set News title
	 * @param title news title
	 */
	public void setTitle(String title) {
		this.title=title;
	}
	
	/**
	 * Method to get all News
	 * @return Json array with all News
	 */
	public static String getAllNews() {
		Siga ligacao = new Siga();
		String r = null;
		try {
			r = ligacao.sigaMaria(false, "SELECT * FROM News");
			System.out.println(r);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}

}

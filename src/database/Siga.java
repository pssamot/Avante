package database;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class to deal with database queries and connections
 * 
 * @author tomas
 *
 */
public class Siga {
	
	/**
	 * static connection variable
	 */
	public static Connection myConn = null;
	
	/**
	 * Method which starts the connection to localhost mysql database 
	 */
	public void bamboraBino() {
		System.out.println("Vamos comecar ligacao");
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/TemCalma?zeroDateTimeBehavior=convertToNull", "phpmyadmin" , "tomas@2018");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method which closes the connection to database
	 */
	public void adeusBino() {
		
		if (myConn != null) {
			try {
				myConn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Vamos Finalizar ligacao");
	}
	
	
	/**
	 * Method that do a query to database.
	 * If is an inser you can retrieve the last id inserted
	 * 
	 * @param isUpdate - true if is an insert/set/update false if is select
	 * @param query - query you wnat to perform
	 * @return string - json array with the values from the query
	 * @throws SQLException throws sql exception if any error happen
	 */
	public String sigaMaria(boolean isUpdate, String query) throws SQLException {
		
		Statement myStmt = null;
		ResultSet myRs = null;
		String ola = " ";
		
		try { 
			
			//Create a statement
			myStmt = myConn.createStatement();
			
			System.out.println("Vai executar: " + query);
			
			// Caso seja uma query para alterar a tabela (insert/delete..)
			if(isUpdate) {
				myStmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS); // RETURN_GENERATED_KEYS retorna GeneratedKeys
				
				ResultSet keys = myStmt.getGeneratedKeys(); //pegamos nos dados automaticamente gerados
				if(keys!=null) {
					while (keys.next()) {
						ola = keys.getNString(1) ; //obtemos o ids
					}
				}
			}else {
				
				myRs = myStmt.executeQuery(query);
				
				// Convertos para json o ResultSet e para String
				 ola = 	convertToJSON(myRs).toString();
				 
				//while (myRs.next()) {
				//	System.out.println(myRs.getString("username") + ", " + myRs.getString("password"));
				//	ola = myRs.getString("username");
				//}
				 
			}
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
		}
		return ola;
	}
	
	/**
	 * Converts the result set from the database to json struct
	 * 
	 * @param resultSet - data from the sql queries
	 * @return json array with results from the database
	 * @throws Exception if any error
	 */
	public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
		
        JSONArray jsonArray = new JSONArray();
        
        while (resultSet.next()) {
        	JSONObject obj = null;
            int total_rows = resultSet.getMetaData().getColumnCount();
            obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
         
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

}

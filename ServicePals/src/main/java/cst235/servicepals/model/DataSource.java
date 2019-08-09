package cst235.servicepals.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides all database functionality for the score service web service
 */
public class DataSource {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private boolean connectedToDb;
	private String dbURL = "jdbc:mysql://localhost:3306";
	private String dbUser = "root";
	private String dbPassword = "root";
	private String database = "servicepals";
	private String tblUsers = database + ".users";
	private String tblCommunities = database + ".communities";
	private String tblServiceProviders = database + ".providers";
	private String tblUserComm = database + ".user_community";
	private String tblUserCommService = database + ".user_comm_service";

	/**
	 * Creates a connection to the database
	 */
	public DataSource() {
		try {
			//Connect to the database
			this.conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
			
			//Create a Statement object
			try {
				this.stmt = conn.createStatement();
				this.connectedToDb = true;
			}
			catch(SQLException e) {
				e.printStackTrace();
				this.connectedToDb = false;
			}				
		}
		catch(SQLException e) {
			e.printStackTrace();
			this.connectedToDb = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes an open database connection
	 */
	public void close() {
		if(this.connectedToDb) {
			try {
				this.conn.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Searches for a user in the database by username and password
	 * @return the user ID if successful, -1 if credentials don't match
	 */
	public int dbGetUserIdFromCredentials(String username, String password) {
		String sql = "SELECT user_id FROM " + tblUsers
			+ " WHERE " + tblUsers + ".user_name = '" + username + "' AND "
			+ tblUsers + ".password = '" + password + "'";
		if(this.connectedToDb) {
			try {
				//Execute SQL statement and get a result set
				this.rs = stmt.executeQuery(sql);
				
				//Process the result set
				if(this.rs.next()) {
					return rs.getInt("user_id");
				}
			}
			catch(SQLException e) {
				System.out.println("\nERROR: NO USER FOUND WITH THOSE CREDENTIALS");
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * queries the users table to determine if the username already exists
	 * @param username the username to check
	 * @return true if the username exists; false if not
	 */
	public boolean checkUserAlreadyExists(String username) {
		String sql = "SELECT user_id FROM " + tblUsers
			+ " WHERE " + tblUsers + ".user_name = '" + username + "'";
		if(this.connectedToDb) {
			try {
				//Execute SQL statement and get a result set
				this.rs = stmt.executeQuery(sql);
				
				//Process the result set
				if(this.rs.next()) {
					return true;
				}
			}
			catch(SQLException e) {
				System.out.println("\nERROR VALIDATING USERNAME");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Creates a user in the users table
	 * @param firstName the user's first name
	 * @param lastName the user's last name
	 * @param username the user's username
	 * @param password the user's password
	 * @param emailAddress the user's email address
	 * @return the user_id of the new user 
	 */
	public int dbCreateUser(String firstName, String lastName, String username,
		String password, String phoneNumber, String emailAddress) {
		
		String sql = "INSERT INTO " + tblUsers
			+ " (first_name, last_name, user_name, password, phone_number, email_address) "
			+ "values('" + firstName + "','" + lastName + "','" + username + "','" + password
			+ "','" + phoneNumber + "','" + emailAddress +"')";
		if(this.connectedToDb) {
			try {
				//Execute SQL statement
				int numRec = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				System.out.println("\nSUCCESS..." + numRec + " user added");
				
				//GET THE AUTO-GENERATED USER ID FOR THE NEW USER
				int userId = -1;
				rs = stmt.getGeneratedKeys();
				if(rs.next()) {
					userId = rs.getInt(1);
					return userId;
				}
				else {
					System.out.println("\nERROR: No user_id CREATED!!!");
				}				
			}
			catch(SQLException e) {
				System.out.println("\nERROR: UNABLE TO CREATE USER!!!");
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * Retrieves all communities from the database
	 * @return
	 */
	public List<Community> dbGetCommunitiesByUserId(int userId) {
		/*
		 * SELECT * FROM communities
		 * join user_community
		 * on user_community.community_id = communities.community_id and user_community.user_id = 1;
		 */
		//Prepare the SQL statement
		String sql = "SELECT community_id, community_name, admin_user_id, community_access FROM " + tblCommunities
			+ " JOIN " + tblUserComm
			+ " ON " + tblUserComm + ".user_id = " + tblCommunities + ".community_id AND "
			+ tblUserComm + ".user_id = " + userId;
		if(this.connectedToDb) {
			try {
				//Execute SQL statement and get a result set
				this.rs = stmt.executeQuery(sql);
				
				//List of Community objects to be returned
				List<Community> comms = new ArrayList<Community>();
				
				//Process the result set
				while(this.rs.next()) {
					//int communityIndex, String communityName, User admin, String accessCode
					Community c = new Community();
					
					//Read the fields in the current record and store in Community object
					c.setCommunityIndex(rs.getInt("community_id"));
					c.setCommunityName(rs.getString("community_name"));
					c.setAdminUserId(rs.getInt("admin_user_id"));
					c.setAccess(rs.getString("community_access"));
					
					//Add the Community to the list of user-communities
					comms.add(c);
				}
				
				return comms;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean dbUpdateCommunities(Community comm) {
		String sql = "UPDATE rpsscores.scores SET "
			+ "wins = " + 1
			+ ", losses = " + 1
			+ ", ties = " + 1
			+ " WHERE gameId = 1";
		if(connectedToDb) {
			try {
				stmt.execute(sql);			
				return true;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

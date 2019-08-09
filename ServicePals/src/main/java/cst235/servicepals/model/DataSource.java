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
	public boolean dbCheckUserAlreadyExists(String username) {
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
				System.out.println("\nERROR VALIDATING USERNAME " + username);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * queries either the users or communities table to determine if the name already exists
	 * @param nameToCheck the user or community name to check
	 * @param nameType a character specifying what table to query: 'U' = users, 'C' = communities
	 * @return true if the name already exists; false if not
	 */
	public boolean dbCheckAlreadyExists(String nameToCheck, char nameType) {
		String sql = "";
		switch(Character.toUpperCase(nameType)) {
			case 'C':
				sql = "SELECT community_id FROM " + tblCommunities
					+ " WHERE " + tblCommunities + ".community_name = '" + nameToCheck + "'";
				break;
			case 'U':
				sql = "SELECT user_id FROM " + tblUsers
					+ " WHERE " + tblUsers + ".user_name = '" + nameToCheck + "'";
				break;
			default:
				System.out.println("\nPROGRAM ERROR: INVALID ARGUMENT " + nameType + ". NEED TO PASS 'u', 'U', 'c', or 'C'");
				break;
		}
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
				System.out.println("\nERROR VALIDATING NAME " + nameToCheck);
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Creates a user in the users table
	 * @param firstName the user's first name
	 * @param lastName the user's last name
	 * @param userName the user's username
	 * @param password the user's password
	 * @param emailAddress the user's email address
	 * @return the user_id of the new user 
	 */
	public int dbCreateUser(String firstName, String lastName, String userName,
		String password, String phoneNumber, String emailAddress) {
		
		String sql = "INSERT INTO " + tblUsers
			+ " (first_name, last_name, user_name, password, phone_number, email_address) "
			+ "values('" + firstName + "','" + lastName + "','" + userName + "','" + password
			+ "','" + phoneNumber + "','" + emailAddress +"')";
		return dbInsertIntoTable(sql, userName);
	}
	
	/**
	 * Creates a new community and writes the (admin user id, community_id) into the user-community table
	 * @param commName the community name
	 * @param accessCode the community access code
	 * @param adminUserId the user id of the community administrator
	 * @return the community_id of the new community if successful; -1 if unsuccessful
	 */
	public int dbCreateCommunity(String commName, String accessCode, int adminUserId) {
		String sql = "INSERT INTO " + tblCommunities
			+ " (community_name, community_access, admin_user_id) "
			+ "values('" + commName + "','" + accessCode + "'," + adminUserId + ")";
		return dbInsertIntoTable(sql, commName);
	}
	
	/**
	 * Inserts a user_id, community_id pair into the user-community table
	 * @param userId the user id
	 * @param commId the community id corresponding to the user
	 * @return the auto-generated primary key if successful or -1 if unsuccessful
	 */
	public int dbInsertIntoUserCommunity(int userId, int commId) {
		String sql = "INSERT INTO " + tblUserComm
			+ " (user_id, community_id) "
			+ "values(" + userId + "," + commId + ")";
		return dbInsertIntoTable(sql, userId + "_" + commId);
	}
	
	/**
	 * executes an SQL INSERT into a table that auto-generates a key and returns the key
	 * @param sql the SQL statement
	 * @return the generated key
	 */
	private int dbInsertIntoTable(String sql, String nameToInsert) {
		if(this.connectedToDb) {
			try {
				//Execute SQL statement
				int numRec = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				System.out.println("\nSUCCESS..." + numRec + " record added..." + nameToInsert + " added");
				
				//GET THE AUTO-GENERATED USER ID FOR THE NEW USER
				int key = -1;
				rs = stmt.getGeneratedKeys();
				if(rs.next()) {
					key = rs.getInt(1);
					return key;
				}
				else {
					//System.out.println("\nERROR: No object " + nameToInsert + " inserted!!!");
					return -1;
				}				
			}
			catch(SQLException e) {
				System.out.println("\nERROR: UNABLE TO CREATE " + nameToInsert + "!!!");
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	/**
	 * Retrieves the user information from the users table with user_id = the passed value
	 * @param userIdToRetrieve the userId to retrieve from the database
	 * @return a User object created from the database information
	 */
	public User dbRetrieveUserById(int userIdToRetrieve) {
		User user = null;
		String sql = "SELECT first_name, last_name, user_name, password, email_address FROM " + tblUsers
			+ " WHERE user_id = " + userIdToRetrieve;
		if(this.connectedToDb) {
			try {
				//Execute SQL statement
				rs = stmt.executeQuery(sql);
				if(rs.next()) {
					user = new User(
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("user_name"),
						rs.getString("password"),
						rs.getString("email_address")
					);
					return user;
				}
			}
			catch(SQLException e) {
				System.out.println("\nERROR: UNABLE TO RETRIEVE USER ID = " + userIdToRetrieve + "!!!");
				e.printStackTrace();
			}
		}
		return user;
	}
	
	/**
	 * Retrieves all communities from the database for the user having userId
	 * @return a list of Community objects for the user having userId
	 */
	public List<Community> dbGetCommunitiesByUserId(int userId) {
		/*
		 * SELECT * FROM communities
		 * join user_community
		 * on user_community.community_id = communities.community_id and user_community.user_id = 1;
		 */
		//Prepare the SQL statement
		String sql = "SELECT * FROM " + tblCommunities
				/*community_id, community_name, admin_user_id, community_access*/
			+ " JOIN " + tblUserComm
			+ " ON " + tblUserComm + ".community_id = " + tblCommunities + ".community_id AND "
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
					c.setCommunityIndex(rs.getInt("community_id") - 1);
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

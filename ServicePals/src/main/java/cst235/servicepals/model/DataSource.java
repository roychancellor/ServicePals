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
	private String tblServices = database + ".services";
	private String tblUserComm = database + ".user_community";
	private String tblUserCommService = database + ".user_comm_service";
	public static final boolean EXCLUDE_USER_ID = false;
	public static final boolean INCLUDE_USER_ID = true;

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
		return checkExists(sql);
	}
	
	/**
	 * Checks to see if a combination of userId, communityId, and serviceId already exists in the database to prevent
	 * a user from offering more than one of the same service to the same community
	 * @param userId the integer userId of the user offering the service
	 * @param communityId the community into which the user is offering the service
	 * @param serviceId the service code of the offered service
	 * @return true if the combination already exists; false if not
	 */
	public boolean dbCheckUserCommServiceExists(int userId, int communityId, int serviceId) {
		String sql = "SELECT * FROM " + tblUserCommService 
			+ " WHERE user_id = " + userId
			+ " AND community_id = " + communityId
			+ " AND service_id = " + serviceId;
		return checkExists(sql);
	}
	
	/**
	 * executes a query and looks for a result set with one row or zero rows
	 * @param sql the SQL statement to execute
	 * @return true if the query returned a row; false if not
	 */
	private boolean checkExists(String sql) {
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
				System.out.println("\nERROR VALIDATING OBJECT");
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
		//return the auto-generated key for the record
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
		//return the auto-generated key for the record
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
		//return the auto-generated key for the record
		return dbInsertIntoTable(sql, userId + "_" + commId);
	}
	
	/**
	 * executes an SQL INSERT into a table that auto-generates a key and returns the key
	 * @param sql the SQL statement
	 * @return the generated key
	 */
	private int dbInsertIntoTable(String sql, String recordName) {
		if(this.connectedToDb) {
			try {
				//Execute SQL statement
				int numRec = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				//System.out.println("\nSUCCESS..." + numRec + " record added..." + recordName + " added");
				
				//GET THE AUTO-GENERATED USER ID FOR THE NEW USER
				if(numRec > 0) {
					int key = -1;
					rs = stmt.getGeneratedKeys();
					if(rs.next()) {
						key = rs.getInt(1);
					}
					return key;
				}
			}
			catch(SQLException e) {
				System.out.println("\nERROR: UNABLE TO CREATE " + recordName + "!!!");
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
	 * Retrieves all communities from the database
	 * @param byUser if false, gets all communities; if true, gets communities for the user id
	 * @return a list of Community objects for the user having userId
	 */
	public List<Community> dbRetrieveCommunities() {
		//Prepare the SQL statement
		String sql = "SELECT * FROM " + tblCommunities;

		return getComms(sql);
	}
	
	/**
	 * Overloaded method that retrieves all communities from the database
	 * that either includes the userId or excludes the userId
	 * @param the userId to filter on in the query
	 * @param includeUserId includes the userId in the query if true, excludes if false
	 * @return a list of Community objects for the user having userId
	 */
	public List<Community> dbRetrieveCommunities(int userId, boolean includeUserId) {
		/*
		 * SELECT * FROM communities
		 * join user_community
		 * on user_community.community_id = communities.community_id and user_community.user_id = 1;
		 */
		//Prepare the SQL statement
		String sql = "";
		if(includeUserId) {
			sql = "SELECT * FROM " + tblCommunities
					/*community_id, community_name, admin_user_id, community_access*/
				+ " JOIN " + tblUserComm
				+ " ON " + tblUserComm + ".community_id = " + tblCommunities + ".community_id AND "
				+ tblUserComm + ".user_id = " + userId;
		}
		//Gets communities that DO NOT have the userId as the admin or a member
		else {
			sql = "SELECT * FROM " + tblCommunities
				+ " WHERE (" + tblCommunities + ".admin_user_id != " + userId
				+ " AND " + tblCommunities + ".community_id NOT IN"
				+ " (SELECT community_id FROM " + tblUserComm
				+ " WHERE " + tblUserComm + ".user_id = " + userId + "))";
		}
		return getComms(sql);
	}
	
	/**
	 * Queries the communities database using the sql statement passed in
	 * @param sql the SQL statement to execute
	 * @return a list of Community objects
	 */
	private List<Community> getComms(String sql) {
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
					c.setCommunityId(rs.getInt("community_id"));
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
	
	/**
	 * Retrieves all service providers by communityId
	 * @param communityId the communityId to use for querying all providers
	 * @return ArrayList of ServiceProvider objects
	 */
	public List<ServiceProvider> dbRetrieveProvidersByComm(int communityId) {
		if(this.connectedToDb) {
			String sql = "SELECT"
					+ " service_id, service_description, " + tblUsers + ".phone_number, service_cost"
					+ " FROM " + tblUserCommService
					+ " JOIN " + tblUsers
					+ " ON " + tblUsers + ".user_id = " + tblUserCommService + ".user_id" 
					+ " WHERE " + tblUserCommService + ".community_id = " + communityId;
			try {
				//Execute SQL statement and get a result set
				this.rs = stmt.executeQuery(sql);
				
				//List of ServiceProvider objects to be returned
				List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
				
				//Process the result set
				while(this.rs.next()) {
					//int serviceId, String serviceDescription, String phoneNumber, double servicePrice
					ServiceProvider p = new ServiceProvider();
					
					//Read the fields in the current record and store in Community object
					p.setServiceId(rs.getInt("service_id"));
					p.setServiceDescription(rs.getString("service_description"));
					p.setPhoneNumber(rs.getString("phone_number"));
					p.setServicePrice(rs.getDouble("service_cost"));
					
					//Add the Community to the list of user-communities
					providers.add(p);
				}
				
				return providers;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;		
	}
	
	/**
	 * Retrieves all service ids and service categories from the services table
	 * @return an ArrayList of ServiceProvider objects created from the service categories from the databasse
	 */
	public List<ServiceProvider> dbGetServiceCategories() {
		if(this.connectedToDb) {
			String sql = "SELECT service_id, service_name FROM " + tblServices;
			try {
				//Execute SQL statement and get a result set
				this.rs = stmt.executeQuery(sql);
				
				//List of ServiceProvider objects to be returned
				List<ServiceProvider> categories = new ArrayList<ServiceProvider>();
				
				//Process the result set
				while(this.rs.next()) {
					//int serviceId, String serviceCategory, String serviceDescription, String phoneNumber, double servicePrice
					ServiceProvider p = new ServiceProvider();
					
					//Read the fields in the current record and store in Community object
					p.setServiceId(rs.getInt("service_id"));
					p.setServiceCategory(rs.getString("service_name"));
					
					//Add the Community to the list of user-communities
					categories.add(p);
				}
				
				return categories;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;		
	}
	
	public int dbCreateServiceProvider(int userId, int communityId, ServiceProvider p) {
		String sql = "INSERT INTO " + tblUserCommService
				+ " (user_id, community_id, service_id, service_description, service_cost)" + 
				" VALUES " + 
				"("
				+ userId + ","
				+ communityId + ","
				+ p.getServiceId() + ","
				+ "\"" + p.getServiceDescription() + "\","
				+ p.getServicePrice()
				+ ")";
		//return the auto-generated key for the record
		return dbInsertIntoTable(sql, userId + "_" + communityId + "_" + p.getServiceId());
	}
}

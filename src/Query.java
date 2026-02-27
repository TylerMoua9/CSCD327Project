/*******************************************************
	CSCD 327 RELATIONAL DATABASE SYSTEMS
					Project
			Student Name: ......... ADD YOUR NAME HERE
 *******************************************************/
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.String;
import java.util.Scanner;

public class Query {

	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet result;
	private Scanner scanner = new Scanner(System.in);

	public Query(Connection c) throws SQLException
	{
		conn = c;
	}

	public void query1() throws IOException, SQLException
	{
		// Take user input
		System.out.println("\nEnter the authorID to update:");
		String authorID = scanner.nextLine();
		System.out.println("\nEnter the first name of the author: ");
		String firstName = scanner.nextLine();
		System.out.println("\nEnter the last name of the author: ");
		String lastName = scanner.nextLine();

		// Prepare the SQL statement
		String query  = "update author set lastName = ?, firstName = ? where authorID = ?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setString(1, lastName);
		stmt.setString(2, firstName);
		stmt.setString(3, authorID);

		// Retrieve data with the query
		stmt.executeUpdate();

		System.out.println(firstName + " " + lastName + " with authorID of " + authorID + " was added!");

		/*
		// Print the retrieved data
		System.out.println("\nQuery output:");
		System.out.println("-------------");


		if(!result.next()) {
			System.out.println("No results exist for this input");
			return;
		}

		else {
			do {
				// It is possible to get the columns using the column names or using the column number, which starts at 1. The example below uses both
				String row = result.getString(1) + "\t\t" + result.getString("title") + "\t\t" + result.getString(3) + "\t\t" + result.getString(4) + "\t\t" + result.getString(5);
				System.out.println(row);
			} while (result.next());
		}
		 */
	}

	public void query2() throws IOException, SQLException {

		// Take user input
		System.out.println("\nEnter the authorID to delete:");
		String authorID = scanner.nextLine();

		// Prepare the SQL statement
		String query  = "delete from author where authorID = ?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setString(1, authorID);

		// Retrieve data with the query
		stmt.executeUpdate();

		System.out.println("authorID of " + authorID + " was deleted!");


		/*
		// Take user inputs
		System.out.println("\nEnter the member id:");
		int memberID = scanner.nextInt();
		scanner.nextLine();
		
		System.out.println("\nEnter the member's name:");
		String memberName = scanner.nextLine();
				
		System.out.println("\nEnter the member's city:");
		String city = scanner.nextLine();
		
		System.out.println("\nEnter the joining date:");
		String joinDate = scanner.nextLine();

		// Prepare the SQL statement
		String query  = "insert into Member values (?, ?, ?, ?)";
		stmt = conn.prepareStatement(query);

		// Replace the '?'s in the above statement with the inputs
		stmt.setInt(1, memberID);
		stmt.setString(2, memberName);
		stmt.setString(3, city);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		try { 
			Date joinDateFormatted = dateFormat.parse(joinDate);
			java.sql.Date sqlDate = new java.sql.Date(joinDateFormatted.getTime());
			stmt.setDate(4, sqlDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		};

		

		// Insert data with the query and print status
		if (stmt.executeUpdate() == 1)
			System.out.println("\nData added successfully.");


		 */

	}

	public void query3() throws IOException, SQLException
	{

		// Prepare the SQL statement
		String query  = "select fullName from (select concat(firstName, ' ', lastName) as fullName, count(*) as NUM_BOOKS from author natural join book_author group by authorID) as counted_books where NUM_BOOKS > 1";
		stmt = conn.prepareStatement(query);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nQuery output:");
		System.out.println("-------------");

		if(!result.next()) {
			System.out.println("No results exist for this input");
			return;
		}

		else
			do {
				String row = result.getString(1);
				System.out.println(row);
			} while (result.next());
	}

	public void query4() throws IOException, SQLException {

		// Prepare the SQL statement
		String query = "with avg_shipping as (select concat(firstName, ' ', lastName) as fullName, avg(shipCost) as AVG_SHIPPING_COST from orders natural join customer group by customerID)" +
				"select fullName from avg_shipping where AVG_SHIPPING_COST = (select max(AVG_SHIPPING_COST) from avg_shipping)";

		stmt = conn.prepareStatement(query);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nQuery output:");
		System.out.println("-------------");

		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			do {
				String row = result.getString(1);
				System.out.println(row);
			} while (result.next());
	}

	public void exampleQuery5() throws IOException, SQLException {

		// Take user input
		System.out.println("\nEnter the starting price range:");
		Float startingPrice = scanner.nextFloat();
		System.out.println("\nEnter the ending price range: ");
		Float endingPrice = scanner.nextFloat();

		// Prepare the SQL statement
		String query  = "select title, name from book natural join publisher where price between ? and ?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setFloat(1, startingPrice);
		stmt.setFloat(2, endingPrice);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			System.out.printf("\n%-30s  %-35s%n", "Book Title", "Publisher Name");
			System.out.printf("------------------------------------------------------\n");
			do {
				System.out.printf("%-30s  %-35s%n", result.getString(1), result.getString(2));
			} while (result.next());

	}

	public void exampleQuery6() throws IOException, SQLException {}

	public void exampleQuery7() throws IOException, SQLException {}

	public void exampleQuery8() throws IOException, SQLException {}

	public void exampleQuery9() throws IOException, SQLException {}

	public void exampleQuery10() throws IOException, SQLException {}

}
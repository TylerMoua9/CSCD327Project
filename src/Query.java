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

	public void query5() throws IOException, SQLException {

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

	public void query6() throws IOException, SQLException {

		// Take user input
		System.out.println("\nEnter the orderID to find its total:");
		int orderID = scanner.nextInt();

		// Prepare the SQL statement
		String query  = "select sum(price * quantity) + coalesce(max(shipCost), 0) as TOTAL_COST from items join orders using (orderID) join book using (ISBN) where orderID = ?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setInt(1, orderID);

		// Retrieve data with the query
		result = stmt.executeQuery();

		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			System.out.printf("\nTotal Cost of the Order\n");
		System.out.printf("----------------------\n");
		System.out.printf("$" + result.getString(1) + "\n");
	}

	public void query7() throws IOException, SQLException {

		// Prepare the SQL statement
		String query = "with countRegions as (select region, count(orderID) as COUNT from orders natural join customer group by region) select region from countRegions where count = (select max(COUNT) from countRegions)";

		stmt = conn.prepareStatement(query);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nRegion(s) with the greatest amount of orders:");
		System.out.println("---------------------------------------------");

		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			do {
				String row = result.getString(1);
				System.out.println(row);
			} while (result.next());

	}

	public void query8() throws IOException, SQLException {

		String[] name;
		String fullName;
		// Take user input
		do {
			System.out.println("\nEnter the full name of the customer in the form <First_Name> <Last_Name>: ");
			fullName = scanner.nextLine().trim();
			name = fullName.split(" ");
		} while (name.length != 2);

		// Prepare the SQL statement
		String query  = "with findID as (select orderID from customer natural join orders where firstName = ? and lastName = ?)\n" +
				"select sum(TOTAL_COST) as TOTAL_FROM_ALL_ORDERS \n" +
				"from (SELECT \n" +
				"    SUM(price * quantity) + COALESCE(MAX(shipCost), 0) AS TOTAL_COST\n" +
				"FROM\n" +
				"    items\n" +
				"        JOIN\n" +
				"    orders USING (orderID)\n" +
				"        JOIN\n" +
				"    book USING (ISBN) \n" +
				"\t\tJOIN\n" +
				"\tfindID USING (orderID)\n" +
				"WHERE\n" +
				"    orderID = findID.orderID) as Total_Order_Cost;";

		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setString(1, name[0]);
		stmt.setString(2, name[1]);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nTotal Cost of All Orders by " + fullName + ":");
		System.out.println("---------------------------------------------");

		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			do {
				String row = result.getString(1);
				System.out.println(row);
			} while (result.next());
	}

	public void query9() throws IOException, SQLException {

		// Prepare the SQL statement
		String query = "select distinct category, category_average_price(category) as AVERAGE_CATEGORY_PRICE from book order by category asc";

		stmt = conn.prepareStatement(query);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nAverage Price of Books in each Category:");
		System.out.println("---------------------------------------------");

		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			System.out.printf("\n%-30s  %-35s%n", "Category", "Average Price of Books");
		System.out.printf("------------------------------------------------------\n");
		do {
			System.out.printf("%-30s  $%-35s%n", result.getString(1), result.getString(2));
		} while (result.next());


	}

	public void query10() throws IOException, SQLException {

		// Prepare the SQL statement
		String query = "call find_coauthors()";

		stmt = conn.prepareStatement(query);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		if (!result.next()) {
			System.out.println("No results exist for this input");
			return;
		} else
			System.out.printf("\n%-20s  %-20s  %-20s%n", "ISBN", "Author1", "Author2");
		System.out.printf("------------------------------------------------------\n");
		do {
			System.out.printf("%-20s  %-20s  %-20s%n",  result.getString(1), result.getString(2), result.getString(3));
		} while (result.next());

	}

}
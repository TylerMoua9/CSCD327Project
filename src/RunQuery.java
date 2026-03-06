/*******************************************************
	CSCD 327 RELATIONAL DATABASE SYSTEMS
					Project
			Student Name: ......... Tyler Moua
 *******************************************************/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Scanner;

public class RunQuery {

	public static void main(String[] args) {

		int queryNumber;
		String run = "1";
		Scanner scanner = new Scanner(System.in);

		try {

			Connection conn = getConnection();
			Query myquery = new Query(conn);

			System.out.println("Welcome!!!");

			while (!run.equals("0")) {

				System.out.println("\nChoose an option from the following Queries:");
				System.out.println("1. Update an author's name");
				System.out.println("2. Delete an author");
				System.out.println("3. List the names of the authors who have written multiple books");
				System.out.println("4. List the names of the customers whose orders incurred the highest average shipping cost");
				System.out.println("5. List the book titles in a specific price range with their publishers' names");
				System.out.println("6. Find the total order value for an input order ID (including shipping cost)");
				System.out.println("7. Find the regions from which the maximum number of orders have been placed");
				System.out.println("8. Find the total price of books across all orders placed by a given customer");
				System.out.println("9. List the average price of books in each category in ascending order of category");
				System.out.println("10. Find all pairs of author names who have coauthored a book");
				System.out.println();
				
				queryNumber = scanner.nextInt();

				switch (queryNumber) {

					case 1:
						myquery.query1();
						break;

					case 2:
						myquery.query2();
						break;

					case 3:
						myquery.query3();
						break;

					case 4:
						myquery.query4();
						break;

					case 5:
						myquery.query5();
						break;

					case 6:
						myquery.query6();
						break;

					case 7:
						myquery.query7();
						break;

					case 8:
						myquery.query8();
						break;

					case 9:
						myquery.query9();
						break;

					case 10:
						myquery.query10();
						break;

					default:
						System.out.println("\nInvalid choice...");
				}

				System.out.println("\nEnter anything to CONTINUE, enter 0 to QUIT");
				scanner.nextLine();
				run = scanner.nextLine().trim();
			}
			
			System.out.println("\nExiting...Bye!");
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();
	}

	public static Connection getConnection() throws SQLException {
		
		// Create connection to the database
		Connection connection;
		String serverName = "10.219.0.50:3306";
		String database = "w26tmoua9_projectDB";
		String url = "jdbc:mysql://" + serverName + "/" + database;
		String username = "w26tmoua9";
		String password = ""; 		// THE PASSWORD HAS BEEN CHANGED
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
}
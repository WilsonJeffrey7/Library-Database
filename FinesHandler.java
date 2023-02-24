import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FinesHandler
{
	
	//This method can be called to update the fines manually
	public static String RefreshFines()
	{
		if(App.conn==null)
		{
			System.out.println("Connection not established");
			return "Connection not established";
		}
		
		try
		{
			Statement stmnt = App.conn.createStatement();
			Statement stmnt2 = App.conn.createStatement();
			
			// Inner join loans and fines
			String command = "SELECT BOOK_LOANS.Loan_id, BOOK_LOANS.Date_in, BOOK_LOANS.Due_Date FROM BOOK_LOANS";
			
			ResultSet rs = stmnt.executeQuery(command);
			
			while (rs.next())
			{
				System.out.println("test");
				String identity = rs.getString("Loan_id");
				Date day_in = rs.getDate("Date_in");
				Date day_due = rs.getDate("Due_Date");
				double fine = 0.0;
				
				// String scommand = "SELECT FINES.Paid FROM FINES WHERE FINES.Loan_id = '" + identity + "'";
				
				// ResultSet srs = stmnt.executeQuery(scommand);
				// srs.next();
				
				// Boolean paid = srs.getBoolean("Paid");
				Boolean paid = false;
				
				if(!paid)
				{
				
					if(day_in == null)
					{
						day_in = new Date();
					}
					
					day_in = new Date(day_in.getTime()+(1209600000 * 2));

					long diff = TimeUnit.DAYS.convert((Math.abs(day_in.getTime()-day_due.getTime())), TimeUnit.MILLISECONDS);
					
					if(diff > 0)
					{
						fine = diff * 0.25;
					}
					else
					{
						fine = 0;
					}
					
					
					String inCommand = "UPDATE FINES SET FINES.Fine_amt = '" + Double.toString(fine) + "' WHERE FINES.Loan_id = '" + identity + "'";
					
					System.out.println(identity);

					stmnt2.executeUpdate(inCommand);
					
				}

			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return e.toString();
		}
		
		return "Fines updated successfully!";
	}
	
	//This method takes a string for a Card_Id and pays the fine for that Card_Id if the book has been returned
	public static String PayFines(String user)
	{
		if(App.conn==null)
		{
			System.out.println("Connection not established");
			return "Connection not established";
		}
		
		try
		{
			Statement stmnt = App.conn.createStatement();

			Statement stmnt2 = App.conn.createStatement();
			
			String command = "SELECT Card_id FROM BORROWER WHERE BORROWER.Card_id = '" + user + "'";
			
			ResultSet rs1 = stmnt.executeQuery(command);
			
			if(rs1.next() == false)
			{
				System.out.println("ERROR: Borrower does not exist");
				return "ERROR: Borrower does not exist";
			}
			else
			{
				String getCommand = "SELECT FINES.Loan_id, BOOK_LOANS.Date_in FROM FINES JOIN BOOK_LOANS ON FINES.Loan_id = BOOK_LOANS.Loan_id WHERE BOOK_LOANS.Card_id = '" + user + "'";
				ResultSet rs2 = stmnt2.executeQuery(getCommand);
				
				while(rs2.next())
				{
					if(rs2.getDate("Date_in") == null)
					{
						return "Fine cannot be paid, book not yet returned";
					}
					else
					{
						String changeCommand = "UPDATE FINES SET FINES.Paid = 1 WHERE FINES.Loan_id = '" + rs2.getString("Loan_Id") + "'";
						stmnt.executeUpdate(changeCommand);
					}
				}
				return "Fines paid successfully!";
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return e.toString();
		}
	}
	
	//This method returns a double with the total amount owed by the inputed Card_id
	public static Double GetFines(String user)
	{
		double totalFine = 0;
		
		if(App.conn==null)
		{
			System.out.println("Connection not established");
			return null;
		}
		
		try
		{
			Statement stmnt = App.conn.createStatement();
			
			String command = "SELECT Card_id FROM BORROWER WHERE BORROWER.Card_id = '" + user + "'";
			
			ResultSet rs1 = stmnt.executeQuery(command);
			
			if(rs1.next() == false)
			{
				System.out.println("ERROR: Borrower does not exist");
				return null;
			}
			else
			{
				String getCommand = "SELECT FINES.Fine_amt, FINES.Paid FROM FINES JOIN BOOK_LOANS ON FINES.Loan_id = BOOK_LOANS.Loan_id WHERE BOOK_LOANS.Card_id = '" + user + "'";
				ResultSet rs2 = stmnt.executeQuery(getCommand);
				
				while(rs2.next())
				{
					if(rs2.getBoolean("Paid") == false)
					{
						totalFine += rs2.getDouble("Fine_amt");
					}
				}
				return totalFine;
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//This method returns a LinkedList, one element for each Card_id. Each element in the list is a string that says how much the Card_id owes.
	public static LinkedList<String[]> DisplayFines()
	{
		LinkedList<String[]> list = new LinkedList<String[]>();
		
		if(App.conn==null)
		{
			System.out.println("Connection not established");
			return null;
		}
		
		try
		{
			Statement stmnt = App.conn.createStatement();
			
			String command = "SELECT Card_id FROM BORROWER";
			
			ResultSet rs1 = stmnt.executeQuery(command);
			
			while (rs1.next())
			{
				String card = rs1.getString("Card_id");
				// String[] entry = "Card_id = " + card + " - Fines: $ " + String.format("%.2f",GetFines(card));
				String[] entry = {card, Double.toString(GetFines(card))};
				list.add(entry);
			}
			
			return list;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

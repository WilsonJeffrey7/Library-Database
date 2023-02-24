import java.sql.*;
import java.util.*;

public class BorrowerManagement {

	
	
	public static String GenerateId()
	{
		String id = null;
		
		try {
			Statement stmnt = App.conn.createStatement();
			
			String idQuery = "SELECT MAX(CAST(SUBSTRING(card_id, 3, LENGTH(card_id)-2) AS UNSIGNED)) FROM Borrower";
			System.out.println(idQuery);
			ResultSet rs = stmnt.executeQuery(idQuery);			
			String maxId = null;

			if(rs.next())
			{
				System.out.println(maxId);
				maxId = rs.getString(1);				
				int max = Integer.parseInt(maxId);
				System.out.println(maxId);
				System.out.println(max);
				
				if(max >= 999999)
				{
					System.out.println("Borrower ID overflow");
					return null;
				}
				
				max += 1;
				
				// int tempMax = max;
				// int digitCount = 0;
				// while(tempMax >= 0)
				// {
				// 	tempMax /= 10;
				// 	digitCount++;
				// }
				
				id = "ID";
				
				// for(int i = 0; i < 6 - digitCount; i++)
				// {
				// 	id += "0";
				// }
				
				id += String.format("%06d", max);				
			}
			
		} catch (SQLException e) {
			System.out.println("Failed to generate ID");
			e.printStackTrace();
		}
		
		return id;
	}
	
	//return true if successful
	public static String CreateBorrower(String ssn, String first_name, String last_name, String email, String address, String city, String state, String phone)
	{
		if(App.conn == null)
		{
			return "Connection not established";
		}

		System.out.println(ssn + ", " + first_name + ", " + last_name + ", " + email + ", " + address + ", " + city + ", " + state + ", " + phone);
		
		if(ssn.equals(null) || ssn.equals("") 
				|| first_name.equals(null) || first_name.equals("")
				|| last_name.equals(null) || last_name.equals("")
				|| email.equals(null) || email.equals("")
				|| address.equals(null) || address.equals("")
				|| city.equals(null) || city.equals("")
				|| state.equals(null) || state.equals("")
				|| phone.equals(null) || phone.equals(""))
		{
			return "Null element detected";
		}
		
		try
		{
			Statement stmnt = App.conn.createStatement();
			
			String ssnCheck = "SELECT * FROM Borrower WHERE Borrower.ssn = '" + ssn + "';";
			
			ResultSet rs = stmnt.executeQuery(ssnCheck);
			
			if(rs.next() == true)
			{
				return "A borrower with this ssn already exists.";
			}
			
			String id = "";
			
			id = GenerateId();
			
			String insertObj = "";			
			String[] elements = (new String[] {id, ssn, first_name, last_name, email, address, city, state, phone});
			
			for(int i = 0; i < elements.length; i++)
            {                
                if(i == elements.length - 1)
                {
                    insertObj += "'" + elements[i] + "'";
                }
                else
                {
                    insertObj += "'" + elements[i] + "', ";
                }
            }
            
            String command = "INSERT INTO Borrower (card_id, ssn, first_name, last_name, email, address, city, state, phone) VALUES (" + insertObj + ");";
			stmnt.executeUpdate(command);
			
			return "Borrower added successfully!";
			// if(stmnt.execute(command))
			// {
			// 	return "Borrower added successfully!";
			// }
			// return "Failed to add borrower, please try again.";
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
	}
	
}

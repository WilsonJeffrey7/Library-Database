import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class bookLoans {

    public static int LoanID = 0;


    public static String CheckOut(String borrowerID, long isbn) {
        if (App.conn == null) {
            System.out.println("Connection failed");
            return "Connection failed";
        }
        
        try {
            Statement stmnt = App.conn.createStatement();
            String command = "SELECT EXISTS(SELECT card_id FROM BORROWER WHERE BORROWER.card_id = '" + borrowerID + "') AS brwexists";
            ResultSet rs1 = stmnt.executeQuery(command);
            
            if (rs1 == null) {
                System.out.println("Invalid borrower");
                return "Invalid borrower";
            } else {
                // check borrower exists
                rs1.next();
                int borrowerexists = rs1.getInt("brwexists");
                if (borrowerexists == 0) {
                    System.out.println("Borrower does not exist");
                    return "Borrower does not exist";
                }
                
            } 
            
            int count = 0;
            Statement stmntl = App.conn.createStatement();
            String commandl = "SELECT COUNT(*) AS bklcount FROM BOOK_LOANS WHERE BOOK_LOANS.card_id = '" + borrowerID + "' AND BOOK_LOANS.date_in IS NULL";
            ResultSet rs2 = stmntl.executeQuery(commandl);
            if (rs2 != null) {
                rs2.next();
                count = rs2.getInt("bklcount");
                rs2.close();
            }
            
            System.out.println("Number of books borrowed: %s".formatted(count));
            if (count >= 3) {
                System.out.println("Loan limit reached!");
                return "Loan limit reached!";

            } else {
                // Query to check out, time, etc.
                Date currentDate = new Date();
                Date returnDate = new Date(currentDate.getTime()+1209600000);
                Statement stmntd = App.conn.createStatement();
                Statement stmnte = App.conn.createStatement();
                String commandd = "INSERT INTO BOOK_LOANS (loan_id, isbn, card_id, date_out, due_date, date_in) VALUES ('" + LoanID + "','"+ isbn + "', '" + borrowerID + "', '" + ((currentDate).getYear()+1900) + "-" + ((currentDate).getMonth()+1) + "-" + (currentDate).getDate() + "', '" + ((returnDate).getYear()+1900) + "-" + ((returnDate).getMonth()+1) + "-" + (returnDate).getDate() + "', NULL)";
                stmntd.executeUpdate(commandd);
                String commande = "INSERT INTO FINES (loan_id, fine_amt, paid) VALUES ('" + LoanID + "', '" + 0 + "', '" + 0 + "')";
                LoanID++;
                stmnte.executeUpdate(commande);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return "Checkout successful!\nISBN: " + Long.toString(isbn) + "\nBorrower ID: " + borrowerID;
    } 

    public static ResultSet CheckInSearch(String search) {
        if (App.conn == null) {
            System.out.println("Connection failed");
            return null;
        }

        try {
            Statement stmnt = App.conn.createStatement();
            String command1 = "SELECT BOOK_LOANS.Loan_Id, BOOK_LOANS.isbn, BOOK_LOANS.Card_id, BOOK.title, BOOK_LOANS.Date_in from BOOK_LOANS join BORROWER on BOOK_LOANS.Card_id = BORROWER.Card_id join BOOK on BOOK_LOANS.isbn = BOOK.isbn where BOOK_LOANS.isbn like concat('%', '" + search + "', '%') or BORROWER.First_name like concat('%', '" + search + "', '%') or BORROWER.Last_name like concat('%', '" + search + "', '%') or BOOK_LOANS.Card_id like concat('%', '" + search + "', '%')";
            ResultSet rs1 = stmnt.executeQuery(command1);
            return rs1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String CheckIn(int loan_id) {
        if (App.conn == null) {
            System.out.println("Connection failed");
            return "Connection failed";
        }

        try {
            Date currentDate = new Date();
            Statement stmnt = App.conn.createStatement();
            String command = "SELECT BOOK_LOANS.Date_in FROM BOOK_LOANS WHERE BOOK_LOANS.Loan_Id = '" + loan_id + "'";
            ResultSet rs1 = stmnt.executeQuery(command);
            rs1.next();

            if (rs1.getDate("date_in") == null) {
                Statement stmt = App.conn.createStatement();
                String commandl = "UPDATE BOOK_LOANS SET BOOK_LOANS.Date_in = '" + ((currentDate).getYear()+1900) + "-" + ((currentDate).getMonth()+1) + "-" + (currentDate).getDate() + "' WHERE BOOK_LOANS.Loan_id = '" + loan_id + "'";
                stmt.executeUpdate(commandl);
                
                System.out.println("The book is now checked in!");

            } else {
                return "Failed to check in!";
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return e.toString();
        }

        return "The book is now checked in!";
    }



}
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Search {

    public static ResultSet searchresult = null;

    public static void search(String title) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");        

            //this is the sql query
            // Add the search gui in between Like '%Name%' then it should search that name
            String query = "\n" +
                    "SELECT binfo.title, auth.author_name, BA.isbn  FROM databaselibrary.authors As auth inner Join book_authors As BA on auth.author_id = BA.author_id inner Join book As binfo on binfo.isbn = BA.isbn left outer join book_loans As bl on bl.isbn = binfo.isbn WHERE binfo.title LIKE '%"+ title +"%' OR auth.author_name LIKE '%"+ title +"%' OR binfo.isbn LIKE '%"+ title + "%'";

            String name = null;


            // create the java statement
            Statement statement = App.conn.createStatement();

            // execute the query, and get a java resultset
            searchresult = statement.executeQuery(query);





            // iterate through the java resultset
            // while (searchresult.next()) {


            //     long isbn = searchresult.getLong("isbn");
            //     String author_name = searchresult.getString("author_name");
            //     String input = searchresult.getString("title");
            //     boolean available = getAvailability();

            //     // print the results
            //     System.out.format("%s  , %s , %s\n", isbn, author_name, input);
            // }
            // statement.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


    }

    public static boolean getAvailability() throws SQLException {
        if (hasColumn(searchresult, "date_in")) {
            if (searchresult.getDate("date_in") == null) {
                return true;
            }else{
                return false;
            }
        } else {
            return true;
        }
        
    }   
    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}

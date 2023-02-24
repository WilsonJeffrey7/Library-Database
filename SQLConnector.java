/**
 *  NAME:
 *      CompanyDBExample.java
 *  AUTHOR:
 *      Chris Irwin Davis
 *      chrisirwindavis@utdallas.edu
 *      The Univeristy of Text at Dallas
 *  DATE:
 *      2016 JAN 08
 *  DESCRIPTION:
 *      This Java stub code is an example of how to connect to,
 *      query, and manipulate a MySQL database. This example is
 *      designed to work with the COMPANY database from the textbook
 *      "Fundamentals of Databse Design, 7/E" by Elmasri and Navathe.       
 *  EDITS:
 *      2018 JAN 30
 *      2020 APR 07
 *      2022 OCT 01
 */


import java.sql.*;

public class SQLConnector {
    static Connection conn = null;
        /**
         * @param args
         */
        public static Connection init(String database) {
        	/*
			 *  Initialize Java variables by data type
        	 *  Each one of these corresponds to a table column.
        	 *  They don't have to be spelled the same or even
        	 *  be the same data type.
        	 */
			String ssn;
			String firstName;
			String mInit;
			String lastName;
			String address;
			String salary;
			String superSsn;
			String dno;
			/*
			 *  The Java dno variable may also be type int
			 *  Note that the SQL data type can be different
			 *  than the wrapper language data type –– Java
			 *  in this case.
			 */
			// int dno; 

			/* Variables for the database connection */
			String databaseName = database;
        	String url;
        	String userName;
        	String passWord;

			
			
			// To be able to display line numbers in a result set
			int rowCount = 0;

            System.out.println("Starting...\n");

            try {
            	/*
            	 *  If databaseName is an empty string, you can still connect,
            	 *  but you will have to execute "USE [databaseName];" before
            	 *  working with a specific database.
            	 */
            	url = "jdbc:mysql://localhost:3306/" + databaseName;
            	userName = "wilsonjeffrey7"; /* Use whatever user account you prefer */
            	passWord = "sqldatabase";     /* Include the password for the account of the previous line. */

                /* Create a connection to the local MySQL server, with the "company" database selected. */
            	conn = DriverManager.getConnection(url,userName,passWord);
            	
            	return conn;             
            }
            catch(SQLException ex) {
                System.out.println("ERROR: Cannot connect to database: " + databaseName);
                System.out.println(ex.getMessage());
                return null;
            }
		}
}
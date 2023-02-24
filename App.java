import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.sql.*;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.AbstractTableModel;

public class App {
    public static Connection conn;
    static public JFrame f = new JFrame();
    static JLabel title = new JLabel("Library Database");
    static Font titleFont = new Font("title", Font.BOLD, 20);

    static JTabbedPane tabbedPane = new JTabbedPane();

    public static void main(String[] args) throws Exception {
        conn = SQLConnector.init("databaselibrary");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        tabbedPane.addTab("Book Search", createBookSearch());
        tabbedPane.addTab("Loan Search", createLoanSearch());
        tabbedPane.addTab("Manage Borrowers", createAddBorrowers());
        tabbedPane.addTab("Manage Fines", createManageFines());

        title.setFont(titleFont);
        f.add(tabbedPane);

        f.setSize(450, 550);
        f.setVisible(true);
    }

    public static JPanel createBookSearch() {
        JPanel bookSearch = new JPanel();
        JButton searchButton = new JButton("Search Books");
        JButton checkoutButton = new JButton("Checkout Book");
        JTextField bookSearchBar = new JTextField(20);
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if(selectedRow != -1) {
                    long isbn = (long)table.getModel().getValueAt(selectedRow, 0);

                    String borrowerID = JOptionPane.showInputDialog(bookSearch, "Please enter Borrower ID: ");

                    String responseMessage = bookLoans.CheckOut(borrowerID, isbn);

                    JOptionPane.showMessageDialog(bookSearch, responseMessage);
                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(bookSearchBar.getText());

                Search.search(bookSearchBar.getText());

                bookSearch.remove(scrollPane);
                bookSearch.remove(checkoutButton);

                SearchTableModel model = new SearchTableModel();

                table.setModel(model);

                bookSearch.add(scrollPane);
                bookSearch.add(checkoutButton);
                f.pack();
            }
        });

        bookSearch.setLayout(new BoxLayout(bookSearch, BoxLayout.Y_AXIS));

        JPanel searchArea = new JPanel();
        searchArea.add(searchButton);
        searchArea.add(bookSearchBar);
        bookSearch.add(searchArea);

        f.pack();
        return bookSearch;
    }

    public static JPanel createLoanSearch() {
        JPanel loanSearch = new JPanel();
        JButton searchButton = new JButton("Search Loans");
        JTextField loanSearchBar = new JTextField(20);
        JButton checkinButton = new JButton("Check In Book");
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        checkinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if(selectedRow != -1) {
                    int bookID = (int)table.getModel().getValueAt(selectedRow, 0);

                    String responseMessage = bookLoans.CheckIn(bookID);

                    JOptionPane.showMessageDialog(loanSearch, responseMessage);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(loanSearchBar.getText());

                loanSearch.remove(scrollPane);
                loanSearch.remove(checkinButton);

                LoanTableModel model = new LoanTableModel(loanSearchBar.getText());

                table.setModel(model);

                loanSearch.add(scrollPane);
                loanSearch.add(checkinButton);
                f.pack();
            }
        });

        loanSearch.setLayout(new BoxLayout(loanSearch, BoxLayout.Y_AXIS));

        JPanel searchArea = new JPanel();

        searchArea.add(searchButton);
        searchArea.add(loanSearchBar);
        loanSearch.add(searchArea);

        f.pack();
        return loanSearch;
    }

    public static JPanel createAddBorrowers() {
        JPanel addBorrowers = new JPanel();
        
        JTextField fName = new JTextField(20);
        JTextField lName = new JTextField(20);
        JTextField ssn = new JTextField(20);
        JTextField address = new JTextField(20);
        JTextField city = new JTextField(20);
        JTextField state = new JTextField(20);
        JTextField phoneNum = new JTextField(20);
        JTextField email = new JTextField(20);

        JLabel fNameLabel = new JLabel("First Name:");
        JLabel lNameLabel = new JLabel("Last Name:");
        JLabel ssnLabel = new JLabel("SSN:");
        JLabel addressLabel = new JLabel("Street Address:");
        JLabel cityLabel = new JLabel("City:");
        JLabel stateLabel = new JLabel("State:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneNumLabel = new JLabel("Phone Number:");

        JButton addBorrowerButton = new JButton("Add New Borrower");
        addBorrowerButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        addBorrowerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String responseMessage = BorrowerManagement.CreateBorrower(ssn.getText(), fName.getText(), lName.getText(), email.getText(), address.getText(), city.getText(), state.getText(), phoneNum.getText());

                JOptionPane.showMessageDialog(addBorrowers, responseMessage);
            }
        });

        JPanel fNamePanel = new JPanel();
        JPanel lNamePanel = new JPanel();
        JPanel ssnPanel = new JPanel();
        JPanel addressPanel = new JPanel();
        JPanel cityPanel = new JPanel();
        JPanel statePanel = new JPanel();
        JPanel emailPanel = new JPanel();
        JPanel phoneNumPanel = new JPanel();

        fNamePanel.add(fNameLabel);
        fNamePanel.add(fName);
        lNamePanel.add(lNameLabel);
        lNamePanel.add(lName);
        ssnPanel.add(ssnLabel);
        ssnPanel.add(ssn);
        addressPanel.add(addressLabel);
        addressPanel.add(address);
        cityPanel.add(cityLabel);
        cityPanel.add(city);
        statePanel.add(stateLabel);
        statePanel.add(state);
        phoneNumPanel.add(phoneNumLabel);
        phoneNumPanel.add(phoneNum);
        emailPanel.add(emailLabel);
        emailPanel.add(email);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setMaximumSize(new Dimension(1000, 350));
        centerPanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);

        centerPanel.add(fNamePanel);
        centerPanel.add(lNamePanel);
        centerPanel.add(addressPanel);
        centerPanel.add(cityPanel);
        centerPanel.add(statePanel);
        centerPanel.add(emailPanel);
        centerPanel.add(phoneNumPanel);
        centerPanel.add(ssnPanel);
        centerPanel.add(addBorrowerButton);

        addBorrowers.setLayout(new BoxLayout(addBorrowers, BoxLayout.Y_AXIS));
        addBorrowers.add(centerPanel);

        return addBorrowers;
    }

    public static JPanel createManageFines() {
        JPanel manageFines = new JPanel();
        JButton updateFinesButton = new JButton("Update Fines");
        JButton markFinePaidButton = new JButton("Mark Fine as Paid");
        JTable table = new JTable(new FinesTableModel());
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        updateFinesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update Fines");

                String updateMessage = FinesHandler.RefreshFines();
                table.setModel(new FinesTableModel());
                table.repaint();
                JOptionPane.showMessageDialog(manageFines, updateMessage);
            }
        });

        markFinePaidButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Mark Fine as Paid");

                int selectedRow = table.getSelectedRow();
                    
                if(selectedRow != -1) {
                    String cardID = (String)table.getModel().getValueAt(selectedRow, 0);

                    String responseMessage = FinesHandler.PayFines(cardID);

                    JOptionPane.showMessageDialog(manageFines, responseMessage);
                }
            }
        });

        manageFines.setLayout(new BoxLayout(manageFines, BoxLayout.Y_AXIS));

        JPanel buttonArea = new JPanel();

        buttonArea.add(updateFinesButton);
        buttonArea.add(markFinePaidButton);
        manageFines.add(buttonArea);
        manageFines.add(scrollPane);

        f.pack();
        return manageFines;
    }

    public static void showPopup(String message) {
        JFrame popup = new JFrame();

        popup.setSize(200, 100);
        popup.setResizable(false);
        popup.add(new JLabel(message));
        popup.setVisible(true);
    }
}

class SearchTableModel extends AbstractTableModel {
    String[] columnNames = {"ISBN", "Author", "Title", "Availability"};
    ArrayList<Object[]> data = new ArrayList<Object[]>();

    public SearchTableModel() {
        try {
            while (Search.searchresult.next()) {
                long isbn = Search.searchresult.getLong("isbn");
                String author_name = Search.searchresult.getString("author_name");
                String title = Search.searchresult.getString("title");
                boolean available = Search.getAvailability();

                Object[] row = {isbn,author_name,title,available};

                data.add(row);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

class LoanTableModel extends AbstractTableModel {
    String[] columnNames = {"Loan ID", "ISBN", "Card ID"};
    ArrayList<Object[]> data = new ArrayList<Object[]>();

    public LoanTableModel(String search) {
        try {
            ResultSet loans = bookLoans.CheckInSearch(search);
            while (loans.next()) {
                int loanID = loans.getInt("Loan_id");
                long isbn = loans.getLong("isbn");
                String cardID = loans.getString("Card_id");

                Object[] row = {loanID,isbn,cardID};

                data.add(row);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

class FinesTableModel extends AbstractTableModel {
    String[] columnNames = {"Card ID", "Fine Amount"};
    ArrayList<Object[]> data = new ArrayList<Object[]>();

    public FinesTableModel() {
        try {
            LinkedList<String[]> fines = FinesHandler.DisplayFines();
            for(String[] s : fines) {
                data.add(s);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}

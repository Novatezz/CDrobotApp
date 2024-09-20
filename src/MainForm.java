import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.net.*;
import java.io.*;

public class MainForm extends JFrame implements ActionListener {
    /**
     * Initial variables for main form to use
     * UI components (JTextField,label,button)
     * import FileManager class (file)
     * set variables for various objects (gridColumn/Row, dangerous, concerning, acceptable,array to hold levels)
     */
    SpringLayout layout = new SpringLayout();

    JLabel lblTitle, lblSearch,lblSort, lblItemTitle, lblAuthor,lblSection,lblXPos,lblYPos,lblBarcode,
            lblDescription,lblProcessLog,lblBinary,lblHashMap,lblFindBarcode,lblAutoTitle,lblAutoSearch, lblMessage;

    JTextField txtSearch, txtTitle, txtAuthor, txtSection, txtXPos, txtYPos, txtBarcode, txtFindBarcode,
            txtAutoSearch,txtID;

    JButton btnSearch, btnSortTitle, btnSortAuthor, btnSortBarcode,btnProcessLog, btnNewItem, btnItemSave,btnBPreorder,
            btnBInorder,btnBPostorder,btnBGraphical, btnHashSave, btnHashDisplay, btnAutoRetrieve,btnAutoRemove,
            btnAutoReturn,btnAutoAdd,btnAutoRandomSort,btnAutoMostSort,btnAutoReverseSort, btnExit/*,btnConnect*/;

    JTable tblArchived = new JTable();

    JTextArea txaProcessLog, txaDescription;

    FileManager file = new FileManager( );
    FileData tableData = file.ReadFromCSV("E:\\Java2\\CDrobot\\src\\CD_ArchivePrototype_SampleData.txt");
    public static String[] header;


    int selectedItem = 0;
    boolean newMode = true;
    boolean searchMode = false;
    String SortDirection = "dec";
    DList dList = new DList();
    BinaryTree theTree = new BinaryTree();

    //chat
    private Socket socket = null;
    //private final DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private MainFormThread1 client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    //chat


    /**
     * Main form Constructor
     */
    public MainForm()
    {
        setTitle("CD Robot Control System");
        setLayout(layout);
        setSize(1000,625);
        setResizable(false);
        //sets background colour of the window
        this.getContentPane().setBackground(Color.decode("#599467"));
        //listener to end application when closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Method call to build UI components (labels, text fields, buttons and tables)
        BuildUIComponents();

        //set location of window in the middle of the screen
        setLocationRelativeTo(null);

        setLocation(this.getX() - 300,this.getY()-180);

        //CHAT RELATED ---------------------------
        getParameters();
        connect(serverName, serverPort);
        //----------------------------------------

        autoButtonEnabled(false);
        autoSortEnabled(false);
        binaryButtonsEnabled(false);
        header = tableData.header;

        //Show Window to user
        setVisible(true);
    }

    private void autoButtonEnabled(boolean state) {
        btnAutoAdd.setEnabled(state);
        btnAutoReturn.setEnabled(state);
        btnAutoRemove.setEnabled(state);
        btnAutoRetrieve.setEnabled(state);
    }
    private void autoSortEnabled(boolean state) {
        btnAutoReverseSort.setEnabled(state);
        btnAutoMostSort.setEnabled(state);
        btnAutoRandomSort.setEnabled(state);
    }
    private void binaryButtonsEnabled(boolean state) {
        btnBGraphical.setEnabled(state);
        btnBPostorder.setEnabled(state);
        btnBInorder.setEnabled(state);
        btnBPreorder.setEnabled(state);
    }

    //method to build UI components on screen
    private void BuildUIComponents()
    {
        //Builds a title bar at top of screen
        lblTitle = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor(" Archive Console", 0, 1, layout, this);
        lblTitle.setFont(new Font("Courier", Font.BOLD, 25));
        lblTitle.setPreferredSize(new Dimension(1000, 40));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Color.decode("#365c3f"));
        lblTitle.setForeground(Color.white);
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblTitle);

        //Adds a search bar under the title to use for searching the table below for keywords
        //label
        lblSearch = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor(" Search String: ", 15, 55, layout, this);
        lblSearch.setPreferredSize(new Dimension(100, 25));
        lblSearch.setOpaque(true);
        lblSearch.setForeground(Color.decode("#365c3f"));
        lblSearch.setBorder(new LineBorder(Color.gray, 1));
        add(lblSearch);
        //text box
        txtSearch = UIBuilderLibrary.BuildJTextFieldInlineToRight(8, 0, layout, lblSearch);
        txtSearch.setPreferredSize(new Dimension(85, 25));
        add(txtSearch);
        //button
        btnSearch = UIBuilderLibrary.BuildJButtonInlineToRight(85, 25, "Search", 5, this, layout, txtSearch);
        add(btnSearch);

        //Connect button => anchored off the search button
//        btnConnect = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Connect", 220, this, layout, btnSearch);
//        add(btnConnect);

        //Method to initialise and build the table with data inside a scroll pane
        JScrollPane scrollTbl = BuildMainTable();

        //Sort buttons positioned below the table of data
        lblSort = UIBuilderLibrary.BuildJLabelInlineBelow("Sort: ", 5, layout, scrollTbl);
        lblSort.setPreferredSize(new Dimension(80, 25));
        add(lblSort);
        //sort by title button
        btnSortTitle = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "By Title", 5, this, layout, lblSort);
        add(btnSortTitle);
        //sort by author button
        btnSortAuthor = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "By Author", 5, this, layout, btnSortTitle);
        add(btnSortAuthor);
        //sort by barcode button
        btnSortBarcode = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "By Barcode", 5, this, layout, btnSortAuthor);
        add(btnSortBarcode);

        //Add in process log section positioned off scrollTbl (main table)
        BuildProcessLogSection(scrollTbl);

        //Build right hand section to display a selected items details
        BuildItemDisplaySection();

        //Build automation section below item details section
        BuildAutomationSection();

        //add an exit button to exit the application
        btnExit = UIBuilderLibrary.BuildJButtonInlineToRight(300, 25, "Exit", 100, this, layout, txtFindBarcode);
        add(btnExit);

        //message label - for later
        lblMessage = UIBuilderLibrary.BuildJLabelInlineBelow("Message test...", 2, layout, lblHashMap);
        add(lblMessage);
    }

    //method to build the UI for the automation section
    private void BuildAutomationSection() {

        //Title bar for automation section
        lblAutoTitle = UIBuilderLibrary.BuildJLabelInlineBelow(" Automation Action Request for the item above:", 25, layout, btnNewItem);
        lblAutoTitle.setPreferredSize(new Dimension(305, 25));
        lblAutoTitle.setOpaque(true);
        lblAutoTitle.setForeground(Color.decode("#365c3f"));
        lblAutoTitle.setBorder(new LineBorder(Color.gray, 1));
        add(lblAutoTitle);
        //retrieve button
        btnAutoRetrieve = UIBuilderLibrary.BuildJButtonInlineBelow(150, 25, "Retrieve", 5, this, layout, lblAutoTitle);
        add(btnAutoRetrieve);
        //remove button
        btnAutoRemove = UIBuilderLibrary.BuildJButtonInlineToRight(150, 25, "Remove", 5, this, layout, btnAutoRetrieve);
        add(btnAutoRemove);
        //return button
        btnAutoReturn = UIBuilderLibrary.BuildJButtonInlineBelow(150, 25, "Return", 5, this, layout, btnAutoRetrieve);
        add(btnAutoReturn);
        //add button
        btnAutoAdd = UIBuilderLibrary.BuildJButtonInlineToRight(150, 25, "Add To Collection", 5, this, layout, btnAutoReturn);
        btnAutoAdd.setMargin(new Insets(0, 0, 0, 0));
        add(btnAutoAdd);
        //search section
        //label
        lblAutoSearch = UIBuilderLibrary.BuildJLabelInlineBelow("Sort Section: ", 5, layout, btnAutoReturn);
        add(lblAutoSearch);
        //text box
        txtAutoSearch = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 5, layout, lblAutoSearch);
        add(txtAutoSearch);
        txtAutoSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                autoSortEnabled(true);
            }
            @Override
            public void focusLost(FocusEvent e) {
                autoSortEnabled(!txtAutoSearch.getText().isEmpty() || !txtAutoSearch.getText().isBlank());
            }
        });
        //Sorting buttons
        btnAutoRandomSort = UIBuilderLibrary.BuildJButtonInlineBelow(150, 25, "Random Collection Sort", 5, this, layout, txtAutoSearch);
        btnAutoRandomSort.setMargin(new Insets(0, 0, 0, 0));
        add(btnAutoRandomSort);
        btnAutoMostSort = UIBuilderLibrary.BuildJButtonInlineBelow(150, 25, "Mostly Sorted Sort", 5, this, layout, btnAutoRandomSort);
        btnAutoMostSort.setMargin(new Insets(0, 0, 0, 0));
        add(btnAutoMostSort);
        btnAutoReverseSort = UIBuilderLibrary.BuildJButtonInlineBelow(150, 25, "Reverse Order Sort", 5, this, layout, btnAutoMostSort);
        btnAutoReverseSort.setMargin(new Insets(0, 0, 0, 0));
        add(btnAutoReverseSort);
    }

    //method to build the UI for the Item display section
    private void BuildItemDisplaySection() {
        lblItemTitle = UIBuilderLibrary.BuildJLabelInlineToRight("Title: ", 545, layout, lblSearch);
        lblItemTitle.setPreferredSize(new Dimension(100, 20));
        add(lblItemTitle);
        txtTitle = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 5, layout, lblItemTitle);
        add(txtTitle);
        txtID = UIBuilderLibrary.BuildJTextFieldInlineToRight(2, 5, layout, txtTitle);
        add(txtID);
        txtID.setEnabled(false);
        lblAuthor = UIBuilderLibrary.BuildJLabelInlineBelow("Author: ", 5, layout, lblItemTitle);
        lblAuthor.setPreferredSize(new Dimension(100, 20));
        add(lblAuthor);
        txtAuthor = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 5, layout, lblAuthor);
        add(txtAuthor);
        lblSection = UIBuilderLibrary.BuildJLabelInlineBelow("Section: ", 5, layout, lblAuthor);
        lblSection.setPreferredSize(new Dimension(100, 20));
        add(lblSection);
        txtSection = UIBuilderLibrary.BuildJTextFieldInlineToRight(4, 5, layout, lblSection);
        add(txtSection);
        lblXPos = UIBuilderLibrary.BuildJLabelInlineBelow("X: ", 5, layout, lblSection);
        lblXPos.setPreferredSize(new Dimension(100, 20));
        add(lblXPos);
        txtXPos = UIBuilderLibrary.BuildJTextFieldInlineToRight(4, 5, layout, lblXPos);
        add(txtXPos);
        lblYPos = UIBuilderLibrary.BuildJLabelInlineBelow("Y: ", 5, layout, lblXPos);
        lblYPos.setPreferredSize(new Dimension(100, 20));
        add(lblYPos);
        txtYPos = UIBuilderLibrary.BuildJTextFieldInlineToRight(4, 5, layout, lblYPos);
        add(txtYPos);
        lblBarcode = UIBuilderLibrary.BuildJLabelInlineBelow("Barcode: ", 5, layout, lblYPos);
        lblBarcode.setPreferredSize(new Dimension(100, 20));
        add(lblBarcode);
        txtBarcode = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 5, layout, lblBarcode);
        add(txtBarcode);
        lblDescription = UIBuilderLibrary.BuildJLabelInlineBelow("Description: ", 5, layout, lblBarcode);
        lblDescription.setPreferredSize(new Dimension(100, 20));
        add(lblDescription);

        txaDescription = new JTextArea();
        txaDescription.setLineWrap(true);
        txaDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(this.txaDescription);
        scrollDescription.setPreferredSize(new Dimension(150, 50));
        scrollDescription.setVerticalScrollBarPolicy(20);
        layout.putConstraint("West", scrollDescription, 0, "West", txtBarcode);
        layout.putConstraint("North", scrollDescription, 5, "South", txtBarcode);
        add(scrollDescription);

        btnNewItem = UIBuilderLibrary.BuildJButtonInlineBelow(100, 25, "New Item", 40, this, layout, lblDescription);
        add(btnNewItem);
        btnItemSave = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Save/Update", 55, this, layout, btnNewItem);
        btnItemSave.setMargin(new Insets(0, 0, 0, 0));
        add(btnItemSave);
    }

    //method the build the UI for the process log section
    private void BuildProcessLogSection(JScrollPane scrollTbl) {
        lblProcessLog = UIBuilderLibrary.BuildJLabelInlineBelow("Process Log:", 5, layout, lblSort);
        lblProcessLog.setPreferredSize(new Dimension(500, 25));
        lblProcessLog.setOpaque(true);
        lblProcessLog.setForeground(Color.decode("#365c3f"));
        lblProcessLog.setBorder(new LineBorder(Color.gray, 1));
        add(lblProcessLog);
        btnProcessLog = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Process Log", 0, this, layout, lblProcessLog);
        btnProcessLog.setMargin(new Insets(0, 0, 0, 0));
        add(btnProcessLog);

        txaProcessLog = new JTextArea();
        txaProcessLog.setLineWrap(true);
        txaProcessLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(this.txaProcessLog);
        scrollLog.setPreferredSize(new Dimension(600, 150));
        scrollLog.setVerticalScrollBarPolicy(20);
        layout.putConstraint("West", scrollLog, 0, "West", scrollTbl);
        layout.putConstraint("North", scrollLog, 65, "South", scrollTbl);
        add(scrollLog);

        lblBinary = UIBuilderLibrary.BuildJLabelInlineBelow(" Display Binary Tree: ", 5, layout, scrollLog);
        lblBinary.setPreferredSize(new Dimension(120, 25));
        lblBinary.setOpaque(true);
        lblBinary.setForeground(Color.decode("#365c3f"));
        lblBinary.setBorder(new LineBorder(Color.gray, 1));
        add(lblBinary);
        btnBPreorder = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Pre-Order", 5, this, layout, lblBinary);
        add(btnBPreorder);
        btnBInorder = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "In-Order", 5, this, layout, btnBPreorder);
        add(btnBInorder);
        btnBPostorder = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Post-Order", 5, this, layout, btnBInorder);
        add(btnBPostorder);
        btnBGraphical = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Graphical", 5, this, layout, btnBPostorder);
        add(btnBGraphical);

        lblHashMap = UIBuilderLibrary.BuildJLabelInlineBelow(" Hashmap / Set: ", 5, layout, lblBinary);
        lblHashMap.setPreferredSize(new Dimension(120, 25));
        lblHashMap.setOpaque(true);
        lblHashMap.setForeground(Color.decode("#365c3f"));
        lblHashMap.setBorder(new LineBorder(Color.gray, 1));
        add(lblHashMap);
        btnHashSave = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Save", 5, this, layout, lblHashMap);
        add(btnHashSave);
        btnHashDisplay = UIBuilderLibrary.BuildJButtonInlineToRight(100, 25, "Display", 5, this, layout, btnHashSave);
        add(btnHashDisplay);

        lblFindBarcode = UIBuilderLibrary.BuildJLabelInlineToRight(" Find Bar Code:", 5, layout, btnHashDisplay);
        lblFindBarcode.setPreferredSize(new Dimension(100, 25));
        lblFindBarcode.setOpaque(true);
        lblFindBarcode.setForeground(Color.decode("#365c3f"));
        add(lblFindBarcode);

        txtFindBarcode = UIBuilderLibrary.BuildJTextFieldInlineToRight(9, 5, layout, lblFindBarcode);
        txtFindBarcode.setPreferredSize(new Dimension(85, 25));
        add(txtFindBarcode);

        txtFindBarcode.addActionListener(e ->
        {
            for (int i = 0; i < tableData.data.length; i++) {
                if (Integer.parseInt(tableData.data[i][6]) == Integer.parseInt(txtFindBarcode.getText())) {
                    txtID.setText(tableData.data[i][0]);
                    txtTitle.setText(tableData.data[i][1]);
                    txtAuthor.setText(tableData.data[i][2]);
                    txtSection.setText(tableData.data[i][3]);
                    txtXPos.setText(tableData.data[i][4]);
                    txtYPos.setText(tableData.data[i][5]);
                    txtBarcode.setText(tableData.data[i][6]);
                    txaDescription.setText(tableData.data[i][7]);
                    autoButtonEnabled(true);
                }
            }
        });
    }

    //method to build the UI for the main table and display read-in data from file
    private JScrollPane BuildMainTable() {
        for (int i = 0; i < tableData.data.length; i++)
        {
            if(tableData.data[i][8].equals("No"))
            {
                tableData.data[i][8] = "false";
            }
        }
        ResetTableData(tableData.data);

        tblArchived.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnNewItem.setEnabled(true);
                newMode = false;
                int row = tblArchived.getSelectedRow();
                selectedItem = Integer.parseInt((String) tblArchived.getValueAt(row, 0));
                for (int i = 0; i < tableData.data.length; i++) {
                    if (Integer.parseInt(tableData.data[i][0]) == selectedItem) {
                        txtID.setText(tableData.data[i][0]);
                        txtTitle.setText(tableData.data[i][1]);
                        txtAuthor.setText(tableData.data[i][2]);
                        txtSection.setText(tableData.data[i][3]);
                        txtXPos.setText(tableData.data[i][4]);
                        txtYPos.setText(tableData.data[i][5]);
                        txtBarcode.setText(tableData.data[i][6]);
                        txaDescription.setText(tableData.data[i][7]);
                        autoButtonEnabled(true);
                    }
                }
            }
        });

        JScrollPane scrollTbl = new JScrollPane(this.tblArchived);
        scrollTbl.setPreferredSize(new Dimension(600, 200));
        scrollTbl.setVerticalScrollBarPolicy(20);
        layout.putConstraint("West", scrollTbl, 0, "West", lblSearch);
        layout.putConstraint("North", scrollTbl, 10, "South", lblSearch);
        add(scrollTbl);
        return scrollTbl;
    }

    //method to reset the table passing in a 2-dimensional array of string to be displayed
    private void ResetTableData(String[][] data)
    {
        //sets the model for the table to use
        tblArchived.setModel(new AbstractTableModel() {

            //override to make column 8 (the boolean column) editable so user can check and uncheck
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }

            //When the user checks or unchecks the value in column 8 set the array data to a string = "true"/"false"
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 8) {
                    data[row][column] = aValue.toString();
                }
            }

            //sets number of rows based on array length
            @Override
            public int getRowCount() {
                return data.length;
            }

            //sets columns based on header array length
            @Override
            public int getColumnCount() {
                return tableData.header.length;
            }

            //sets column names based on string in column header array
            @Override
            public String getColumnName(int column) {
                return tableData.header[column];
            }

            //sets cell values in table
            //each row and column will be the string values except for colum 8
            //these will be converted to boolean values so that the table displays check boxes
            @Override
            public Object getValueAt(int row, int column) {
                switch (column) {
                    case 0, 1, 2, 3, 4, 5, 6, 7:
                        return data[row][column];
                    case 8:
                        if (data[row][column].equals("true")) {
                            return true;
                        } else {
                            return false;
                        }
                    default:
                        return null;
                }
            }

            //sets the class of each column in the table to string and column 8 to boolean
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 8) {
                    return Boolean.class;
                }
                return String.class;
            }
        });

        //Setting each relevant column width to better display data contained
        tblArchived.getColumnModel().getColumn(0).setPreferredWidth(20);
        tblArchived.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblArchived.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblArchived.getColumnModel().getColumn(4).setPreferredWidth(20);
        tblArchived.getColumnModel().getColumn(5).setPreferredWidth(20);
        tblArchived.getColumnModel().getColumn(7).setPreferredWidth(250);
        tblArchived.getColumnModel().getColumn(8).setPreferredWidth(70);

        //sets an auto sort by clicking on the column header
        tblArchived.setAutoCreateRowSorter(true);
    }

    //method to validate user input on Item display section
    private boolean IsValidated()
    {
        if (txtTitle.getText().isEmpty() || txtTitle.getText().isBlank()) {
            txtTitle.grabFocus();
            txtTitle.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"Title\"");
            return false;
        }
        if (txtAuthor.getText().isEmpty() || txtAuthor.getText().isBlank()) {
            txtAuthor.grabFocus();
            txtAuthor.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"Author\"");
            return false;
        }
        if (txtSection.getText().isEmpty() || txtSection.getText().isBlank()) {
            txtSection.grabFocus();
            txtSection.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"Section\"");
            return false;
        }
        try {
            if (txtXPos.getText().isEmpty() || txtXPos.getText().isBlank()) {
                txtXPos.grabFocus();
                txtXPos.selectAll();
                txaProcessLog.setText("Please Enter a valid entry for \"X\"\nThis Must be a whole Number");
                return false;
            }
            int temp = Integer.parseInt(txtXPos.getText());
        } catch (Exception e) {
            txtXPos.grabFocus();
            txtXPos.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"X\"\nThis Must be a whole Number");
            return false;
        }
        try {
            if (txtYPos.getText().isEmpty() || txtYPos.getText().isBlank()) {
                txtYPos.grabFocus();
                txtYPos.selectAll();
                txaProcessLog.setText("Please Enter a valid entry for \"Y\"\nThis Must be a whole Number");
                return false;
            }
            int temp = Integer.parseInt(txtYPos.getText());
        } catch (Exception e) {
            txtYPos.grabFocus();
            txtYPos.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"Y\"\nThis Must be a whole Number");
            return false;
        }
        try {
            if (txtBarcode.getText().isEmpty() || txtBarcode.getText().isBlank()) {
                txtBarcode.grabFocus();
                txtBarcode.selectAll();
                txaProcessLog.setText("Please Enter a valid entry for \"Barcode\"\nThis Must be a whole Number");
                return false;
            }
            int temp = Integer.parseInt(txtBarcode.getText());
        } catch (Exception e) {
            txtBarcode.grabFocus();
            txtBarcode.selectAll();
            txaProcessLog.setText("Please Enter a valid entry for \"Barcode\"\nThis Must be a whole Number");
            return false;
        }
        return true;
    }

    //method for bubble sorting a 2-dimensional array
    public String[][] BubbleSort(String[][] arr)
    {
        //for array length
        for(int j=0; j<arr.length; j++)
        {
            //for the position of i to the length of array
            for(int i=j+1; i<arr.length; i++)
            {
                //if value i is greater than value j
                if((arr[i][1].compareToIgnoreCase(arr[j][1]))<0)
                {
                    //swap value i and j in the array
                    String[] words = arr[j];
                    arr[j] = arr[i];
                    arr[i]= words;
                }
            }
        }
        //return the sorted array
        return arr;
    }

    //method for Insertion sorting a 2-dimensional array
    public String[][] InsertionSort( String[][] arr ,String Direction)
    {
        //set variable to hold position in array
        int i;
        //start looping through array
        for (int j = 1; j < arr.length; j++) // Start with 1 (not 0)
            {
                //set a "key" = row j in 2-d array
                String[] key = arr[j];

                //if statement to check ascending and descending sort
                if(Direction.equals("dec"))
                {
                    //For each value (i) that is less than value (j)
                    for (i = j - 1; (i >= 0) && (Integer.parseInt(arr[i][6]) < Integer.parseInt(key[6])); i--) // Smaller values are moving up
                    {
                        //copy value i over value j
                        arr[i + 1] = arr[i];
                    }
                }
                //if not descending must be ascending by default
                else
                {
                    //For each value (i) that is greater than value (j)
                    for (i = j - 1; (i >= 0) && (Integer.parseInt(arr[i][6]) > Integer.parseInt(key[6])); i--) // Larger values are moving up
                    {
                        //copy value i over value j
                        arr[i + 1] = arr[i];
                    }
                }
                // Put the key in its proper location
                arr[i+1] = key;
            }
        //return sorted 2-d array
        return arr;
    }

    //method to merge 2 sub-arrays of "String[][] arr" together
    void merge(String[][] arr, int l, int m, int r)
    {
        // First sub array is arr[l -> m]
        // Second sub array is arr[m+1 -> r]

        // Find sizes of two sub arrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays Left and Right
        String[][] L = new String[n1][];
        String[][] R = new String[n2][];

        // Copy data to temp arrays
        Arrays.setAll(L, i -> arr[l + i]);
        Arrays.setAll(R, j -> arr[m + 1 + j]);

        // Merge the temp arrays
        // Initial indices of first and second sub arrays
        int i = 0, j = 0;

        // Initial index of merged sub array
        int k = l;
        //while (i) is less than the size of sub array 1 and (j) is less than the size of sub array 2
        while (i < n1 && j < n2) {
            //compare the values of left and right sub arrays
            if (L[i][2].compareToIgnoreCase(R[j][2])<0)
            {
                //position left
                arr[k] = L[i];
                i++;
            }
            else
            {
                //position right
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    //method for Merge sorting a 2-dimensional array
    //l = first position in array
    //r = last position in array
    public String[][] MergeSort(String[][] arr, int l, int r)
    {
        //if first position is less than last position
        if (l < r) {

            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            MergeSort(arr, l, m);
            MergeSort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
        //return sorted array
        return arr;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnSortTitle)
        {
            ResetTableData(BubbleSort(tableData.data));
            PopulateBinaryTree();
        }
        if (e.getSource() == btnSortBarcode)
        {
            if (SortDirection.equals("dec")) {
                ResetTableData(InsertionSort(tableData.data, SortDirection));
                SortDirection = "asc";
            } else {
                ResetTableData(InsertionSort(tableData.data, SortDirection));
                SortDirection = "dec";
            }
            PopulateBinaryTree();

        }
        if (e.getSource() == btnSortAuthor)
        {
            ResetTableData(MergeSort(tableData.data, 0, tableData.data.length - 1));
            PopulateBinaryTree();
        }
        if (e.getSource() == btnSearch)
        {
            if (!searchMode && !txtSearch.getText().isEmpty()) {
                ArrayList<String[]> searchList = new ArrayList<>();
                for (int i = 0; i < tableData.data.length; i++) {
                    for (int j = 0; j < tableData.data[i].length; j++) {
                        if (tableData.data[i][j].toLowerCase().contains(txtSearch.getText().toLowerCase())) {
                            searchList.add(tableData.data[i]);
                            break;
                        }
                    }
                }
                String[][] searchModel = new String[searchList.size()][];
                for (int i = 0; i < searchList.size(); i++) {
                    searchModel[i] = searchList.get(i);
                }
                ResetTableData(searchModel);
                searchMode = true;
                btnSearch.setText("Clear");
                txtSearch.setEnabled(false);

            } else {
                searchMode = false;
                btnSearch.setText("Search");
                txtSearch.setText("");
                txtSearch.setEnabled(true);
                ResetTableData(tableData.data);
            }
        }
        if (e.getSource() == btnExit)
        {
            System.exit(0);
        }
        if (e.getSource() == btnItemSave)
        {
            if (!IsValidated()) {
                return;
            }
            if (!newMode) {
                int saveId = Integer.parseInt(txtID.getText());
                for (int i = 0; i < tableData.data.length; i++) {
                    if (Integer.parseInt(tableData.data[i][0]) == saveId) {
                        tableData.data[i][1] = txtTitle.getText();
                        tableData.data[i][2] = txtAuthor.getText();
                        tableData.data[i][3] = txtSection.getText();
                        tableData.data[i][4] = txtXPos.getText();
                        tableData.data[i][5] = txtYPos.getText();
                        tableData.data[i][6] = txtBarcode.getText();
                        tableData.data[i][7] = txaDescription.getText();
                    }
                }
            } else {
                List<String[]> tempList = new ArrayList<>();
                Collections.addAll(tempList, tableData.data);
                String[] tempLine = {String.valueOf(tempList.size() + 1), txtTitle.getText(),
                        txtAuthor.getText(), txtSection.getText(), txtXPos.getText(), txtYPos.getText(),
                        txtBarcode.getText(), txaDescription.getText(), "No"};
                tempList.add(tempLine);
                tableData.data = new String[tempList.size()][];
                for (int i = 0; i < tempList.size(); i++) {
                    tableData.data[i] = tempList.get(i);
                }
                txtID.setText(String.valueOf(tableData.data.length));
                btnNewItem.setEnabled(true);
                newMode = false;
            }
            ResetTableData(tableData.data);
            binaryButtonsEnabled(false);
            autoButtonEnabled(true);
        }
        if (e.getSource() == btnNewItem)
        {
            btnNewItem.setEnabled(false);
            newMode = true;
            txtID.setText("");
            txtTitle.setText("");
            txtAuthor.setText("");
            txtSection.setText("");
            txtXPos.setText("");
            txtYPos.setText("");
            txtBarcode.setText("");
            txaDescription.setText("");
            autoButtonEnabled(false);
        }
        if(e.getSource()==btnProcessLog)
        {
            if(dList!=null) {
                txaProcessLog.setText(dList.toString());
            }
        }
        if(e.getSource()==btnAutoAdd)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Add - "+txtBarcode.getText()+"."));
            txaProcessLog.setText(dList.toString());
            send("Add");
        }
        if(e.getSource()==btnAutoRemove)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Remove - "+txtBarcode.getText()+"."));
            txaProcessLog.setText(dList.toString());
            send("Remove");
        }
        if(e.getSource()==btnAutoRetrieve)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Retrieve - "+txtBarcode.getText()+"."));
            txaProcessLog.setText(dList.toString());
            send("Retrieve");
        }
        if(e.getSource()==btnAutoReturn)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Return - "+txtBarcode.getText()+"."));
            txaProcessLog.setText(dList.toString());
            send("Return");
        }
        if(e.getSource()==btnAutoMostSort)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Mostly Sorted Sort - "+txtAutoSearch.getText().toUpperCase()+"."));
            txaProcessLog.setText(dList.toString());
            send("Mostly Sorted Sort");
            txtAutoSearch.setText("");
            autoSortEnabled(false);
        }
        if(e.getSource()==btnAutoRandomSort)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Random Collection Sort - "+txtAutoSearch.getText().toUpperCase()+"."));
            txaProcessLog.setText(dList.toString());
            send("Random Collection Sort");
            txtAutoSearch.setText("");
            autoSortEnabled(false);
        }
        if(e.getSource()==btnAutoReverseSort)
        {
            dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Sent - Reverse Order Sort - "+txtAutoSearch.getText().toUpperCase()+"."));
            txaProcessLog.setText(dList.toString());
            send("Reverse Order Sort");
            txtAutoSearch.setText("");
            autoSortEnabled(false);
        }

        if(e.getSource()==btnBPreorder)
        {
            theTree.list = "";
            theTree.preorderTraverseTree(theTree.root);
            txaProcessLog.setText(theTree.list);
        }
        if(e.getSource()==btnBInorder)
        {
            theTree.list = "";
            theTree.inOrderTraverseTree(theTree.root);
            txaProcessLog.setText(theTree.list);
        }
        if(e.getSource()==btnBPostorder)
        {
            theTree.list = "";
            theTree.postOrderTraverseTree(theTree.root);
            txaProcessLog.setText(theTree.list);
        }
        if(e.getSource()==btnBGraphical)
        {
            JFrame BinaryDisplay = new JFrame();
            BinaryDisplay.setTitle("Binary Display");
            BinaryDisplay.setSize(1200,500);
            BinaryDisplay.setLocationRelativeTo(null);
            BinaryDisplay.setContentPane(new BinaryTreeDisplay(theTree));
            BinaryDisplay.setVisible(true);
        }
        if(e.getSource()==txtAutoSearch)
        {
            autoSortEnabled(true);
        }
    }

    private void PopulateBinaryTree()
    {
        theTree = new BinaryTree();
        for (int i = 0; i < tableData.data.length; i++)
        {
            theTree.addNode(Integer.parseInt(tableData.data[i][6]),tableData.data[i][1]);
        }
        binaryButtonsEnabled(true);
    }

    public void connect(String serverName, int serverPort)
    {
        println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    private void send(String buttonPressed)
    {
        try
        {
            StringBuilder str = new StringBuilder("Main: " + buttonPressed + ": " + txtAutoSearch.getText().toUpperCase() + ": " + txtBarcode.getText() + ": ");
            if(!txtAutoSearch.getText().isEmpty())
            {
                for (int i = 0; i < tableData.data.length; i++)
                {
                    if(tableData.data[i][3].equalsIgnoreCase(txtAutoSearch.getText())) {
                        str.append(Arrays.toString(tableData.data[i]));
                    }
                }
            }else
            {
                str.append(Arrays.toString(tableData.data[tblArchived.getSelectedRow()]));
            }
            streamOut.writeUTF(str.toString());
            streamOut.flush();
        }
        catch (IOException ioe)
        {
            println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else
        {
            println(msg);
            String[] temp = msg.split(": ");
            if(!temp[1].equals("Main"))
            {
                dList.head.append(new Node(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " - Rcvd - "+ temp[2] +" - " + temp[3] + "."));
                txaProcessLog.setText(dList.toString());
            }
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new MainFormThread1(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            println("Error closing ...");
        }
        client.close();
        client.interrupt();
    }

    void println(String msg)
    {
        lblMessage.setText(msg);
    }

    public void getParameters()
    {
        serverName = "localhost";
        serverPort = 4444;
    }
}
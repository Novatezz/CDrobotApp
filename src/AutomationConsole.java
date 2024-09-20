import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class AutomationConsole extends JFrame implements ActionListener {

    SpringLayout layout = new SpringLayout();
    JLabel lblTitle,lblRequest,lblBarcode,lblSection,lblMessage;
    JButton btnProcess,btnAdd,btnExit;
    JTextField txtBarcode,txtSection;
    JComboBox comSort;
    JTable tblArchived = new JTable();
    String[] header;
    //FileManager file = new FileManager( );
    //FileData tableData = file.ReadFromCSV("E:\\JavaSemester2\\CDrobot\\src\\CD_ArchivePrototype_SampleData.txt");

    //CHAT RELATED ---------------------------
    private Socket socket = null;
    //private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private AutomationThread2 client2 = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    //----------------------------------------


    public AutomationConsole() {
        setTitle("Automation Console");
        setLayout(layout);
        setSize(800,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setLocation(this.getX() + 300,this.getY()+270);

        //sets background colour of the window
        this.getContentPane().setBackground(Color.decode("#599467"));

        header = MainForm.header;
        //tblArchived = new JTable(,header);
        BuildMainTable();


        lblTitle = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor(" Automation Console",0,0,layout,this);
        lblTitle.setFont(new Font("Courier", Font.BOLD,25));
        lblTitle.setPreferredSize(new Dimension(800,40));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Color.decode("#365c3f"));
        lblTitle.setForeground(Color.white);
        add(lblTitle);

        lblRequest = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor("Current Requested Action:",150,60,layout,this);
        lblRequest.setPreferredSize(new Dimension(150,25));
        add(lblRequest);

        String[] comboItems = new String[]{"Retrieve","Remove","Return","Add","Random Collection Sort","Mostly Sorted Sort","Reverse Order Sort"};
        comSort = new JComboBox<>(comboItems);
        comSort.setPreferredSize(new Dimension(150,25));
        layout.putConstraint("West",comSort,155,"West",lblRequest);
        layout.putConstraint("North",comSort,0,"North",lblRequest);
        add(comSort);
        btnProcess = UIBuilderLibrary.BuildJButtonInlineToRight(100,25,"Process",5,this,layout,comSort);
        add(btnProcess);


        lblBarcode = UIBuilderLibrary.BuildJLabelInlineBelow("Barcode of Selected Item:",5,layout,lblRequest);
        lblRequest.setPreferredSize(new Dimension(150,25));
        add(lblBarcode);
        txtBarcode = UIBuilderLibrary.BuildJTextFieldInlineToRight(8,5,layout,lblBarcode);
        add(txtBarcode);
        lblSection = UIBuilderLibrary.BuildJLabelInlineToRight("Section:",5,layout,txtBarcode);
        add(lblSection);
        txtSection = UIBuilderLibrary.BuildJTextFieldInlineToRight(2,5,layout,lblSection);
        add(txtSection);
        btnAdd = UIBuilderLibrary.BuildJButtonInlineToRight(100,25,"Add Item",5,this,layout,txtSection);
        add(btnAdd);



        btnExit = UIBuilderLibrary.BuildJButtonWithNorthWestAnchor(100,25,"Exit",650,320,this,layout,this);
        add(btnExit);
        lblMessage = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor("message",20,340,layout,this);
        add(lblMessage);

        //CHAT RELATED ---------------------------
        getParameters();
        connect(serverName, serverPort);
        //----------------------------------------

        setVisible(true);
    }
    private void BuildMainTable() {
//        for (int i = 0; i < tableData.data.length; i++)
//        {
//            if(tableData.data[i][8].equals("No"))
//            {
//                tableData.data[i][8] = "false";
//            }
//        }
        //ResetTableData(null);

//        tblArchived.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                btnNewItem.setEnabled(true);
//                newMode = false;
//                int row = tblArchived.getSelectedRow();
//                selectedItem = Integer.parseInt((String) tblArchived.getValueAt(row, 0));
//                for (int i = 0; i < tableData.data.length; i++) {
//                    if (Integer.parseInt(tableData.data[i][0]) == selectedItem) {
//                        txtID.setText(tableData.data[i][0]);
//                        txtTitle.setText(tableData.data[i][1]);
//                        txtAuthor.setText(tableData.data[i][2]);
//                        txtSection.setText(tableData.data[i][3]);
//                        txtXPos.setText(tableData.data[i][4]);
//                        txtYPos.setText(tableData.data[i][5]);
//                        txtBarcode.setText(tableData.data[i][6]);
//                        txaDescription.setText(tableData.data[i][7]);
//                        autoButtonEnabled(true);
//                    }
//                }
//            }
//        });

        JScrollPane scrollTbl = new JScrollPane(this.tblArchived);
        scrollTbl.setPreferredSize(new Dimension(740,180));
        scrollTbl.setVerticalScrollBarPolicy(20);
        layout.putConstraint("West",scrollTbl,20,"West",this);
        layout.putConstraint("North",scrollTbl,85,"South",this);
        add(scrollTbl);
    }
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
                return header.length;
            }

            //sets column names based on string in column header array
            @Override
            public String getColumnName(int column) {
                return header[column];
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
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnProcess)
        {
            if((comSort.getSelectedIndex()<4 && !txtBarcode.getText().isEmpty())||
                    (comSort.getSelectedIndex()>3 && !txtSection.getText().isEmpty()))
            {
                send((String) comSort.getSelectedItem());

            }else {
                lblMessage.setText("Please Enter a valid Barcode for actions or section for sorting");
            }
        }

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

    private void send(String mode)
    {
        try
        {
            if(!txtBarcode.getText().isEmpty()||!txtBarcode.getText().isEmpty())
            {
            streamOut.writeUTF("Auto: " + mode + ": "+ txtBarcode.getText() );
            streamOut.flush();
            }
            if(!txtSection.getText().isEmpty()||!txtSection.getText().isEmpty())
            {
                streamOut.writeUTF("Auto: " + mode + ": "+ txtSection.getText() );
                streamOut.flush();
            }
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
            System.out.println("Handle: " + msg);
            println(msg);
            String[] temp = msg.split(": ");
            if(!temp[1].equals("Auto")) {
                comSort.setSelectedItem(temp[2]);
                if(comSort.getSelectedIndex()<4)
                {
                    txtBarcode.setText(temp[4]);
                    txtSection.setText("");
                }else {
                    txtSection.setText(temp[3]);
                    txtBarcode.setText("");
                }
                if(temp[5]!=null) {
                    String[] temp2 = temp[5].split("]");
                    String[][] temp3 = new String[temp2.length][];
                    for (int i = 0; i < temp2.length; i++) {
                        temp2[i] = temp2[i].substring(1);
                        //System.out.println(temp2[i]);
                        temp3[i] = temp2[i].split(",");
                    }
                    ResetTableData(temp3);
                }
            }
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client2 = new AutomationThread2(this, socket);
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
        client2.close();
        client2.interrupt();
    }

    void println(String msg)
    {
        //display.appendText(msg + "\n");
        lblMessage.setText(msg);
    }

    public void getParameters()
    {
//        serverName = getParameter("host");
//        serverPort = Integer.parseInt(getParameter("port"));

        serverName = "localhost";
        serverPort = 4444;
    }
}

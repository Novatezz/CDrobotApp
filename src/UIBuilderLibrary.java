import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A component building toolset for Java Swing components using a spring layout system.
 * Each component is passed to the appropriate method which sets its base parameters, position and configuration before
 * returning it to the caller.
 */
public class UIBuilderLibrary
{

    /**
     * Takes a provided JLabel and configures it based upon the provided parameters using a Spring Layout system.
     * The label will be positioned by the spring layout using the top left (North-West) corner of the label vs
     * the top left of another component.
     *
     * @param text The text to be printed to the label
     * @param hPad The horizontal padding distance between the 2 objects
     * @param vPad The vertical padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the label will be located in association to.
     *
     * @return A completed and setup JLabel ready to be added to a Frame
     */
    public static JLabel BuildJLabelWithNorthWestAnchor(String text, int hPad, int vPad, SpringLayout layout, Component other)
    {
        // Create a new label object with the specified text displayed on it.
        JLabel myLabel = new JLabel(text);
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myLabel,hPad,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myLabel,vPad,SpringLayout.NORTH,other);
        myLabel.setForeground(Color.white);
        return  myLabel;
    }

    /**
     * Takes a provided JLabel and configures it based upon the provided parameters using a Spring Layout system.
     * The label will be positioned by the spring layout to be inline horizontally and to the direct right to
     * a designated component.
     *
     * @param text The text to be printed to the label
     * @param hPad The horizontal padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the label will be located in association to.
     *
     * @return A completed and setup JLabel ready to be added to a Frame
     */
    public static JLabel BuildJLabelInlineToRight(String text, int hPad, SpringLayout layout, Component other)
    {
        // Create a new label object with the specified text displayed on it.
        JLabel myLabel = new JLabel(text);
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myLabel,hPad,SpringLayout.EAST,other);
        layout.putConstraint(SpringLayout.NORTH, myLabel,0,SpringLayout.NORTH,other);
        myLabel.setForeground(Color.white);
        return  myLabel;
    }

    /**
     * Takes a provided JLabel and configures it based upon the provided parameters using a Spring Layout system.
     * The label will be positioned by the spring layout to be inline vertically and directly beneath the
     * designated component.
     *
     * @param text The text to be printed to the label
     * @param vPad The vertical padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the label will be located in association to.
     *
     * @return A completed and setup JLabel ready to be added to a Frame
     */
    public static JLabel BuildJLabelInlineBelow(String text, int vPad, SpringLayout layout, Component other)
    {
        // Create a new label object with the specified text displayed on it.
        JLabel myLabel = new JLabel(text);
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myLabel,0,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myLabel,vPad,SpringLayout.SOUTH,other);
        myLabel.setForeground(Color.white);
        return  myLabel;
    }

    public static JTable BuildJTableWithNorthWestAnchor(int hPad, int vPad, SpringLayout layout, Component other)
    {
        // Create a new label object with the specified text displayed on it.
        JTable myTable = new JTable();
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myTable,hPad,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myTable,vPad,SpringLayout.NORTH,other);
        myTable.setForeground(Color.white);
        return  myTable;
    }

    public static JTable BuildJTableInlineToRight( int hPad, SpringLayout layout, Component other)
    {
        // Create a new label object with the specified text displayed on it.
        JTable myTable = new JTable();
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myTable,hPad,SpringLayout.EAST,other);
        layout.putConstraint(SpringLayout.NORTH, myTable,0,SpringLayout.NORTH,other);
        myTable.setForeground(Color.white);
        return  myTable;
    }

    public static JTable BuildJTableInlineBelow(int vPad, SpringLayout layout, Component other)
    {
        // Data to be displayed in the JTable
        String[][] data = {
                { "Kundan Kumar Jha", "4031", "CSE" },
                { "Anand Jha", "6014", "IT" }
        };
        // Column Names
        String[] columnNames = { "Name", "Roll Number", "Department" };
        // Create a new label object with the specified text displayed on it.
        JTable myTable = new JTable(data,columnNames);
        myTable.setBounds(30,40,200,300);
        // Constrain the vertical and horizontal positions of the label
        layout.putConstraint(SpringLayout.WEST, myTable,0,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myTable,vPad,SpringLayout.SOUTH,other);
        myTable.setForeground(Color.white);
        myTable.setBackground(Color.decode("#365c3f"));
        return  myTable;
    }

    /**
     * Takes a provided JTextField and configures it based upon the provided parameters using a Spring Layout system.
     * The field will be positioned by the spring layout using the top left (North-West) corner of the field vs
     * the top left of another component.
     *
     * @param size The width in columns the text field size will be set to (1 column = 1 lower case 'm' in current font setting)
     * @param hPad The horizontal padding distance between the 2 objects
     * @param vPad The vertical padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     *
     * @return A completed and setup JTextField ready to be added to a Frame
     */
    public static JTextField BuildJTextFieldWithNorthWestAnchor(int size,int hPad,int vPad,SpringLayout layout,Component other)
    {
        // Create a new text field object with the specified text size. The size is based upon a column width principal where each
        // column is equivalent to a lower case m in size.
        JTextField myTextField = new JTextField(size);
        // Constrain the vertical and horizontal positions of the text field
        layout.putConstraint(SpringLayout.WEST, myTextField,hPad,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myTextField,vPad,SpringLayout.NORTH,other);
        //myTextField.setBackground(Color.decode("#365c3f"));
        //myTextField.setForeground(Color.white);
        //myTextField.setBorder(new LineBorder(Color.gray,1));
        return  myTextField;
    }

    /**
     * Takes a provided JTextField and configures it based upon the provided parameters using a Spring Layout system.
     * The field will be positioned by the spring layout to be directly inline horizontally and to the right side of the
     * designated component
     *
     * @param size The width in columns the text field size will be set to (1 column = 1 lower case 'm' in current font setting)
     * @param hPad The horizontal padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     *
     * @return A completed and setup JTextField ready to be added to a Frame
     */
    public static JTextField BuildJTextFieldInlineToRight(int size,int hPad,SpringLayout layout,Component other)
    {
        // Create a new text field object with the specified text size. The size is based upon a column width principal where each
        // column is equivalent to a lower case m in size.
        JTextField myTextField = new JTextField(size);
        // Constrain the vertical and horizontal positions of the text field
        layout.putConstraint(SpringLayout.WEST, myTextField,hPad,SpringLayout.EAST,other);
        layout.putConstraint(SpringLayout.NORTH, myTextField,0,SpringLayout.NORTH,other);
       //myTextField.setBackground(Color.decode("#365c3f"));
        //myTextField.setForeground(Color.white);
        myTextField.setBorder(new LineBorder(Color.gray,1));
        return  myTextField;
    }

    /**
     * Takes a provided JTextField and configures it based upon the provided parameters using a Spring Layout system.
     * The field will be positioned by the spring layout to be directly inline vertically and to the bottom of the
     * designated component
     *
     * @param size The width in columns the text field size will be set to (1 column = 1 lower case 'm' in current font setting)
     * @param vPad The vertical padding distance between the 2 objects
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     *
     * @return A completed and setup JTextField ready to be added to a Frame
     */
    public static JTextField BuildJTextFieldInlineBelow(int size,int vPad,SpringLayout layout,Component other)
    {
        // Create a new text field object with the specified text size. The size is based upon a column width principal where each
        // column is equivalent to a lower case m in size.
        JTextField myTextField = new JTextField(size);
        // Constrain the vertical and horizontal positions of the text field
        layout.putConstraint(SpringLayout.WEST, myTextField,0,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myTextField,vPad,SpringLayout.SOUTH,other);
        //myTextField.setBackground(Color.decode("#365c3f"));
        //myTextField.setForeground(Color.white);
        //myTextField.setBorder(new LineBorder(Color.gray,1));
        return  myTextField;
    }

    /**
     * Takes a provided JButton and configures it based upon the provided parameters using a Spring Layout system.
     * The JButton will be positioned by the spring layout using the top left (North-West) corner of the JButton vs
     * the top left of another component.
     *
     * @param width The preferred width of the button
     * @param height The preferred height of the button
     * @param text The text to be printed on the button
     * @param hPad The horizontal padding distance between the 2 objects
     * @param vPad The vertical padding distance between the 2 objects
     * @param listener The listener object to be informed when the button is pressed
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     * @return
     */
    public static JButton BuildJButtonWithNorthWestAnchor(int width, int height, String text, int hPad, int vPad,
                                                          ActionListener listener, SpringLayout layout, Component other)
    {
        // Create a new button object with the specified text displayed on it.
        JButton myButton = new JButton(text);
        //Sets the preferred size of the component by providing it a Dimension object with e height and width value.
        myButton.setPreferredSize(new Dimension(width,height));
        //Adds an action listener to the button which will be notified whenever the button is pressed.
        myButton.addActionListener(listener);
        // Constrain the vertical and horizontal positions of the button
        layout.putConstraint(SpringLayout.WEST, myButton,hPad,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myButton,vPad,SpringLayout.NORTH,other);
        myButton.setBackground(Color.decode("#365c3f"));
        myButton.setForeground(Color.white);
        return  myButton;
    }

    /**
     * Takes a provided JButton and configures it based upon the provided parameters using a Spring Layout system.
     * The JButton will be positioned by the spring layout  to be directly inline horizontally and to the right of the
     * designated component
     *
     * @param width The preferred width of the button
     * @param height The preferred height of the button
     * @param text The text to be printed on the button
     * @param hPad The horizontal padding distance between the 2 objects
     * @param listener The listener object to be informed when the button is pressed
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     * @return
     */
    public static JButton BuildJButtonInlineToRight(int width, int height, String text, int hPad,
                                                    ActionListener listener, SpringLayout layout, Component other)
    {
        // Create a new button object with the specified text displayed on it.
        JButton myButton = new JButton(text);
        //Sets the preferred size of the component by providing it a Dimension object with e height and width value.
        myButton.setPreferredSize(new Dimension(width,height));
        //Adds an action listener to the button which will be notified whenever the button is pressed.
        myButton.addActionListener(listener);
        // Constrain the vertical and horizontal positions of the button
        layout.putConstraint(SpringLayout.WEST, myButton,hPad,SpringLayout.EAST,other);
        layout.putConstraint(SpringLayout.NORTH, myButton,0,SpringLayout.NORTH,other);
        myButton.setBackground(Color.decode("#365c3f"));
        myButton.setForeground(Color.white);
        return  myButton;
    }

    /**
     * Takes a provided JButton and configures it based upon the provided parameters using a Spring Layout system.
     * The JButton will be positioned by the spring layout  to be directly inline vertically and to the bottom of the
     * designated component
     *
     * @param width The preferred width of the button
     * @param height The preferred height of the button
     * @param text The text to be printed on the button
     * @param vPad The vertical padding distance between the 2 objects
     * @param listener The listener object to be informed when the button is pressed
     * @param layout The spring layout object used for positioning
     * @param other The component which the text field will be located in association to.
     * @return
     */
    public static JButton BuildJButtonInlineBelow(int width, int height, String text, int vPad, ActionListener listener, SpringLayout layout, Component other)
    {
        // Create a new button object with the specified text displayed on it.
        JButton myButton = new JButton(text);
        //Sets the preferred size of the component by providing it a Dimension object with e height and width value.
        myButton.setPreferredSize(new Dimension(width,height));
        //Adds an action listener to the button which will be notified whenever the button is pressed.
        myButton.addActionListener(listener);
        // Constrain the vertical and horizontal positions of the button
        layout.putConstraint(SpringLayout.WEST, myButton,0,SpringLayout.WEST,other);
        layout.putConstraint(SpringLayout.NORTH, myButton,vPad,SpringLayout.SOUTH,other);
        myButton.setBackground(Color.decode("#365c3f"));
        myButton.setForeground(Color.white);
        return  myButton;
    }

}

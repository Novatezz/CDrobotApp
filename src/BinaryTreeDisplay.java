import javax.swing.*;
import java.awt.*;

public class BinaryTreeDisplay extends JPanel
{
    BinaryTree treeDisplay;
    int xValue = 600;
    int yValue = 50;

    BinaryTreeDisplay(BinaryTree tree)
    {
        treeDisplay = tree;
    }
    public void paintComponent(Graphics g)
    {
        drawTree(g,treeDisplay.root,xValue,yValue);
    }
    public void drawTree(Graphics g, BNode focusNode, int x , int y)
    {
        g.drawString(String.valueOf(focusNode.key), x, y);
        if(focusNode.leftChild !=null && focusNode.rightChild != null)
        {
            g.drawLine(x-5,y+5,x-90,y+40);
            drawTree(g,focusNode.leftChild,x-100,y+50);
            g.drawLine(x+5,y+5,x+90,y+40);
            drawTree(g,focusNode.rightChild,x+100,y+50);
        }
        if(focusNode.leftChild != null && focusNode.rightChild == null)
        {
            g.drawLine(x-5,y+5,x-40,y+20);
            drawTree(g,focusNode.leftChild,x-50,y+30);
        }
        if(focusNode.rightChild != null && focusNode.leftChild == null)
        {
            g.drawLine(x+5,y+5,x+40,y+20);
            drawTree(g,focusNode.rightChild,x+50,y+30);
        }
    }
}

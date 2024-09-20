import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File Manager class
 * Holds all of our methods for reading and writing files
 */
public class FileManager
{
    /**
     * Method for reading CSV file from file path
     * @param filePath name of file and where we want the file to be read from
     * @return File data object populated by data from read file
     */
    public FileData ReadFromCSV(String filePath)
    {
        //New File data object
        FileData fileData = new FileData();
        //Sets length of 2D array - 30 is max length we want to read in
        List<String[]> readData = new ArrayList<>();
        try
        {
            //new buffered reader object passing in filepath
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            //reads header data and sets object variable to read data
            fileData.header = reader.readLine().split(";");

            //temporary variable to hold data from each line
            String line;
            //counter of lines
            int count = 0;
            //read each line and split them by the delimiter ';'
            while ((line = reader.readLine()) != null)
            {
                String[] temp = line.split(";");
                //sets row at column position
                readData.add(temp);
                //increment counter
                count++;
            }
            fileData.data = new String[count][];
            for (int i = 0; i < readData.size(); i++) {
                fileData.data[i] = readData.get(i);
            }
            //close reader
            reader.close();
        }
        catch (Exception ex)
        {
            //if we get an error mid read return null
            fileData = null;
            System.out.println(ex.getMessage());
        }
        return fileData;
    }


}


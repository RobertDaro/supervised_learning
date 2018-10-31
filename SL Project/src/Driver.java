import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Robert on 10/30/2018.
 *
 * The driver for the program, should theoretically handle everything.
 */
public class Driver
{
    public static void main(String[] args)
    {
        //system arguments
        String fileName = args[0];
        String targetVar = args[1];
        System.out.println(fileName);

        DataFrame data = readFile(fileName,targetVar);
        System.out.println("Finished reading");

    }

    public static DataFrame readFile(String fileStr, String targetValStr)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileStr)))
        {
            //First, grab the header file
            String line = reader.readLine();
            DataFrame completeData = new DataFrame(line, targetValStr);

            //grabbing first line of the data
            line = reader.readLine();
            while(line != null && !line.equals(""))
            {
                completeData.addEntry(line);
                line = reader.readLine();
            }

            return completeData;
        }
        catch(IOException e)
        {
            System.out.println("IO error occured when reading from the file.");
        }
        return null;
    }
}

import javax.xml.crypto.Data;
import java.io.*;
import java.util.Random;

/**
 * Created by Robert on 10/30/2018.
 *
 * The driver for the program, should theoretically handle everything.
 */
public class Driver
{
    public static void main(String[] args) throws IOException
    {
        //system arguments
        String fileName = args[0];
        String targetVar = args[1];
        String validationResultsFile = args[2];
        String testingResultsFile = args[3];
        String agentFlag = args[4]; //"n" for neural net, else random

        //read in data
        System.out.println(fileName);
        DataFrame data = readFile(fileName,targetVar);

        //split data into training, validation, testing
        int validationSplit = data.size() *3/5;
        int testingSplit = validationSplit + (data.size()/5);

        DataFrame testingData = data.splitAt(testingSplit);
        DataFrame validationData = data.splitAt(validationSplit);

        //create our agent
        Agent agent = new RandomAgent();

        if(agentFlag.equals("n"))
        {
            //make a neural agent
        }
        else
        {
            //fine as is!
        }

        // Training agent
        agent.learn(data, targetVar);
        //test agent over validation data
        testAgent(agent, validationData,targetVar,
                validationResultsFile,true);
        //evaluate testing data
        testAgent(agent, testingData,targetVar,
                testingResultsFile,false);

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


    // Returns 1 if a correct answer was chosen.
    public static int resultOutput(double result, String answer)
    {
        // This should not happen
        if(result < 0.0 || result > 1.0)
        {
            return -500;
        }
        else if (result < 0.5 &&
                (answer.equalsIgnoreCase("failed") || answer.equalsIgnoreCase("canceled")))
        {
            return 1;
        }
        else if (result > 0.5 && answer.equalsIgnoreCase("successful"))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    //Test the agent on stuff
    public static void testAgent(Agent agent, DataFrame data, String targetVar, String file, Boolean learn)
    {
        double[] results;

        if(learn)
        {
            results = agent.learn(data, targetVar);
        }
        else
        {
            results = agent.evaluate(data);
        }

        try(PrintWriter writer = new PrintWriter(new BufferedWriter( new FileWriter(file))))
        {
            writer.println("true_answer,agent_answer");
            for(int i = 0; i < data.size(); i++ )
            {
                DataEntry entry =  data.at(i);
                double result = results[i];
                String answer = entry.at(targetVar);
                double realAns;
                if(answer.equalsIgnoreCase("failed") || answer.equalsIgnoreCase("canceled"))
                {
                    realAns = 0.0;
                }
                else
                {
                    realAns = 1.0;
                }

                writer.println("" + realAns +"," + result);
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

    }
}

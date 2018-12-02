import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by Robert on 12/1/2018.
 */
public class NetTest
{
    public static void main(String[] args)
    {
        //system arguments
        String fileName = args[0];
        String targetVar = args[1];
        double learningRate = Double.parseDouble(args[2]);
        String outputFile = args[3];

        //read in data
        System.out.println(fileName);
        DataFrame data = readFile(fileName,targetVar);

        //Functions to use
        final WeightedSumFunction sumFunction = new WeightedSumFunction();
        final TanhActivation tanhActivation = new TanhActivation();
        final SigmoidActivation sigmoidActivation = new SigmoidActivation();
        final SquaredError squaredError = new SquaredError();

        //Nodes, Layers, etc
        InputNode input1 = new InputNode(sumFunction,tanhActivation);
        InputNode input2 = new InputNode(sumFunction,tanhActivation);
        Node output = new Node(sumFunction,sigmoidActivation);

        ArrayList<Node> inNodes = new ArrayList<>();
        inNodes.add(input1);
        inNodes.add(input2);

        ArrayList<Node> outNodes = new ArrayList<>();
        outNodes.add(output);

        Layer inputLayer = new Layer(inNodes);
        Layer outputLayer = new Layer(outNodes);

        //Number of hidden layers and sizes
        int[] sizes = {16,8,4};

        Net net = NetMaker.makeNet(inputLayer,outputLayer,sumFunction,tanhActivation,sizes);

        //Now time to run the experiments
        ArrayList<Double> actualAnswers = new ArrayList<>();
        ArrayList<Double> guesses = new ArrayList<>();
        ArrayList<Double> aVals = new ArrayList<>();
        ArrayList<Double> bVals = new ArrayList<>();

        for(int i = 0; i < 30; i++)
        {
            for(int j = 0; j < data.size(); j++)
            {
                double[] trueAnswers = {Double.parseDouble(data.at(j).at(targetVar))};
                double inVal1 = Double.parseDouble(data.at(j).at("A"));
                input1.setValue(inVal1);
                double inVal2 = Double.parseDouble(data.at(j).at("B"));
                input2.setValue(inVal2);
                double[] guessedAnswers = net.learn(trueAnswers,learningRate,squaredError);

                //We want to see some progress
                if(j % 30 == 0)
                {
                    actualAnswers.add(trueAnswers[0]);
                    guesses.add(guessedAnswers[0]);
                    aVals.add(inVal1);
                    bVals.add(inVal2);
                }
            }
        }

        try(PrintWriter writer = new PrintWriter(new BufferedWriter( new FileWriter(outputFile))))
        {
            writer.write("A,B,Actual,Guessed\n");
            for(int i = 0; i < actualAnswers.size(); i++)
            {
                writer.write("" + aVals.get(i) + "," + bVals.get(i) + "," + actualAnswers.get(i) +  "," + guesses.get(i) + "\n");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        input1.setValue(0);
        input2.setValue(1);
        System.out.println(net.getResult()[0]);

        input1.setValue(0);
        input2.setValue(0);
        System.out.println(net.getResult()[0]);

        input1.setValue(1);
        input2.setValue(0);
        System.out.println(net.getResult()[0]);

        input1.setValue(1);
        input2.setValue(1);
        System.out.println(net.getResult()[0]);
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

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Robert on 12/2/2018.
 */
public class NetExperiments
{
    private static final int CATEGORY = 0, CURRENCY = 1, TIME = 2, GOAL = 3, COUNTRY = 4;
    private static final int NUM_INPUTS = 5;

    //Found with python
    private static double MAX_GOAL = 166361390.71;
    private static double MIN_GOAL = 0.01;
    private static double MAX_TIME = 1446246000000.0;
    private static double MIN_TIME = 437000.0;

    //Neural Nets that are being used
    private static Net netWork1;
    private static Net netWork2;

    private static ArrayList<InputNode>[] inputs1;
    private static ArrayList<InputNode>[] inputs2;

    //Functions to use
    private static final WeightedSumFunction sumFunction = new WeightedSumFunction();
    private static final TanhActivation tanhActivation = new TanhActivation();
    private static final SigmoidActivation sigmoidActivation = new SigmoidActivation();
    private static final SquaredError squaredError = new SquaredError();

    //learning rate
    private static double learningRate;

    //categories
    private static ArrayList<String> mainCatList;
    private static ArrayList<String> currencyList;
    private static ArrayList<String> countryList;

    //Stuff to use
    private static String targetVar;
    private static String outputFileAPrefix;
    private static String outputFileBPrefix;

    public static void main(String[] args)
    {
        //System args
        String mainCategoriesFileName = args[0];
        String currenciesFileName = args[1];
        String countriesFileName = args[2];
        String dataFileName = args[3];
        targetVar = args[4];
        outputFileAPrefix = args[5];
        outputFileBPrefix = args[6];
        learningRate = Double.parseDouble(args[7]);


        //Lists of data
        mainCatList = getCategoryList(mainCategoriesFileName);
        currencyList = getCategoryList(currenciesFileName);
        countryList = getCategoryList(countriesFileName);

        DataFrame data = readFile(dataFileName,targetVar);
        //split data into training, validation, testing
        int validationSplit = data.size() *3/5;
        int testingSplit = validationSplit + (data.size()/5);

        DataFrame testingData = data.splitAt(testingSplit);
        DataFrame validationData = data.splitAt(validationSplit);

        //make our nets!
        makeNets();

        //These will run all net stuff, and output whatever values we get!
        System.out.println("Starting experiment A");
        experiment(netWork1,inputs1,outputFileAPrefix, data, validationData, testingData);
        System.out.println("Starting experiment B");
        experiment(netWork2,inputs2,outputFileBPrefix, data, validationData, testingData);
        System.out.println("Finished!");
    }

    //processes a given file and gets the data as an arraylist
    private static ArrayList<String> getCategoryList(String fileName)
    {
        ArrayList<String> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            //First, grab the header file, just to get rid of it
            reader.readLine();

            //grabbing first line of the data
            String line = reader.readLine();
            while(line != null && !line.equals(""))
            {
                list.add(line);
                line = reader.readLine();
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return list;
    }

    //reads stuff into dataframes
    private static DataFrame readFile(String fileStr, String targetValStr)
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

    //Experiment A: 2 hidden layers
    private static void experiment(Net netWork1, ArrayList<InputNode>[] inputs1, String filePrefix, DataFrame data, DataFrame validationData, DataFrame testingData)
    {
        //Now time to run the experiments
        ArrayList<Double> actualAnswers = new ArrayList<>();
        ArrayList<Double> guesses = new ArrayList<>();

        //First, we do the training examples
        for(int i = 0; i < data.size(); i++)
        {
            //Data frame we are working with
            DataEntry entry = data.at(i);

            //set the inputs
            setInputs(inputs1, entry);

            //true answer
            double[] trueAnswersArr = new double[1];
            if(entry.at(targetVar).equals("failed") || entry.at(targetVar).equals("canceled"))
            {
                actualAnswers.add(1.0);
                trueAnswersArr[0] = 1.0;
            }
            else
            {
                actualAnswers.add(0.0);
                trueAnswersArr[0] = 0.0;
            }

            //guess an answer and learn from it
            double[] guessedAnswers = netWork1.learn(trueAnswersArr,learningRate,squaredError);
            guesses.add(guessedAnswers[0]);

            //give indication of how far we are
            System.out.println("training " + i);
        }

        //Now we write to a file our data
        writeResults(actualAnswers,guesses,filePrefix + "training.csv");

        //reset our arrays
        actualAnswers = new ArrayList<>();
        guesses = new ArrayList<>();

        //Next, we do the validation examples
        for(int i = 0; i < validationData.size(); i++)
        {
            //Data frame we are working with
            DataEntry entry = validationData.at(i);

            //set the inputs
            setInputs(inputs1, entry);

            //true answer
            double[] trueAnswersArr = new double[1];
            if(entry.at(targetVar).equals("failed") || entry.at(targetVar).equals("canceled"))
            {
                actualAnswers.add(1.0);
                trueAnswersArr[0] = 1.0;
            }
            else
            {
                actualAnswers.add(0.0);
                trueAnswersArr[0] = 0.0;
            }

            //guess an answer and learn from it
            double[] guessedAnswers = netWork1.learn(trueAnswersArr,learningRate,squaredError);
            guesses.add(guessedAnswers[0]);

            //give indication of how far we are
            if(i % 100 == 0)
            {
                System.out.println("validation " + i);
            }
        }

        //Now we write to a file our data
        writeResults(actualAnswers,guesses,filePrefix + "validation.csv");

        //reset our arrays
        actualAnswers = new ArrayList<>();
        guesses = new ArrayList<>();

        //Finally, we do the testing examples
        for(int i = 0; i < testingData.size(); i++)
        {
            //Data frame we are working with
            DataEntry entry = testingData.at(i);

            //set the inputs
            setInputs(inputs1, entry);

            //true answer
            double[] trueAnswersArr = new double[1];
            if(entry.at(targetVar).equals("failed") || entry.at(targetVar).equals("canceled"))
            {
                actualAnswers.add(1.0);
                trueAnswersArr[0] = 1.0;
            }
            else
            {
                actualAnswers.add(0.0);
                trueAnswersArr[0] = 0.0;
            }

            //guess an answer and record it
            double[] guessedAnswers = netWork1.getResult();
            guesses.add(guessedAnswers[0]);

            //give indication of how far we are
            if(i % 100 == 0)
            {
                System.out.println("testing " + i);
            }
        }

        //Now we write to a file our data
        writeResults(actualAnswers,guesses,filePrefix + "testing.csv");
    }

    private static void makeNets()
    {
        ArrayList<Node> input1master = new ArrayList<>();
        ArrayList<Node> input2master = new ArrayList<>();

        //making net arrays
        inputs1 = new ArrayList[NUM_INPUTS];
        inputs2 = new ArrayList[NUM_INPUTS];

        //make array lists
        for(int i = 0; i < NUM_INPUTS; i++)
        {
            inputs1[i] = new ArrayList<>();
            inputs2[i] = new ArrayList<>();
        }

        //now add nodes for categories
        for(int i = 0; i < mainCatList.size(); i++)
        {
            InputNode one = new InputNode(sumFunction,tanhActivation);
            InputNode two = new InputNode(sumFunction,tanhActivation);
            inputs1[CATEGORY].add(one);
            input1master.add(one);
            inputs2[CATEGORY].add(two);
            input2master.add(two);
        }

        //now adding nodes for currency types
        for(int i = 0; i < currencyList.size(); i++)
        {
            InputNode one = new InputNode(sumFunction,tanhActivation);
            InputNode two = new InputNode(sumFunction,tanhActivation);
            inputs1[CURRENCY].add(one);
            input1master.add(one);
            inputs2[CURRENCY].add(two);
            input2master.add(two);
        }

        //Now adding nodes for time
        {
            InputNode one = new InputNode(sumFunction,tanhActivation);
            InputNode two = new InputNode(sumFunction,tanhActivation);
            inputs1[TIME].add(one);
            input1master.add(one);
            inputs2[TIME].add(two);
            input2master.add(two);
        }

        //Now adding nodes for goal
        {
            InputNode one = new InputNode(sumFunction,tanhActivation);
            InputNode two = new InputNode(sumFunction,tanhActivation);
            inputs1[GOAL].add(one);
            input1master.add(one);
            inputs2[GOAL].add(two);
            input2master.add(two);
        }

        //finally, countries
        for(int i = 0; i < countryList.size(); i++)
        {
            InputNode one = new InputNode(sumFunction,tanhActivation);
            InputNode two = new InputNode(sumFunction,tanhActivation);
            inputs1[COUNTRY].add(one);
            input1master.add(one);
            inputs2[COUNTRY].add(two);
            input2master.add(two);
        }

        //Output nodes
        Node output1 = new Node(sumFunction,sigmoidActivation);
        ArrayList<Node> outList1 = new ArrayList<>();
        outList1.add(output1);
        Node output2 = new Node(sumFunction,sigmoidActivation);
        ArrayList<Node> outList2 = new ArrayList<>();
        outList2.add(output2);

        //Making layers
        Layer inputLayer1 = new Layer(input1master);
        Layer inputLayer2 = new Layer(input2master);
        Layer outputLayer1 = new Layer(outList1);
        Layer outputLayer2 = new Layer(outList2);

        //sizes
        int[] sizes1 = {16,4};
        int[] sizes2 = {30,16,4};

        //finally, making nets
        netWork1 = NetMaker.makeNet(inputLayer1,outputLayer1,sumFunction,tanhActivation,sizes1);
        netWork2 = NetMaker.makeNet(inputLayer2,outputLayer2,sumFunction,tanhActivation,sizes2);
    }

    private static void setInputs(ArrayList<InputNode>[] inputs1, DataEntry entry)
    {
        //First, handle main category
        String mainCat = entry.at("main_category");
        for(int i = 0; i < mainCatList.size(); i++)
        {
            if(mainCatList.get(i).equals(mainCat))
            {
                //Mark the node as 1 if it corresponds
                inputs1[CATEGORY].get(i).setValue(1.0);
            }
            else
            {
                //Else it is zero
                inputs1[CATEGORY].get(i).setValue(0.0);
            }
        }

        //Next, currency
        String currency = entry.at("currency");
        for(int i = 0; i < currencyList.size(); i++)
        {
            if(currencyList.get(i).equals(currency))
            {
                //Mark it a 1 for a match
                inputs1[CURRENCY].get(i).setValue(1.0);
            }
            else
            {
                inputs1[CURRENCY].get(i).setValue(0.0);
            }
        }

        //Next up, length of time for the campaign
        {
            String end_date = entry.at("deadline");
            String start_date = entry.at("launched");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");


            try
            {
                Date end = format.parse(end_date);
                Date start = format.parse(start_date);
                double timeElapsed = scaleTime(end.getTime() - start.getTime(), -2.0, 2.0);
                inputs1[TIME].get(0).setValue(timeElapsed);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        //Time to handle goal amount
        {
            double goalInput = Double.parseDouble(entry.at("usd_goal_real"));
            goalInput = scaleGoal(goalInput, -2.0, 2.0);
            inputs1[GOAL].get(0).setValue(goalInput);
        }

        //Finally, country
        String country = entry.at("country");
        for(int i = 0; i < countryList.size(); i++)
        {
            if(countryList.get(i).equals(country))
            {
                //Mark it a 1 for a match
                inputs1[COUNTRY].get(i).setValue(1.0);
            }
            else
            {
                inputs1[COUNTRY].get(i).setValue(0.0);
            }
        }
    }

    //scales a goal value to be between two given bounds
    private static double scaleGoal(double inputGoal, double left, double right)
    {
        double bottom = MAX_GOAL - MIN_GOAL;
        double top = (inputGoal - MIN_GOAL) * (right - left);
        return left + top/bottom;
    }

    //scales a goal value to be between two given bounds
    private static double scaleTime(double inputTime, double left, double right)
    {
        double bottom = MAX_TIME - MIN_TIME;
        double top = (inputTime - MIN_TIME) * (right - left);
        return left + top/bottom;
    }

    //writes results to indicated file
    private static void writeResults(ArrayList<Double> answers, ArrayList<Double> guesses, String fileName)
    {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter( new FileWriter(fileName))))
        {
            writer.write("Actual,Guessed\n");
            for(int i = 0; i < answers.size(); i++)
            {
                writer.write("" + answers.get(i) +  "," + guesses.get(i) + "\n");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

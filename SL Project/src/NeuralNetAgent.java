import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Robert on 11/18/2018.
 *
 * This agent should utilize a neural net to learn how to predict successful startups
 */
//TODO: WHole class will need a rework soon
public class NeuralNetAgent extends Agent
{
    private Net net;
    private InputNode input1;
    private InputNode input2;
    private InputNode input3;

    public NeuralNetAgent()
    {
        super();
        //for now, 3 nodes
        ArrayList<Node> nodes = new ArrayList<>();
        input1 = new InputNode(new WeightedSumFunction(), new TanhActivation());
        nodes.add(input1);
        input2 = new InputNode(new WeightedSumFunction(), new TanhActivation());
        nodes.add(input2);
        input3 = new InputNode(new WeightedSumFunction(), new TanhActivation());
        nodes.add(input3);
        Layer inputLayer = new Layer(nodes);

        ArrayList<Node> nodes2 = new ArrayList<>();
        nodes2.add(new Node(new WeightedSumFunction(), new TanhActivation()));
        nodes2.add(new Node(new WeightedSumFunction(), new TanhActivation()));
        Layer outputLayer = new Layer(nodes2);

        int[] arr = {4};
        net = NetMaker.makeNet(inputLayer,outputLayer,new WeightedSumFunction(), new TanhActivation(), arr);
    }

    @Override
    public double[] learn(DataFrame data, String targetLabel)
    {
        double[] results = new double[data.size()];
//        for(int i = 0; i < data.size(); i++)
//        {
//            DataEntry entry = data.at(i);
//
//            String input_1 = entry.at("usd_goal_real");
//            String input_2 = entry.at("backers");
//
//            String end_date = entry.at("deadline");
//            String start_date = entry.at("launched");
//
//            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//
//            try
//            {
//                Date end = format.parse(end_date);
//                Date start = format.parse(start_date);
//                input3.setValue(end.getTime()-start.getTime());
//                input1.setValue(Double.parseDouble(input_1));
//                input2.setValue(Double.parseDouble(input_2));
//            }
//            catch (Exception e)
//            {
//                //TODO: Actualy handle stuff
//                System.out.println("WHOOPS");
//            }
//
//            double[] answers = new double[2];
//            if(entry.at("state").equals("successful"))
//            {
//                answers[0] = 0.0;
//                answers[1] = 1.0;
//            }
//            else
//            {
//                answers[1] = 0.0;
//                answers[0] = 1.0;
//            }
//
//            double[] arr = net.learn(answers,0.01,new TanhActivation());
//            results[i] = getAnswer(arr);
//        }

        return results;
    }

    @Override
    public double[] evaluate(DataFrame data)
    {
        double[] results = new double[data.size()];
        for(int i = 0; i < data.size(); i++)
        {
            DataEntry entry = data.at(i);

            String input_1 = entry.at("usd_goal_real");
            String input_2 = entry.at("backers");

            String end_date = entry.at("deadline");
            String start_date = entry.at("launched");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");


            try
            {
                Date end = format.parse(end_date);
                Date start = format.parse(start_date);
                input3.setValue(end.getTime()-start.getTime());
                input1.setValue(Double.parseDouble(input_1));
                input2.setValue(Double.parseDouble(input_2));
            }
            catch (Exception e)
            {
                //TODO: Actualy handle stuff
                System.out.println("WHOOPS");
            }

            double[] arr = net.getResult();
            results[i] = getAnswer(arr);
        }

        return results;
    }

//    //Evaluates cost
//    private double calculateCost(double[] guess, double[] actual)
//    {
//        double cost = 0.0;
//        for(int i = 0; i < guess.length; i++)
//        {
//            cost += Math.pow(guess[i] - actual[i],2);
//        }
//        return cost/2;
//    }
    private double getAnswer(double[] arr)
    {
        if(arr[0] > arr[1])
        {
            return 0.0;
        }
        return 1.0;
    }
}

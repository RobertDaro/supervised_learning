/**
 * Created by Robert on 11/18/2018.
 *
 * This agent should utilize a neural net to learn how to predict successful startups
 */
public class NerualNetAgent extends Agent
{
    private int inNodeNum; //input layer node number
    private int fstNodeNum; //Number for first hidden layer
    private int scnNodeNum; //Number for second hidden layer
    private int outNodeNum; //Number for the output layer

    private Layer inputs;
    private Layer first;
    private Layer second;
    private Layer out;

    @Override
    public double[] learn(DataFrame data, String targetLabel)
    {
        return null;
    }

    @Override
    public double[] evaluate(DataFrame data)
    {
        return null;
    }
}

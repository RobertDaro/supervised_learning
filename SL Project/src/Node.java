import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/18/2018.
 */
public class Node
{

    //The connections to this node and from it
    protected List<Connection> inConnections;

    protected List<Connection> outConnections;

    //functions for handling input summation and activation
    protected InputFunction inFunc;

    protected ActivationFunction activation;

    protected double theta; //The weird part of the error

    protected double lastOutput; //The last output that this net returned, VERY IMPORTANT TO NOT CHANGE CARELESSLY TODO: This is wrong!

    public Node(InputFunction in, ActivationFunction out)
    {
        inConnections = new ArrayList<>();
        outConnections = new ArrayList<>();
        inFunc = in;
        activation = out;
    }

    public void addConnectionOut(Connection out)
    {
        outConnections.add(out);
    }

    public void addConnectionsIn(Connection in)
    {
        inConnections.add(in);
    }

    //Easy function to add a new connection with a given node
    public void connectTo(Node other)
    {
        Connection connect = new Connection(this,other);
        this.addConnectionOut(connect);
        other.addConnectionsIn(connect);
    }

    public List<Connection> getInConnections()
    {
        return inConnections;
    }

    public List<Connection> getOutConnections()
    {
        return outConnections;
    }

    //get the output of the Node
    public double calculateOut()
    {
        double totalInput = inFunc.collectOutput(inConnections);
        lastOutput = activation.calculateOutput(totalInput);
        return lastOutput;
    }

    //adjusts the weights with the given error value
    public void adjustWeights(double errorDerivative, double learningRate)
    {
        theta = errorDerivative * activation.derivativeOutput(inFunc.collectOutput(inConnections));

        for(Connection connection : inConnections)
        {
            //Start the calculations for left node thetas
            Node other = connection.getFromNode();
            other.backPropThetas();

            //Finally, adjust weight
            connection.setWeight(connection.getWeight() - learningRate * theta * other.lastOutput);

            //start weight adjustments
            other.adjustWeightsInternal(learningRate);
        }
    }

    //handles the propagation of error stuff backwards
    protected void backPropThetas()
    {
        double leftSide = 0;
        for(Connection connection : outConnections)
        {
            //This is summing up the thetas from the nodes that this node fed to
            Node other = connection.getToNode();
            double weight = connection.getWeight();
            leftSide += weight * other.theta;
        }
        theta = leftSide * activation.derivativeOutput(inFunc.collectOutput(inConnections));

        //Now that we have our theta, time to kickoff other theta calculations
        for(Connection connection : inConnections)
        {
            //Start the calculations for left node thetas
            Node other = connection.getFromNode();
            other.backPropThetas();
        }
    }

    protected void adjustWeightsInternal(double learningRate)
    {
        //Now that we have our theta, time to adjust weights
        for(Connection connection : inConnections)
        {
            Node other = connection.getFromNode();

            //adjust weight
            connection.setWeight(connection.getWeight() - learningRate * theta * other.lastOutput);

            //Start the calculations for left node weights
            other.adjustWeightsInternal(learningRate);
        }
    }
}

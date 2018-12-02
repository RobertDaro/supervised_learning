import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/18/2018.
 *
 * Meant to represent layers in a neural network
 */
public class Layer
{
    protected List<Node> nodes;

    //no nodes constructor
    public Layer()
    {
        nodes = new ArrayList<>();
    }

    //predefined nodes constructor
    public Layer(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    //getter for nodes in this layer
    public List<Node> getNodes()
    {
        return nodes;
    }

    //retrieves the output of this layer as an array of doubles
    public double[] getResults()
    {
        double[] results = new double[nodes.size()];

        for(int i = 0; i < nodes.size(); i++)
        {
            results[i] = nodes.get(i).calculateOut();
        }

        return results;
    }

    //Starts the error adjustment process
    public void adjustWeights(double[] errorDerivatives, double learningRate)
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            nodes.get(i).adjustWeights(errorDerivatives[i],learningRate);
        }
    }
}

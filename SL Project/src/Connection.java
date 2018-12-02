/**
 * Created by Robert on 11/20/2018.
 */
public class Connection
{
    //the node we are coming from
    protected Node fromNode;

    //the node we are going to
    protected Node toNode;

    //the weight of this connection
    protected double weight;

    //this creates new connections
    public Connection(Node _fromNode, Node _toNode)
    {
        fromNode = _fromNode;
        toNode = _toNode;
        this.weight = 2 * (Math.random() * 2 - 1); //randomly assign weight when first hitting this
    }

    //this one specifies weight
    public Connection(Node _fromNode, Node _toNode, double _weight)
    {
        fromNode = _fromNode;
        toNode = _toNode;
        this.weight = _weight;
    }

    //getter for weight
    public double getWeight()
    {
        return weight;
    }

    //setter for weight
    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public double getInput()
    {
        return fromNode.calculateOut();
    }

    //this should be what we use most of the time, I think
    public double getWeightedInput()
    {
        return fromNode.calculateOut() * weight;
    }

    //getters for nodes
    public Node getFromNode()
    {
        return  fromNode;
    }

    public Node getToNode()
    {
        return toNode;
    }
}

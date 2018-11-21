/**
 * Created by Robert on 11/18/2018.
 */
public class Node
{
    private double[] weightsIn; //The weights coming from this node

    private double[] weightsOut;

    private double value; //The value at this node

    public Node(double[] in, double[] out)
    {
        value = Math.random() * 2 - 1;
        weightsIn = in;
        weightsOut = out;
    }

    //setter for the value
    public void setValue(double newVal)
    {
        value = newVal;
    }

    //getter for the value
    public double getValue()
    {
        return value;
    }
}

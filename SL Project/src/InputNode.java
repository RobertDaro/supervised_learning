/**
 * Created by Robert on 11/21/2018.
 */
public class InputNode extends Node
{
    double value;

    public InputNode(InputFunction in, ActivationFunction out)
    {
        super(in,out);
    }

//    public double getValue()
//    {
//        return value;
//    }

    public void setValue(double value)
    {
        this.value = value;
    }

    @Override
    //get the output of the Node
    public double calculateOut()
    {
        return value;
    }
}

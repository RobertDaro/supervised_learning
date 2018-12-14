import java.util.List;

/**
 * Created by Robert on 12/2/2018.
 */
public class BiasValueFunction implements InputFunction
{
    private double biasVal;
    public BiasValueFunction(double value)
    {
        biasVal = value;
    }
    @Override
    public double collectOutput(List<Connection> inputs)
    {
        return biasVal;
    }
}

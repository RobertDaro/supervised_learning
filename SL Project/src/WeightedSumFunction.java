import java.util.List;

/**
 * Created by Robert on 11/21/2018.
 */
public class WeightedSumFunction implements InputFunction
{
    @Override
    public double collectOutput(List<Connection> input)
    {
        double weightedSum = 0.0;
        for(Connection connection : input)
        {
            weightedSum += connection.getWeightedInput();
        }
        return weightedSum;
    }
}

/**
 * Created by Robert on 10/31/2018.
 *
 * This agent doesn't learn, and just guesses randomly on everything.
 */
public class RandomAgent extends Agent
{
    @Override
    public double[] learn(DataFrame data, String targetLabel)
    {
        double [] resultVals = new double[data.size()];
        for (int i = 0; i < resultVals.length; i++)
        {
            resultVals[i] = Math.random();
        }
        return resultVals;
    }

    @Override
    public double[] evaluate(DataFrame data)
    {
        double [] resultVals = new double[data.size()];
        for (int i = 0; i < resultVals.length; i++)
        {
            resultVals[i] = Math.random();
        }
        return resultVals;
    }
}

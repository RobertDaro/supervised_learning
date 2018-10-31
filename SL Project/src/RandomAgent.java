/**
 * Created by Robert on 10/31/2018.
 *
 * This agent doesn't learn, and just guesses randomly on everything.
 */
public class RandomAgent extends Agent
{
    @Override
    public double learn(DataEntry entry, String targetLabel)
    {
        return Math.random();
    }

    @Override
    public double evaluate(DataEntry entry)
    {
        return Math.random();
    }
}

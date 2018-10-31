/**
 * Created by Robert on 10/30/2018.
 *
 * Made to represent an agent during an experiment.
 */
public abstract class Agent
{

    public abstract double learn(DataEntry entry, String targetLabel);

    public abstract double evaluate(DataEntry entry);

}

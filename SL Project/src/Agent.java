/**
 * Created by Robert on 10/30/2018.
 *
 * Made to represent an agent during an experiment.
 */
public abstract class Agent
{

    public abstract double[] learn(DataFrame data, String targetLabel);

    public abstract double[] evaluate(DataFrame data);

}

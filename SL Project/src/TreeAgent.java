import sun.reflect.generics.tree.Tree;

/**
 * Created by James on 11/20/2018.
 *
 * This agent uses a decision tree to learn to predict probability of success
 */
public class TreeAgent extends Agent
{
    private RegressionTree tree;


    @Override
    public double learn(DataEntry entry, String targetLabel)
    {
        tree = RegressionTree(entry, targetLabel, )



        return Math.random();
    }

    @Override
    public double evaluate(DataEntry entry)
    {
        return Math.random();
    }


}

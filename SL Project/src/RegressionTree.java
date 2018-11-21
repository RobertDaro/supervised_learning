import java.util.ArrayList;

public class RegressionTree {
    private double rootVal;
    private ArrayList<RegressionTree> children;

    public RegressionTree(){


    }

    public RegressionTree(DataFrame data, String[] labels, double defaultVal){
        children = new ArrayList<RegressionTree>();
        if(data.size()==0)
        {
            rootVal = defaultVal;
        }
        else if(labels.length == 0)
        {
            rootVal = defaultVal;
        }
        else if(!data.getAll(data.getTargetValueLabel()).contains("successful"))
        {
            rootVal = 0;
        }
        else if(!data.getAll(data.getTargetValueLabel()).contains("failed") && !data.getAll(data.getTargetValueLabel()).contains("canceled"))
        {
            rootVal = 1;
        }
        else
        {

        }

    }

    //Method for growing the regression tree
    private  growTree()
    {

    }
}

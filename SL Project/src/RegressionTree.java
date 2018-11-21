/**
 * Created by James on 11/20/2018.
 *
 * implementation of a regression Tree
 */

import java.util.ArrayList;

public class RegressionTree {
    private Attribute root;
    private String rootLabel;
    private ArrayList<RegressionTree> children;
    private int depth;
    protected RegressionTree(){
        root = new Attribute();
        children= new ArrayList<>();
        depth = 0;
    }
    public RegressionTree(double val)
    {
        root = new Attribute(val);
        children= new ArrayList<>();
        depth = 0;
    }

    protected RegressionTree(DataFrame data, String[] labels, double defaultVal, int depth){
        children = new ArrayList<>();
        this.depth = depth;
        if(data.size()==0 || labels.length == 0)
        {
            root = new Attribute(defaultVal);
        }
        else if(!data.getAll(data.getTargetLabel()).contains("successful"))
        {
            root = new Attribute(0);
        }
        else if(!data.getAll(data.getTargetLabel()).contains("failed") && !data.getAll(data.getTargetLabel()).contains("canceled"))
        {
            root = new Attribute(1);
        }
        else if (depth > 5)
        {
            ArrayList<String> realAns = data.getAll(labels[labels.length-1]);
            double ExpVal = 0;
            for(int i = 0; i < realAns.size();i++)
            {
                ExpVal += Double.parseDouble(realAns.get(i));
            }
            ExpVal = ExpVal / realAns.size();
            root = new Attribute(ExpVal);
        }
        else
        {
            root = getBestAttribute(labels, data, depth);
            DataFrame leftChildData = new DataFrame(data.getLabelsAsString(), data.getTargetLabel());
            DataFrame rightChildData = new DataFrame(data.getLabelsAsString(), data.getTargetLabel());
            if(root.getIsClass())
            {
                for(int i = 0; i < data.size(); i++)
                {
                    DataEntry entry = data.at(i);
                    if(Double.parseDouble(entry.at(depth)) == (root.getVal()))
                    {
                        leftChildData.addEntry(entry.toString());
                    }
                    else
                    {
                        rightChildData.addEntry(entry.toString());
                    }
                }
            }
            else
            {
                for(int i = 0; i < data.size(); i++)
                {
                    DataEntry entry = data.at(i);
                    if(( Double.parseDouble(entry.at(depth)) <= (root.getVal())))
                    {
                        leftChildData.addEntry(entry.toString());
                    }
                    else
                    {
                        rightChildData.addEntry(entry.toString());
                    }
                }

            }
            children.add(new RegressionTree(leftChildData, labels, root.getVal(), depth+1));
            children.add(new RegressionTree(rightChildData, labels, root.getVal(), depth+1));

        }

    }

    //Method for growing the regression tree
    private  Attribute getBestAttribute(String[] labels, DataFrame data, int depth)
    {
        ArrayList<String> values = data.getAll(labels[depth]);
        if(depth == 0 || depth == 1 || depth == 2 || depth == 5)
        {

            return new Attribute(Double.parseDouble(values.get(0)), true);
        }
        else
        {
            double avg = 0;
            for(String s : values)
            {
                avg += Double.parseDouble(s);
            }
            avg = avg / values.size();
            return new Attribute(avg);
        }

    }
}

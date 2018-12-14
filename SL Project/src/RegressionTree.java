/**
 * Created by James on 11/20/2018.
 *
 * implementation of a regression Tree
 */

import java.util.ArrayList;

public class RegressionTree {
    private Attribute root;
    private ArrayList<RegressionTree> children;
    private int depth;
    protected RegressionTree(){
        root = new Attribute();
        children= new ArrayList<>();
        depth = 0;
    }
    public RegressionTree(double val, int index)
    {
        root = new Attribute(val, index);
        children= new ArrayList<>();
        depth = 0;
    }
    //Method for tree creation
    protected RegressionTree(DataFrame data, String[] labels, double defaultVal, int depth){
        children = new ArrayList<>();
        this.depth = depth;
        ArrayList<String> realAns = data.getAll(labels[6]);
        double ExpVal = 0;
        for(int i = 0; i < realAns.size();i++)
        {
            ExpVal += Double.parseDouble(realAns.get(i));
        }
        ExpVal = ExpVal / realAns.size();
        ArrayList<String> results = data.getAll(data.getTargetLabel());
        //If no data, give node default val
        if(data.size()==0)
        {
            root = new Attribute(ExpVal, 6);
        }
        //if given no successful entries, have 0 as result
        else if(!results.contains("1.0"))
        {
            root = new Attribute(0, 6);
        }
        else if(!results.contains("0.0"))
        {
            root = new Attribute(1, 6);
        }
        else if (depth > 4)
        {

            root = new Attribute(ExpVal, 6);
        }
        else
        {
            root = getAttribute(labels, data);
            DataFrame leftChildData = new DataFrame(data.getLabelsAsString(), data.getTargetLabel());
            DataFrame rightChildData = new DataFrame(data.getLabelsAsString(), data.getTargetLabel());
            if(root.getIsClass())
            {
                for(int i = 0; i < data.size(); i++)
                {
                    DataEntry entry = data.at(i);
                    if(Double.parseDouble(entry.at(root.getTargetIndex())) == (root.getVal()))
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
                    if(( Double.parseDouble(entry.at(root.getTargetIndex())) <= (root.getVal())))
                    {
                        leftChildData.addEntry(entry.toString());
                    }
                    else
                    {
                        rightChildData.addEntry(entry.toString());
                    }
                }

            }
            children.add(new RegressionTree(leftChildData, labels, ExpVal, depth+1));
            children.add(new RegressionTree(rightChildData, labels, ExpVal, depth+1));

        }

    }

    //Method for determining the attribute to split at
    private  Attribute getAttribute(String[] labels, DataFrame data)
    {
        int pick = (int)Math.round(Math.random() * (5));
//        int pick = 4;
        ArrayList<String> values = data.getAll(labels[pick]);
        if(pick == 0 || pick == 1 || pick == 2 || pick == 5)
        {
            int pivot = (int)(Math.random() * values.size());
            return new Attribute(Double.parseDouble(values.get(pivot)), pick,true);
        }
        else
        {
            double avg = 0;
            for(String s : values)
            {
                avg += Double.parseDouble(s); // / values.size());
            }
            avg = avg / values.size();
            return new Attribute(avg, pick);
        }

    }

    protected double assess(DataEntry entry)
    {
        if(children.size() == 0)
        {
            return root.getVal();
        }
        else{
            if(root.getIsClass())
            {
                if(Double.parseDouble(entry.at(root.getTargetIndex())) == root.getVal())
                {
                    return children.get(0).assess(entry);
                }
                else
                {
                    return children.get(1).assess(entry);
                }
            }
            else
            {
                if(Double.parseDouble(entry.at(root.getTargetIndex())) < root.getVal())
                {
                    return children.get(0).assess(entry);
                }
                else
                {
                    return  children.get(1).assess(entry);
                }
            }
        }
    }
}

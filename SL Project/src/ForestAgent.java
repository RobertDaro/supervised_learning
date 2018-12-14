import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by James on 11/20/2018.
 *
 * This agent uses multple decision trees to learn to predict probability of success
 */
public class ForestAgent extends Agent
{
    private ArrayList<RegressionTree> trees = new ArrayList<>();
    private DataFrame learnedData;
    private int numTrees;
    @Override
    public double[] learn(DataFrame data, String targetLabel)
    {

        DataFrame proData = processData(data, targetLabel);
        if(learnedData == null)
        {
            learnedData = proData;
        }
        else
        {
            for(int i = 0; i < proData.size(); i++)
            {
                learnedData.addEntry(proData.at(i).toString());
            }
        }

        int index;
        for( int i = 0; i < numTrees; i++)
        {
            DataFrame tempFrame = new DataFrame(learnedData.getLabelsAsString(), learnedData.getTargetLabel());
            for(int j = 0; j < data.size(); j++)
            {
                 index = (int)(Math.random() * learnedData.size());
                 tempFrame.addEntry(learnedData.at(index).toString());
            }
            trees.add(new RegressionTree(tempFrame,tempFrame.getLabels() , 0, 0));
        }
        double[] output = new double[learnedData.size()];
        for (int i = 0; i < learnedData.size(); i++)
        {
            DataEntry tempEntry = learnedData.at(i);
            double sum = 0;
           for(int j = 0 ; j < trees.size(); j++)
           {
            sum += trees.get(j).assess(tempEntry);

           }
           sum = sum / trees.size();
           output[i] = sum;

        }
        return output;



    }

    @Override
    public double[] evaluate(DataFrame data)
    {
        double[] output = new double[data.size()];
        DataFrame proData = processData(data, data.getTargetLabel());

        for (int i = 0; i < proData.size(); i++)
        {
            DataEntry tempEntry = proData.at(i);
            double sum = 0;
            for(int j = 0 ; j < trees.size(); j++)
            {
                sum += trees.get(j).assess(tempEntry) / trees.size();

            }
            output[i] = sum;
        }
        return output;
    }

    private double classify(ArrayList<String> list, String label)
    {
        if(!list.contains(label))
        {
            list.add(label);
        }
        return (double) list.indexOf(label);
    }

    private double stateEval(String state)
    {
        if(state.equals("successful"))
        {
            return 1.0;
        }
        else
        {
            return 0.0;
        }
    }

    private DataFrame processData(DataFrame data, String targetLabel)
    {
        //Processing raw data for use by regression Tree
        String proHeader = "category,main_category,currency,time,goal,country,state";
        String[] proLabels = new String[]{"category","main_category","currency","time","goal","country","state"};
        DataFrame proData = new DataFrame(proHeader, targetLabel);
        DataEntry entry;
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> mainCategories = new ArrayList<>();
        ArrayList<String> currencies = new ArrayList<>();
        ArrayList<String> countries = new ArrayList<>();
        for(int i = 0; i < data.size(); i++)
        {
            StringBuilder dataLine = new StringBuilder();
            entry = data.at(i);

            dataLine.append(classify(categories,entry.at("category"))); dataLine.append(",");
            dataLine.append(classify(mainCategories,entry.at("main_category"))); dataLine.append(",");
            dataLine.append(classify(currencies,entry.at("currency"))); dataLine.append(",");

            LocalDate end = LocalDate.parse(entry.at("deadline"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
            LocalDate start = LocalDate.parse(entry.at("launched"), formatter);
            int length = Period.between(start, end).getDays();
            dataLine.append(length); dataLine.append(",");

            dataLine.append(entry.at("goal")); dataLine.append(",");
            dataLine.append(classify(countries,entry.at("country"))); dataLine.append(",");

            dataLine.append(stateEval(entry.at("state")));

            proData.addEntry(dataLine.toString());

        }

        return proData;
    }

    protected void setNumTrees(int numTrees) {
        this.numTrees = numTrees;
    }
}

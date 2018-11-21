import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by James on 11/20/2018.
 *
 * This agent uses a decision tree to learn to predict probability of success
 */
public class TreeAgent extends Agent
{
    private RegressionTree tree;


    @Override
    public double[] learn(DataFrame data, String targetLabel)
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
            System.out.println(entry.at(0));
        }
        System.out.println(proData);

//      TODO  tree = new RegressionTree(proData, proLabels,0);

        return new double[]{};

    }

    @Override
    public double[] evaluate(DataFrame data)
    {
        return new double[]{};
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
}

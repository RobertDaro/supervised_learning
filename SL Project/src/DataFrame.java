import java.util.ArrayList;

/**
 * Created by Robert on 10/30/2018.
 * Edited by James on 11/20/2018
 *
 * Holds multiple entries, assumes uniform representation across lines
 */
public class DataFrame
{
    //used to keep track of what we are looking at
    private String[] labels;

    //used to keep track of our target variable
    private String targetValueLabel;

    //index of the target value in the data
    private int targetValueIndex;

    //list of entries
    private ArrayList<DataEntry> data;

    /*
    Literally just include the header and what you are looking for.

    The header should be in the format: x1,x2,...,x_final
     */
    public DataFrame(String header, String targetVal)
    {
        labels = header.split(",");
        targetValueLabel = targetVal;

        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i].equals(targetValueLabel))
            {
                targetValueIndex = i;
            }
        }

        data = new ArrayList<>();
    }

    //Alternative constructor
    public DataFrame(String[] _labels, String _targetValueLabel, int _targetValueIndex, ArrayList<DataEntry> _data)
    {
        labels = _labels;
        targetValueLabel = _targetValueLabel;
        targetValueIndex = _targetValueIndex;
        data = _data;
    }

    //add a new entry, represented as a line of form: x1,x2,...,x_final
    public void addEntry(String line)
    {
        data.add(new DataEntry(line, labels));
    }

    //return a list of all values given a label
    public ArrayList<String> getAll(String label)
    {
        ArrayList<String> resultList = new ArrayList<>();

        int index = -1;

        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i].equals(label))
            {
                index = i;
            }
        }

        //This only happens if it cannot find the given label
        if(index < 0)
        {
            return resultList;
        }

        for(DataEntry entry : data)
        {
            resultList.add(entry.at(index));
        }

        return resultList;
    }

    //gets the entry at a given index
    public DataEntry at(int index)
    {
        return data.get(index);
    }

    //gets length of the data set
    public int size()
    {
        return data.size();
    }

    //splits the DataFrame along a given index
    // returns tail, original keeps prefix
    public DataFrame splitAt(int index)
    {
        ArrayList<DataEntry> tail = new ArrayList<>();

        for(int i = index; i < data.size();)
        {
            tail.add(data.get(index));
            data.remove(index);
        }
        data.trimToSize();

        return new DataFrame(labels,targetValueLabel,targetValueIndex,tail);
    }

    //TODO: Change these strings (and in DataEntry) to String Builders or something. Strings are really slow.
    //toString, hopefully it's readable
    public String toString()
    {
        String str = "";
        for(String label: labels)
        {
            str = str + label + ", ";
        }
        str = str + "\n";
        for(DataEntry entry : data)
        {
            str = str + "\n" + entry;
        }
        return str;
    }
    //getter for target Value's label
    public String getTargetLabel() {
        return targetValueLabel;
    }

    //getter for labels
    public String[] getLabels() {
        return labels;
    }
    public String getLabelsAsString(){
        if (labels.length < 1)
        {
            return "";
        }
        StringBuilder ans = new StringBuilder();
        ans.append(labels[0]);
        for (int i = 1; i < labels.length; i++)
        {
            ans.append(","); ans.append(labels[i]);
        }
        return ans.toString();
    }
}

/**
 * Created by Robert on 10/30/2018.
 *
 * Object used to store a single line from a CSV
 */
public class DataEntry
{
    //labels for the data
    private String[] labels;

    //The actual values this line represents
    private String[] values;

    //constructor, takes a comma separated string and converts it into an entry
    // line should be of form x1,x2,...,x_final
    public DataEntry(String line, String[] labels)
    {
        values = line.split(",");
        this.labels = labels;
    }

    //gets the value at the specified index
    public String at(int index)
    {
        return values[index];
    }

    //gets the value by label, returns null if not found.
    public String at(String label)
    {
        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i].equals(label))
            {
                return values[i];
            }
        }

        return null;
    }

    //toString method for pretty printing
    public String toString()
    {
        String str = "";
        for(int i = 0; i < values.length - 1; i++)
        {
            str = str + values[i] +", ";
        }
        str = str + values[values.length-1];

        return str;
    }
}

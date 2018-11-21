/**
 * Created by James on 11/21/2018.
 *
 * Class for comparison Q's in decition tree nodes
 */
public class Attribute {
    private double val;
    private boolean isClass;

    public Attribute(){

    }
    public Attribute(double value){
        val = value;
        isClass = false;
    }
    public Attribute(double value, boolean isClass)
    {
        val = value;
        this.isClass = isClass;
    }

    public double getVal()
    {
        return val;
    }
    public boolean getIsClass()
    {
        return  isClass;
    }

}

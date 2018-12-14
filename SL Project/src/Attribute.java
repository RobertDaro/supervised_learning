/**
 * Created by James on 11/21/2018.
 *
 * Class for comparison Q's in decition tree nodes
 */
public class Attribute {
    private double val;
    private boolean isClass;
    private int targetIndex;

    public Attribute(){

    }
    public Attribute(double value, int index){
        val = value;
        isClass = false;
        targetIndex = index;
    }
    public Attribute(double value, int index, boolean isClass)
    {
        val = value;
        this.isClass = isClass;
        targetIndex = index;
    }

    public double getVal()
    {
        return val;
    }
    public boolean getIsClass()
    {
        return  isClass;
    }
    public int getTargetIndex()
    {
        return targetIndex;
    }
}

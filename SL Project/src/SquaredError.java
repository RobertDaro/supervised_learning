/**
 * Created by Robert on 12/1/2018.
 */
public class SquaredError implements ErrorFunction
{
    public double calcError(double trueAnswer, double guess)
    {
        double inner = trueAnswer - guess;
        return 0.5 * Math.pow(inner ,2);
    }

    //Simple stuff really
    public double calcErrorDerivative(double trueAnswer, double guess)
    {
        return guess - trueAnswer;
    }
}

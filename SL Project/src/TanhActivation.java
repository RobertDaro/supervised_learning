/**
 * Created by Robert on 11/21/2018.
 */
public class TanhActivation implements ActivationFunction
{
    //calculates the tanh of the input
    public double calculateOutput(double input)
    {
        return Math.tanh(input);
    }

    //calculates the derivative of tanh(input)
    public double derivativeOutput(double input)
    {
        return (1 - Math.pow(calculateOutput(input),2));
    }
}

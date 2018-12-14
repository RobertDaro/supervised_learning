/**
 * Created by Robert on 12/1/2018.
 */
public class SigmoidActivation implements ActivationFunction
{
    @Override
    public double calculateOutput(double input)
    {
        double bottom = 1 + Math.exp(-input);
        return 1/bottom;
    }

    @Override
    public double derivativeOutput(double input)
    {
        return calculateOutput(input) * (1 - calculateOutput(input));
    }
}

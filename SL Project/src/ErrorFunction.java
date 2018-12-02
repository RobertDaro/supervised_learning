/**
 * Created by Robert on 12/1/2018.
 */
public interface ErrorFunction
{
    double calcError(double trueAnswer, double guess);
    double calcErrorDerivative(double trueAnswer, double guess);
}

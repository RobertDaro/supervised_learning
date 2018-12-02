import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/21/2018.
 */
public class Net
{
    private Layer inputLayer;
    private List<Layer> hiddenLayers;
    private Layer outputLayer;

    //constructor that takes al the stuff needed

    public Net(Layer inputLayer, List<Layer> hiddenLayers, Layer outputLayer)
    {
        this.inputLayer = inputLayer;
        this.hiddenLayers = hiddenLayers;
        this.outputLayer = outputLayer;
    }

    //gets the results of the current input
    public double[] getResult()
    {
        return outputLayer.getResults();
    }

    //returns results and also learns from mistakes
    public double[] learn(double[] answers, double learningRate, ErrorFunction func)
    {
        // these are our guesses for each layer
        double[] guesses = outputLayer.getResults();

        //Assigning error derivatives for each guess
        double[] errorDerivs = new double[guesses.length];

        for(int i = 0; i < guesses.length; i++)
        {
            errorDerivs[i] = func.calcErrorDerivative(answers[i], guesses[i]);
        }

        //Adjusting the network
        outputLayer.adjustWeights(errorDerivs,learningRate);

        return guesses;
    }
}

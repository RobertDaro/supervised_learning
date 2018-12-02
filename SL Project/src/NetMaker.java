import sun.rmi.server.Activation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/21/2018.
 */
public class NetMaker
{
    private NetMaker()
    {
        //enforces static behavior
    }

    public static Net makeNet(Layer input, Layer output, InputFunction inputFunction, ActivationFunction activation, int[] sizes)
    {
        //empty list to hold layers
        ArrayList<Layer> hiddenLayers = new ArrayList<>();

        //making layers, unconnected atm
        for(int i = 0; i < sizes.length; i++)
        {
            ArrayList<Node> nodes = new ArrayList<>();
            for(int k = 0; k < sizes[i]; k++)
            {
                nodes.add(new Node(inputFunction,activation));
            }

            //adding Layer
            hiddenLayers.add(new Layer(nodes));
        }

        //Connection time, first connecting input to hidden layers
        connectLayers(input,hiddenLayers.get(0),activation);

        //Now connect each hidden layer to it's neighbor
        for(int i = 0; i < hiddenLayers.size()-1; i++)
        {
            connectLayers(hiddenLayers.get(i),hiddenLayers.get(i+1),activation);
        }

        //connecting last hidden layer to the output
        connectLayers(hiddenLayers.get(hiddenLayers.size()-1), output);

        Net net = new Net(input,hiddenLayers,output);
        return net;
    }

    //Function to connect layers together, also adds bias nodes
    private static void connectLayers(Layer left, Layer right)
    {
        for(Node node : left.getNodes())
        {
            //now to connect everything
            for(Node other : right.getNodes())
            {
                if(other.activation instanceof BiasValueFunction)
                {
                    //do not connect to a bias
                }
                else
                {
                    //form the connection
                    node.connectTo(other);
                }
            }
        }
    }

    private static void connectLayers(Layer left, Layer right, ActivationFunction activation)
    {
        for(Node node : left.getNodes())
        {
            //now to connect everything
            for(Node other : right.getNodes())
            {
                if(other.activation instanceof BiasValueFunction)
                {
                    //do not connect to a bias
                }
                else
                {
                    //form the connection
                    node.connectTo(other);
                }
            }
        }
        //adding a bias
        Node bias = new Node(new BiasValueFunction(2 * (Math.random() * 2 - 1)),activation);
        right.getNodes().add(bias);
    }
}

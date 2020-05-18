package ai;

import java.io.Serializable;

public class Layer implements Serializable {
	
	//CHAMPS ...
	public int 		Length;
	public Neuron 	Neurons[];


	public Layer(int l, int prev) {
		Length = l;
		Neurons = new Neuron[l];
		
		for (int j = 0; j < Length; j++) {
			Neurons[j] = new Neuron(prev);
		}
	}
}

class Neuron implements Serializable {
	
	//CHAMPS ...
	public double[]		weights;
	public double		bias;
	public double		delta;
	public double		value;


	public Neuron(int prevLayerSize) {
		weights = new double[prevLayerSize];
		
		bias = Math.random() / 10_000_000_000_000.0;
		delta = Math.random() / 10_000_000_000_000.0;
		value = Math.random() / 10_000_000_000_000.0;
		
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random() / 10_000_000_000_000.0;
		}
	}
}
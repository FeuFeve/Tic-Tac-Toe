package Game.JavaAI;

import java.util.Arrays;
import java.util.HashMap;

public class Test {

	//CHAMPS ...
	public static HashMap<double[], double[]> mapTrain;
	public static HashMap<double[], double[]> mapTest;
	public static HashMap<double[], double[]> mapDev;


	public static void main(String[] args) {
		try {
//			Trainer.reset();
//			Trainer.generateGame();
			trainAI();
//			test();
		} 
		catch (Exception e) {
			System.out.println("Test.main()");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void trainAI() {
		double startTime = System.nanoTime();
		try {
			int[] layers = new int[]{ 9, 9, 9 };

			double error = 0.0 ;
			MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
			double samples = 100_000_000 ;

			//TRAINING ...
			for (int i = 0; i < samples; i++) {
				do {
					Trainer.reset();
					Trainer.generateGame();
				} while (Trainer.winner == 0);

				for (int j = 0; j < Trainer.states.size(); j++) {

					double[] inputs = Trainer.states.get(j);
					double[] outputs = new double[inputs.length];
					Arrays.fill(outputs, 0);

					try {
						if (Trainer.winner == Trainer.ai) {
							outputs[Trainer.nextTile.get(j)] = 1;
						}
						else {
							outputs[Trainer.nextTile.get(j)] = 0;
						}
					} catch (IndexOutOfBoundsException e) {
						continue;
					}

					error += net.backPropagate(inputs, outputs);
				}

				if ( i % 1_000_000 == 0) {
					System.out.println("Error at step " + i + " is " + (error/(double)i));
				}
			}

			error /= samples ;
			System.out.println("Error is " + error);

			System.out.println("Learning completed!");
		}
		catch (Exception e) {
			System.out.println("Test.test()");
			e.printStackTrace();
			System.exit(-1);
		}
		double endTime = System.nanoTime();
		System.out.println("Done in: " + (endTime - startTime) / 1_000_000_000);
	}

	public static void test() {
		try {
			int[] layers = new int[]{ 2, 5, 1 };
			
			double error = 0.0 ;
			MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
			double samples = 100 ;

			//TRAINING ...
			for (int i = 0; i < samples; i++) {
				double[] inputs = new double[]{Math.round(Math.random()), Math.round(Math.random())};
				System.out.println("Input: " + inputs[0] + " " + inputs[1]);
				double[] output = new double[1];

				if ((inputs[0] == 1.0) || (inputs[1] == 1.0))
					output[0] = 1.0;
				else
					output[0] = 0.0;
				
				error += net.backPropagate(inputs, output);

				if ( i % 100_000 == 0 ) System.out.println("Error at step " + i + " is " + (error/(double)i));
			}

			error /= samples ;
			System.out.println("Error is " + error);

			System.out.println("Learning completed!");

			//TEST ...
			double[] inputs = new double[]{0.0, 1.0};
			double[] output = net.forwardPropagation(inputs);

			System.out.println(inputs[0] + " or " + inputs[1] + " = " + Math.round(output[0]) + " (" + output[0] + ")");
		} 
		catch (Exception e) {
			System.out.println("Test.test()");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
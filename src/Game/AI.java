package Game;

import Game.JavaAI.MultiLayerPerceptron;
import Game.JavaAI.SigmoidalTransferFunction;
import Game.JavaAI.Trainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

class AI {

    static int difficultyLevel;
    static String mediumAIPath = "JavaAI/medium_AI.ser";
    static String hardAIPath = "JavaAI/hard_AI.ser";


    static int play(int availableTiles) {
        return ThreadLocalRandom.current().nextInt(0, availableTiles);
    }

    static void initMediumAndHardAIs() {
        // Create the dir if it does not exist
        new File("JavaAI").mkdirs();

        // Train the medium AI if it's not already done
        File mediumAI = new File(mediumAIPath);
        if (!mediumAI.exists()) {
            train(mediumAIPath, 1_000_000, true);
        }

        // Copy the medium AI to the "hard AI" if there is none
        File hardAI = new File(hardAIPath);
        if (!hardAI.exists()) {
            try {
                Files.copy(mediumAI.toPath(), hardAI.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) {  }

            // Train a bit more the hard AI
            train(hardAIPath, 1_000_000, false);
        }
    }

    static void train(String path, int epochs, boolean overridePrevious) {
        double startTime = System.nanoTime();

        System.out.println("------------------------------");
        System.out.println("--         TRAINING         --");
        System.out.println("------------------------------");

        // Format big numbers into more readable ones
        Locale locale  = new Locale("en", "US");
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        System.out.println("Beginning training for " + decimalFormat.format(epochs) + " epochs...");

        // Beginning
        try {
            int[] layers = new int[]{ 9, 9, 9 };

            double error = 0.0 ;

            // Create a new multi layer perceptron if we want to override the previous
            MultiLayerPerceptron net;
            if (overridePrevious) {
                net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
            }
            // Else, load the previous (if there is one) and continue the training from where we left
            else {
                net = MultiLayerPerceptron.load(path);
                if (net == null) {
                    net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
                }
            }

            //TRAINING ...
            for (int i = 0; i < epochs; i++) {
                // Generate a game where either the AI or the player has won
                do {
                    Trainer.reset();
                    Trainer.generateGame();
                } while (Trainer.winner == 0);

                // Train the AI (not) to reproduce each step depending on if it has won or not
                for (int j = 0; j < Trainer.states.size(); j++) {

                    // Give the AI each step as an input
                    double[] inputs = Trainer.states.get(j);
                    double[] outputs = new double[inputs.length];
                    Arrays.fill(outputs, 0);

                    try {
                        if (Trainer.winner == Trainer.ai) {
                            outputs[Trainer.nextTile.get(j)] = 1;
                        }
                        // Following else is to uncomment only if we do not fill the outputs with '0', otherwise it's useless
//                        else {
//                            outputs[Trainer.nextTile.get(j)] = 0;
//                        }
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                    error += net.backPropagate(inputs, outputs);
                }

                if ( i % (epochs / 100) == 0) {
                    System.out.println("Error at step " + decimalFormat.format(i) + " is " + (error/(double)i));
                }
            }

            error /= epochs ;
            System.out.println("Error is " + error);

            System.out.println("Learning completed!");

            // Save the training
            net.save(path);
        }
        catch (Exception e) {
            System.out.println("AI.train()");
            e.printStackTrace();
            System.exit(-1);
        }

        double endTime = System.nanoTime();
        System.out.println("Done in: " + (endTime - startTime) / 1_000_000_000);
    }
}

package Game;

import java.util.concurrent.ThreadLocalRandom;

class AI {

    static int difficultyLevel;


    static int play(int availableTiles) {
        return ThreadLocalRandom.current().nextInt(0, availableTiles);
    }
}

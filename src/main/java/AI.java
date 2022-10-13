import java.util.Random;

public class AI {
    private int size;

    public AI(int size) {
        this.size = size;
    }

    public int aiNewSet(char[] gameBoard) {
        boolean choice = false;
        int pos = -1;
        while (!choice) {
            int posChoise = getRandom(size*size-1);
            if (gameBoard[posChoise] == '\u0000') {
                pos = posChoise;
                choice = true;
            }
        }
        return pos;
    }

    public int getRandom(int length) {
        return new Random().nextInt(length);
    }

}

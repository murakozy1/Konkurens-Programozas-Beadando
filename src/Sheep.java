import java.util.ArrayList;
import java.util.Random;

public class Sheep implements Runnable{
    private final int num;
    private int iPos;
    private int jPos;
    private Object[][] farmGrid;
    private Farm farm;
    private int delay;

    private enum DIRECTION {
        left,
        right,
        up,
        down,
        downLeft,
        downRight,
        upLeft,
        upRight
    }
    private enum WOLFDIRECTION {
        left,
        right,
        up,
        down,
        none
    }

    public Sheep(int num, int iPos, int jPos, Object[][] farmGrid, Farm farm, int delay){
        this.num = num;
        this.iPos = iPos;
        this.jPos = jPos;
        this.farmGrid = farmGrid;
        this.farm = farm;
        this.delay = delay;
    }

    @Override
    public String toString(){
        return String.valueOf((char) ('A' + (num)));
    }

    public void move(){
        synchronized (farm.farmGrid){
            int iPostmp;
            int jPostmp;
            ArrayList<DIRECTION> validMoves = new ArrayList<>();

            if (wolfRadar() == WOLFDIRECTION.none){
                for (DIRECTION dir : DIRECTION.values()) {
                    iPostmp = iPos;
                    jPostmp = jPos;
                    switch (dir) {
                        case left: jPostmp--; break;
                        case right: jPostmp++; break;

                        case up: iPostmp--; break;
                        case upLeft: iPostmp--; jPostmp--; break;
                        case upRight: iPostmp--; jPostmp++; break;

                        case down: iPostmp++; break;
                        case downLeft: iPostmp++; jPostmp--; break;
                        case downRight: iPostmp++; jPostmp++; break;
                    }
                    if ((iPostmp >= 0 && iPostmp < farm.GRIDHEIGHT) &&
                       (jPostmp >= 0 && jPostmp < farm.GRIDWIDTH) &&
                       (farmGrid[iPostmp][jPostmp] instanceof Empty || farmGrid[iPostmp][jPostmp] instanceof Gate)) {
                            validMoves.add(dir);
                    }
                }
            }
            else{
                switch (wolfRadar()){
                    case up:
                        //also 3 kozul valamelyik:
                        if (farm.farmGrid[iPos+1][jPos-1] instanceof Empty){ validMoves.add(DIRECTION.downLeft); }
                        if (farm.farmGrid[iPos+1][jPos] instanceof Empty){ validMoves.add(DIRECTION.down); }
                        if (farm.farmGrid[iPos+1][jPos+1] instanceof Empty){ validMoves.add(DIRECTION.downRight); }
                        break;
                    case down:
                        //folso 3 kozul valamelyik:
                        if (farm.farmGrid[iPos-1][jPos-1] instanceof Empty){ validMoves.add(DIRECTION.upLeft); }
                        if (farm.farmGrid[iPos-1][jPos] instanceof Empty){ validMoves.add(DIRECTION.up); }
                        if (farm.farmGrid[iPos-1][jPos+1] instanceof Empty){ validMoves.add(DIRECTION.upRight); }
                        break;
                    case left:
                        //jobb 3 kozul valamelyik:
                        if (farm.farmGrid[iPos-1][jPos+1] instanceof Empty){ validMoves.add(DIRECTION.upRight); }
                        if (farm.farmGrid[iPos][jPos+1] instanceof Empty){ validMoves.add(DIRECTION.right); }
                        if (farm.farmGrid[iPos+1][jPos+1] instanceof Empty){ validMoves.add(DIRECTION.downRight); }
                        break;
                    case right:
                        //bal 3 kozul valamelyik:
                        if (farm.farmGrid[iPos-1][jPos-1] instanceof Empty){ validMoves.add(DIRECTION.upLeft); }
                        if (farm.farmGrid[iPos][jPos-1] instanceof Empty){ validMoves.add(DIRECTION.left); }
                        if (farm.farmGrid[iPos+1][jPos-1] instanceof Empty){ validMoves.add(DIRECTION.downLeft); }
                        break;
                    default: break;
                }
            }

            if (!validMoves.isEmpty()) {
                DIRECTION move = validMoves.get(new Random().nextInt(validMoves.size()));
                farmGrid[iPos][jPos] = new Empty();
                switch (move) {
                    case left: jPos--; break;
                    case right: jPos++; break;

                    case up: iPos--; break;
                    case upLeft: iPos--; jPos--; break;
                    case upRight: iPos--; jPos++; break;

                    case down: iPos++; break;
                    case downLeft: iPos++; jPos--; break;
                    case downRight: iPos++; jPos++; break;
                }
            }

            if (farmGrid[iPos][jPos] instanceof Gate) {
                farm.declareSheepWin();
                System.out.println(this + ". Sheep got out! Simulation: COMPLETE...");
            }

            if (!farm.hasSheepWon()) farmGrid[iPos][jPos] = this;
        }
    }

    private WOLFDIRECTION wolfRadar(){
        for (int i = 0; i < 3; i++){ //up
            if (farmGrid[iPos-1][jPos + (i - 1)] instanceof Wolf){
                return WOLFDIRECTION.up;
            }
        }
        for (int i = 0; i < 3; i++){ //down
            if (farmGrid[iPos+1][jPos + (i - 1)] instanceof Wolf){
                return WOLFDIRECTION.down;
            }
        }
        for (int i = 0; i < 3; i++){ //left
            if (farmGrid[iPos + (i - 1)][jPos - 1] instanceof Wolf){
                return WOLFDIRECTION.left;
            }
        }
        for (int i = 0; i < 3; i++){ //right
            if (farmGrid[iPos + (i - 1)][jPos + 1] instanceof Wolf){
                return WOLFDIRECTION.right;
            }
        }
        return WOLFDIRECTION.none;
    }

    @Override
    public void run() {
        while (!farm.hasSheepWon()) {
            this.move();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

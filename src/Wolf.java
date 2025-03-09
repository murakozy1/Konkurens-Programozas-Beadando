import java.util.ArrayList;
import java.util.Random;

public class Wolf implements Runnable{

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
        down
    }

    public Wolf(int num, int iPos, int jPos, Object[][] farmGrid, Farm farm, int delay){
        this.num = num + 1;
        this.iPos = iPos;
        this.jPos = jPos;
        this.farmGrid = farmGrid;
        this.farm = farm;
        this.delay = delay;
    }

    public void move(){
        synchronized (farm.farmGrid){
            int iPostmp;
            int jPostmp;
            ArrayList<DIRECTION> validMoves = new ArrayList<>();


            for (DIRECTION dir : DIRECTION.values()) {
                iPostmp = iPos;
                jPostmp = jPos;
                switch (dir) {
                    case up: iPostmp--; break;
                    case down: iPostmp++; break;
                    case left: jPostmp--; break;
                    case right: jPostmp++; break;
                }
                if (iPostmp >= 0 && iPostmp < farm.GRIDHEIGHT &&
                        jPostmp >= 0 && jPostmp < farm.GRIDWIDTH &&
                        (farmGrid[iPostmp][jPostmp] instanceof Empty) && !(inSheepZone(iPostmp, jPostmp))) {
                    validMoves.add(dir);
                }
            }


            if (!validMoves.isEmpty()) {
                DIRECTION move = validMoves.get(new Random().nextInt(validMoves.size()));
                farmGrid[iPos][jPos] = new Empty();
                switch (move) {
                    case up: iPos--; break;
                    case down: iPos++; break;
                    case left: jPos--; break;
                    case right: jPos++; break;
                }
            }

            if (!farm.hasSheepWon()) farmGrid[iPos][jPos] = this;
        }
    }

    private boolean inSheepZone(int i, int j){
        if (i >= farm.sheepZoneStarti && i < farm.sheepZoneEndi){
            if (j >= farm.sheepZoneStartj && j < farm.sheepZoneEndj){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return Integer.toString(this.num);
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

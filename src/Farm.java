import java.util.ArrayList;
import java.util.Random;

public class Farm {

    public final Object[][] farmGrid;
    ArrayList<Sheep> sheepList = new ArrayList<>();
    ArrayList<Wolf> wolfList = new ArrayList<>();

    public final int GRIDHEIGHT;
    public final int GRIDWIDTH;
    private volatile boolean sheepWon = false;
    int sheepZoneStarti;
    int sheepZoneStartj;
    int sheepZoneEndi;
    int sheepZoneEndj;
    private int animalDelay;

    public Farm(int height, int width, int numberOfSheep, int numberOfWolves, int animalDelay){ //height: hany sora van, vagyis az i || width: hany oszlopa van, vagyis a j
        farmGrid = new Object[height][width];
        GRIDHEIGHT = height;
        GRIDWIDTH = width;
        this.animalDelay = animalDelay;

        int[] leftGatePosition = {0, 0};
        int[] rightGatePosition = {0, 0};
        int[] topGatePosition = {0, 0};
        int[] bottomGatePosition = {0, 0};

        leftGatePosition[0] = (int)(Math.random() * (height - 3 + 1)) + 1;
        rightGatePosition[0] = (int)(Math.random() * (height - 3 + 1)) + 1;
        topGatePosition[1] = (int)(Math.random() * (width - 3 + 1)) + 1;
        bottomGatePosition[1] = (int)(Math.random() * (width - 3 + 1)) + 1;

        rightGatePosition[1] = width - 1;
        bottomGatePosition[0] = height - 1;

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                farmGrid[i][j] = new Empty();
            }
        }

        for (int i = 0; i < width; i++){
            farmGrid[0][i] = new Wall();
            farmGrid[height-1][i] = new Wall();
        }

        for (int i = 0; i < height; i++){
            farmGrid[i][0] = new Wall();
            farmGrid[i][width-1] = new Wall();
        }

        farmGrid[leftGatePosition[0]][leftGatePosition[1]] = new Gate();
        farmGrid[rightGatePosition[0]][rightGatePosition[1]] = new Gate();
        farmGrid[topGatePosition[0]][topGatePosition[1]] = new Gate();
        farmGrid[bottomGatePosition[0]][bottomGatePosition[1]] = new Gate();

        sheepZoneStarti = ((height - 2) / 3) + 1;
        sheepZoneStartj = ((width - 2) / 3) + 1;
        sheepZoneEndi = sheepZoneStarti + ((height - 2) / 3); //az Endi és az Endj már excluded, pl 14x14-nél 9,9, de a 8,8 a sheepZone jobb alsó sarka
        sheepZoneEndj = sheepZoneStartj + ((width - 2) / 3);

        for (int i = 0; i < numberOfSheep; i++) {
            int sheepi = generateRandomSheepi(sheepZoneStarti, sheepZoneEndi);
            int sheepj = generateRandomSheepj(sheepZoneStartj, sheepZoneEndj);
            while (!(farmGrid[sheepi][sheepj] instanceof Empty)) {
                sheepi = generateRandomSheepi(sheepZoneStarti, sheepZoneEndi);
                sheepj = generateRandomSheepj(sheepZoneStartj, sheepZoneEndj);
            }
            farmGrid[sheepi][sheepj] = new Sheep(i, sheepi, sheepj, this.farmGrid, this, animalDelay);
            sheepList.add((Sheep) farmGrid[sheepi][sheepj]);
        }

        int wolvesSpawned = 0;
        Random random = new Random();
        while (wolvesSpawned < numberOfWolves){
            for (int i = 0; i < GRIDHEIGHT; i++){
                for (int j = 0; j < GRIDWIDTH; j++){
                    if ((this.farmGrid[i][j] instanceof Empty) && (!inSheepZone(sheepZoneStarti, sheepZoneEndi, sheepZoneStartj, sheepZoneEndj, i, j))) {
                        if ((random.nextInt(15) == 0) && (wolvesSpawned < numberOfWolves)) {
                            this.farmGrid[i][j] = new Wolf(wolvesSpawned, i, j, this.farmGrid, this, animalDelay);
                            wolfList.add((Wolf) farmGrid[i][j]);
                            wolvesSpawned++;
                        }
                    }
                }
            }
        }
    }

    public void printFarm(){
        for (int i = 0; i < GRIDHEIGHT; i++){
            for (int j = 0; j < GRIDWIDTH; j++){
                System.out.print(farmGrid[i][j]);
            }
            System.out.println();
        }
    }

    private int generateRandomSheepi(int sheepZoneStarti, int sheepZoneEndi){
        return new Random().nextInt((sheepZoneEndi - 1) - sheepZoneStarti + 1) + sheepZoneStarti;
    }

    private int generateRandomSheepj(int sheepZoneStartj, int sheepZoneEndj){
        return new Random().nextInt((sheepZoneEndj - 1) - sheepZoneStartj + 1) + sheepZoneStartj;
    }

    private boolean inSheepZone(int sheepZoneStarti, int sheepZoneEndi, int sheepZoneStartj, int sheepZoneEndj, int i, int j){
        if (i >= sheepZoneStarti && i < sheepZoneEndi){
            if (j >= sheepZoneStartj && j < sheepZoneEndj){
                return true;
            }
        }
        return false;
    }

    public void simulate(){
        ArrayList<Thread> sheepThreads = new ArrayList<>();
        ArrayList<Thread> wolfThreads = new ArrayList<>();

        for (Sheep sheep : sheepList) {
            Thread thread = new Thread(sheep, sheep.toString());
            sheepThreads.add(thread);
            thread.start();
        }

        for (Wolf wolf : wolfList) {
            Thread thread = new Thread(wolf, wolf.toString());
            wolfThreads.add(thread);
            thread.start();
        }

        while(!sheepWon){
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printFarm();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

        for (Thread thread : sheepThreads) {
            thread.interrupt();
        }

        for (Thread thread : wolfThreads) {
            thread.interrupt();
        }
    }

    public synchronized boolean hasSheepWon(){
        return this.sheepWon;
    }

    public synchronized void declareSheepWin(){
        this.sheepWon = true;
    }


}

package battleship;

import org.apache.commons.lang3.time.StopWatch;

public class GameTimer {
    public static final double MILLISECONDS_IN_SECOND = 1000.0;
    private StopWatch stopWatch;

    public GameTimer() {
        this.stopWatch = new StopWatch();
    }

    public void startTurn() {
        stopWatch.reset();
        stopWatch.start();
    }

    public void endTurn() {
        stopWatch.stop();
        long timeInMillis = stopWatch.getTime();
        System.out.println("Tempo gasto nesta jogada: " + timeInMillis + " ms (" + (timeInMillis / MILLISECONDS_IN_SECOND) + " segundos).");
    }
}

package wingman.dodger;


import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;

public class MainThread extends Thread {

    private final static int MAX_FPS = 60;
    private final static int MAX_FRAME_SKIPS = 2;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;
    private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
    private final static int STAT_INTERVAL = 1000;
    private final static int FPS_HISTORY_NR = 10;
    private long lastStatusStore = 0;
    private long statusIntervalTimer = 0l;
    private long totalFrameSkipped = 0l;
    private long framesSkippedPerStatCycle = 0l;
    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    private double fpsStore[];
    private long statsCount = 0;
    private double averageFps = 0.0;

    private boolean running;

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d("Alfred", "Starting game loop");
        initTimingElements();
        long beginTime;
        long timeDiff;
        int sleepTime;
        int framesSkipped;
        sleepTime = 0;
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;
                    //this.gamePanel.update();
                    this.gamePanel.render(canvas);
                    timeDiff = System.currentTimeMillis() - beginTime;
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }

                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        //this.gamePanel.update();
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }

                    if (framesSkipped > 0) {
                        Log.d("Alfred", "Skipped:" + framesSkipped);
                    }
                    framesSkippedPerStatCycle += framesSkipped;
                    storeStats();
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }


        }

    }

    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;
            statsCount++;

            double totalFps = 0.0;
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            if (statsCount < FPS_HISTORY_NR)
                averageFps = totalFps / statsCount;
            else
                averageFps = totalFps / FPS_HISTORY_NR;

            totalFrameSkipped += framesSkippedPerStatCycle;
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;
            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
            gamePanel.setAvgFps("FPS: " + df.format(averageFps));
        }
    }

    private void initTimingElements() {
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
        Log.d("Alfred .initTimingElements()", "Timing elements for status initialised");
    }

}

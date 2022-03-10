package app;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FileHandler {
    private String fileName;
    private File file;
    private FileWriter fileWriter;
    private FileReader fileReader;
    private double x1, x2, y1, y2, a1, a2, t;
    private long start = System.nanoTime();
    private Thread generatePointsThread;

    public FileHandler(String fileName) throws IOException {
        fileReader = new FileReader("/Users/mateusz.jamroz/IdeaProjects/NewDoublePendulumApp/src/app/data.txt");
        fileWriter = new FileWriter("/Users/mateusz.jamroz/IdeaProjects/NewDoublePendulumApp/src/app/data.txt");

    }

    public void writeData(DoublePendulum[] doublePendulums, int N) throws IOException {
        generatePointsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<N;i++) {
                    // FILE FORMAT: {x1} {y1} {x2} {y2} {a1} {a2} {t} ; {nextLinex1} {nextLinexy1} etc..
                    x1 = (double) Math.round(doublePendulums[i].getX1()*1000) / 1000;
                    y1 = (double) Math.round(doublePendulums[i].getY1()*1000) / 1000;
                    x2 = (double) Math.round(doublePendulums[i].getX2()*1000) / 1000;
                    y2 = (double) Math.round(doublePendulums[i].getY2()*1000) / 1000;
                    a1 = (double) Math.round(doublePendulums[i].getA1()*1000) / 1000;
                    a2 = (double) Math.round(doublePendulums[i].getA2()*1000) / 1000;
                    t =  (start - System.nanoTime() >> 10000)*-1 / (double) 1000;

                    try {
                        fileWriter.append(a1 + " " + a2 + " ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    fileWriter.append(t + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        generatePointsThread.start();
    }
}

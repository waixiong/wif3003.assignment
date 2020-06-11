/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wif3003;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keanb
 */
public class CPAssignment {

    /**
     * @param args the command line arguments
     */
    static volatile boolean done = false;
    static boolean sleep = false;

    public static void main(String[] args) {
        game(400, 10, 10, false);
    }

    public static void game(int n, int t, int m, boolean s) {
        if (n <= t){
            System.out.println("Number of points(n) must be greater than number of threads(t)");
            return;
        }
        
        sleep = s;
        
        long maxTime = System.currentTimeMillis() + m * 1000;

        Random random = new Random();

        Set<Point> pointSet = new HashSet<>(n);

        Map<Thread, Integer> threadCountMap = new HashMap<>();

        CountDownLatch mainLatch = new CountDownLatch(1);

        CyclicBarrier barrier = new CyclicBarrier(t);

        while (pointSet.size() < n) {
            int x = random.nextInt(1001);
            int y = random.nextInt(1001);
            Point point = new Point(x, y);
            pointSet.add(point);
        }

        int index = 0;
        Point[] points = new Point[pointSet.size()];
        for (Point point: pointSet){
            points[index] = point;
            index++;
        }

        for (int i = 0; i < t; i ++) {
            Thread thread = new Thread(() -> {

                int failCount = 0;
                try {

                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                while (true) {

                    synchronized (CPAssignment.class) {
                        if(done) return;
                        
                        Point firstPoint = points[random.nextInt(n)];
                        Point secondPoint = points[random.nextInt(n)];

//                        if(firstPoint.isUsed() || secondPoint.isUsed()) {
//                            failCount ++;
//                            continue;
//                        }
//
//                        firstPoint.setUsed(true);
//                        secondPoint.setUsed(true);
                        if(firstPoint.use() && secondPoint.use()) {
                            failCount ++;
                            
                            if (failCount == 20) {
                                if(!done) {
                                    mainLatch.countDown();
                                    done = true;
                                }
                                return;
                            }
                            continue;
                        }
                    }

                    Thread currentThread = Thread.currentThread();
                    threadCountMap.put(currentThread, threadCountMap.get(currentThread) + 1);
                }
            }, "thread-" + i);

            threadCountMap.put(thread, 0);
            thread.start();
        }

        try {

//            mainLatch.await(maxTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            boolean rsult = mainLatch.await(m * 1000, TimeUnit.MILLISECONDS);
            System.out.printf("Run : %b\n", rsult);
            done = true;
        } catch (InterruptedException e) {
            System.out.println("\tError");
            e.printStackTrace();
        }


        for(Map.Entry<Thread, Integer> entry : threadCountMap.entrySet()) {
            System.out.println(entry.getKey().getName() + " created count: " + entry.getValue());
        }
    }
}

interface DrawPointInterface {
    void draw(double x, double y);
}

interface DrawLineInterface {
    void draw(double x1, double y1, double x2, double y2, int c);
}

class CPGame {
    
    volatile boolean done = false;
    volatile int failCount = 0;
    int n, t, m;
    long maxTime;
    Random random;
    Set<Point> pointSet;
    Map<Thread, Integer> threadCountMap;
    CountDownLatch mainLatch;
    CyclicBarrier barrier;
    Point[] points;
    
    DrawPointInterface drawPoint;
    DrawLineInterface drawLine;
    
    String result = "";
    boolean sleep = false;
    
    public CPGame(int n, int t, int m, DrawPointInterface dp, DrawLineInterface dl, boolean sleep) {
        this.sleep = sleep;
        done = false;
        failCount = 0;
        drawPoint = dp;
        drawLine = dl;
        if (n <= t){
            t = n;
        }
        this.n = n;
        this.t = t;
        this.m = m;
        
        this.maxTime = System.currentTimeMillis() + m * 1000;

        this.random = new Random();

        this.pointSet = new HashSet<>(n);

        this.threadCountMap = new HashMap<>();

        this.mainLatch = new CountDownLatch(1);

        this.barrier = new CyclicBarrier(t);

        while (pointSet.size() < n) {
            int x = random.nextInt(1001);
            int y = random.nextInt(1001);
            Point point = new Point(x, y);
            pointSet.add(point);
        }

        int index = 0;
        points = new Point[pointSet.size()];
        for (Point point: pointSet){
            points[index] = point;
            if(drawPoint != null) {
                drawPoint.draw(point.getX(), point.getY());
            }
            index++;
        }
    }
    
    public void runConcurrentGame() {
        // try only 20 times
        for (int i = 0; i < t; i ++) {
//            System.out.printf("Thread %d\n", i);
            Thread thread = new Thread(() -> {
                
                // id - random number for color
                int id = new Random().nextInt(65536);
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // Try to link lines
                while (!done) {

                    synchronized (CPGame.class) {
                        // try only 20 times
                        if (failCount >= 20) {
                            if(!done) {
                                done = true;
                                System.out.println("fail 20 times");
                                mainLatch.countDown();
                            }
                            return;
                        }

                        Point firstPoint = points[random.nextInt(n)];
                        Point secondPoint;
//                        if(firstPoint.isUsed() || secondPoint.isUsed()) {
//                            failCount ++;
//                            continue;
//                        }
//
//                        firstPoint.setUsed(true);
//                        secondPoint.setUsed(true);
                        if(firstPoint.use()) {
                            // loop get second point
                            while(true) {
                                secondPoint = points[random.nextInt(n)];
                                if(secondPoint.use()) {
                                    break;
                                } else {
                                    failCount ++;
                                    // System.out.printf("\t%s 2 Points used, fail count %d\n", Thread.currentThread().getName(), failCount);
                                    continue;
                                }
                            }
                        } else {
                            failCount ++;
                            // System.out.printf("\t%s 1 Points used, fail count %d\n", Thread.currentThread().getName(), failCount);
                            continue;
                        }
                        System.out.printf("\t%s draw\n", Thread.currentThread().getName());
//                        int threadN = i;
                        drawLine.draw(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), id);
//                        Thread ui = new Thread(new DrawLineRunnable(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), id, drawLine));
//                        ui.start();
                    }

                    Thread currentThread = Thread.currentThread();
                    threadCountMap.put(currentThread, threadCountMap.get(currentThread) + 1);
                    
                    // sleep before construct next line
                    if(sleep) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CPGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }, "thread-" + i);

            threadCountMap.put(thread, 0);
            thread.start();
        }

        try {

//            mainLatch.await(maxTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            System.out.println("Start");
            boolean rsult = mainLatch.await(m * 1000, TimeUnit.MILLISECONDS);
            System.out.printf("Run : %b\n", rsult);
            done = true;
        } catch (InterruptedException e) {
            System.out.println("\tError");
            e.printStackTrace();
        }


        for(Map.Entry<Thread, Integer> entry : threadCountMap.entrySet()) {
            String result = entry.getKey().getName() + " created count: " + entry.getValue();
            System.out.println(result);
            this.result += result + "\n";
        }
    }
    
    public void runSingleThreadGame() {
        int id = new Random().nextInt(65536);
        int failCount = 0;
        int count = 0;
        System.out.println("\tStart");
        while (!done) {

            if (failCount >= 20) {
                if(!done) {
                    done = true;
//                    mainLatch.countDown();
                }
            }
            
            int rn = random.nextInt(n);
            Point firstPoint = points[rn];
            System.out.printf("\t\t%d\n", rn);
            rn = random.nextInt(n);
            Point secondPoint = points[rn];

//                        if(firstPoint.isUsed() || secondPoint.isUsed()) {
//                            failCount ++;
//                            continue;
//                        }
//
//                        firstPoint.setUsed(true);
//                        secondPoint.setUsed(true);
//            if(!(firstPoint.use() && secondPoint.use())) {
//                failCount ++;
//                System.out.printf("\tPoints used, fail count %d\n", failCount);
//                continue;
//            }
            if(firstPoint.use()) {
                // loop get second point
                while(true) {
                    rn = random.nextInt(n);
                    secondPoint = points[rn];
                    System.out.printf("\t\t%d\n", rn);
                    if(secondPoint.use()) {
                        break;
                    } else {
                        failCount ++;
                        System.out.printf("\t2 Points used, fail count %d\n", failCount);
                        continue;
                    }
                }
            } else {
                failCount ++;
                System.out.printf("\t1 Points used, fail count %d\n", failCount);
                continue;
            }
            System.out.printf("\tLine %d draw\n", count+1);
            drawLine.draw(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), id);

            count ++;
        }
        System.out.printf("Single Thread tried %d times\n", count);
    }
}

class Point {
    private double x;
    private double y;

    private boolean used;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.used = false;
    }
    
    public double getX() {return x;}
    public double getY() {return y;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return this.x == point.x && this.y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", used=" + used +
                '}';
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
    synchronized boolean use() {
        // true mean can use
        // false mean cant use, used before
        if(this.used) 
            return false;
        this.used = true;
        return true;
    }
}

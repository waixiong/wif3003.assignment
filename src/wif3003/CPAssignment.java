/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpassignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author keanb
 */
public class CPAssignment {

    /**
     * @param args the command line arguments
     */
    static volatile boolean done = false;

    public static void main(String[] args) {
        game(100, 10, 10);
    }

    public static void game(int n, int t, int m) {
        if (n <= t){
            System.out.println("Number of points(n) must be greater than number of threads(t)");
            return;
        }
        
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

                while (!done) {

                    synchronized (CPAssignment.class) {
                        if (failCount == 20) {
                            if(!done) {
                                done = true;
                                mainLatch.countDown();
                            }
                            return;
                        }

                        Point firstPoint = points[random.nextInt(n)];
                        Point secondPoint = points[random.nextInt(n)];

                        if(firstPoint.isUsed() || secondPoint.isUsed()) {
                            failCount ++;
                            continue;
                        }

                        firstPoint.setUsed(true);
                        secondPoint.setUsed(true);
                    }

                    Thread currentThread = Thread.currentThread();
                    threadCountMap.put(currentThread, threadCountMap.get(currentThread) + 1);
                }
            }, "thread-" + i);

            threadCountMap.put(thread, 0);
            thread.start();
        }

        try {

            mainLatch.await(maxTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

            done = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for(Map.Entry<Thread, Integer> entry : threadCountMap.entrySet()) {
            System.out.println(entry.getKey().getName() + " created count: " + entry.getValue());
        }
    }
}


class Point {
    private int x;
    private int y;

    private boolean used;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
}

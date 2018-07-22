package com.dawn.banana.distributelock.concurrent;

import java.util.concurrent.Semaphore;

/**
 * Created by Dawn on 2018/7/22.
 * semaphore翻译的字面意思为信号量，semaphore可以控制同时访问的线程个数，
 * 通过acquire（）获取一个许可，如果没有就等待，而release()释放一个许可
 */
public class SemaphoreTest {

    public static void main(String [] args){

        int N =8;
        //机器数目
        Semaphore semaphore = new Semaphore(5);
        for (int i=0;i<N;i++){
            new Worker(i,semaphore).start();
        }


    }
    static  class Worker extends Thread{

        private int num;

        private Semaphore semaphore;

        public Worker(int num,Semaphore semaphore){
            this.num = num;
            this.semaphore = semaphore;

        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器生产。。。");
                Thread.sleep(2000);
                System.out.println("工人"+this.num+"释放出机器");
                semaphore.release();
            }catch (InterruptedException e){
                e.printStackTrace();
            }


        }
    }


}

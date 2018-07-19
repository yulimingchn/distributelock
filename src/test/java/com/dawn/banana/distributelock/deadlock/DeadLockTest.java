package com.dawn.banana.distributelock.deadlock;


/**
 * Created by Dawn on 2018/7/16.
 * 一个简单的死锁类，当DeadLock类的对象flag == 1时（td1），先锁定o1，睡眠500毫秒
 * 而td1在睡眠的时候另一个flag==0的对象（td2）线程启动。先锁定o2，睡眠500毫秒
 * td1睡眠结束后需要锁定o2才能继续执行，而此时o2已经被td2锁定
 * td2睡眠结束后需要锁定o1才能继续执行，而此时o1已经被td1锁定
 * td1，td2相互等待，都需要得到对方锁定的资源才能继续执行，从而死锁
 */
public class DeadLockTest implements Runnable {

    public int flag = 1;

    //静态对象是类的所有对象共享的
    private static Object o1= new Object(),o2=new Object();

    @Override
    public void run() {
        System.out.println("flag="+flag);
        if (flag == 1){
            synchronized (o1){
                try {
                    System.out.println("当前线程id为"+Thread.currentThread().getId()+"的线程获取锁对象o1");
                    Thread.sleep(500);
                    System.out.println("当前线程id为"+Thread.currentThread().getId()+"的线程尝试获取锁对象o2");
                }catch (Exception e){
                    e.printStackTrace();
                }
                synchronized (o2){
                    System.out.println("1");

                }
            }
        }
        if (flag == 0){
            synchronized (o2){
                try {
                    System.out.println("当前线程id为"+Thread.currentThread().getId()+"的线程获取锁对象o2");
                    Thread.sleep(500);
                    System.out.println("当前线程id为"+Thread.currentThread().getId()+"的线程尝试获取锁对象o1");
                }catch (Exception e){
                    e.printStackTrace();
                }
                synchronized (o1){
                    System.out.println("0");
                }
            }
        }
    }

    public static void main(String[] args){
        DeadLockTest td1 = new DeadLockTest();
        DeadLockTest td2 = new DeadLockTest();
        td1.flag = 1;
        td2.flag = 0;

        //td1，td2都处于可执行状态，但jvm线程调度先执行哪个线程是不确定的，td2的run()可能在td1的run()之前运行
        new Thread(td1).start();
        new Thread(td2).start();
    }



}

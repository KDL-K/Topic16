package com.shevlik;

import java.time.Instant;

public class Tunnel {
    private String name;
    private volatile boolean free;
    private long timeMovingThroughTunnelInSec;

    public Tunnel(String name,long timeMovingThroughTunnelInSec){
        this.name=name;
        this.timeMovingThroughTunnelInSec=timeMovingThroughTunnelInSec;
        free=true;
    }

    public boolean isFree(){
        return free;
    }
    public String getName(){
        return name;
    }



    public synchronized boolean tryGoThroughTunnel(long timeWaitingFreeTunnel, String nameOfTrain){
        Instant endOfWaiting=Instant.now().plusMillis(timeWaitingFreeTunnel*1000);
        boolean waitTimePassed=false;
        System.out.println(nameOfTrain+" tries to go through "+name);
        while (!free && !waitTimePassed){
            try {
                /*System.out.println(nameOfTrain+" is waiting.");*/
                wait(timeWaitingFreeTunnel*1000);
                if(endOfWaiting.compareTo(Instant.now())<0){
                    waitTimePassed=true;
                }
                /*System.out.println(nameOfTrain+" can go.");*/
            }catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
        if(!free){
            return false;
        }
        free=false;
        Instant endOfGoing=Instant.now().plusMillis(timeMovingThroughTunnelInSec*1000);
        while(endOfGoing.compareTo(Instant.now())>0){
            try {
                System.out.println(nameOfTrain + " is going through "+name);
                long i;
                wait(((i=endOfGoing.toEpochMilli()-Instant.now().toEpochMilli())<0)?1:i);
            }catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }

        free=true;
        System.out.println(nameOfTrain + " has gone out of "+name);
        notifyAll();
        return true;
    }

}

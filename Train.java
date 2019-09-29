package com.company;

public class Train implements Runnable{
    private String name;
    private long timeWaitingFreeTunnel;
    private boolean hasGoneThroughTunnel;
    private Tunnel tunnelMain;
    private Tunnel tunnelSpare;

    public Train(String name, long timeWaitingFreeTunnel, Tunnel tunnelMain, Tunnel tunnelSpare){
        this.name=name;
        this.timeWaitingFreeTunnel=timeWaitingFreeTunnel;
        this.tunnelMain=tunnelMain;
        this.tunnelSpare=tunnelSpare;
        hasGoneThroughTunnel=false;
    }

    public void setHasGoneThroughTunnel(boolean hasGoneThroughTunnel){
        this.hasGoneThroughTunnel=hasGoneThroughTunnel;
    }

    @Override
    public void run() {
        while (!hasGoneThroughTunnel){
            hasGoneThroughTunnel=tunnelMain.tryGoThroughTunnel(timeWaitingFreeTunnel, name);
            if(!hasGoneThroughTunnel & tunnelSpare.isFree()){
                System.out.println(name+" tries to go through anothet tunnel "+tunnelSpare.getName());
                hasGoneThroughTunnel=tunnelSpare.tryGoThroughTunnel(timeWaitingFreeTunnel, name);
            }
        }

    }
}

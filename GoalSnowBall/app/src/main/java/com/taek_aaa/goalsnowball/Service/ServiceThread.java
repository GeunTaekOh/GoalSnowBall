package com.taek_aaa.goalsnowball.Service;

import java.util.logging.Handler;

/**
 * Created by taek_aaa on 2017. 1. 24..
 */

public class ServiceThread extends Thread{
    Handler handler;
    Boolean isRun = true;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }
    public void stopForever(){
        synchronized (this){
            this.isRun=false;
        }
    }

    public void run(){
        while (isRun){
            //handler.sendEmty
            try{
                Thread.sleep(1000*60);      //현재는 1분마다임 나중에 고치기
            }catch (Exception e){

            }
        }
    }
}

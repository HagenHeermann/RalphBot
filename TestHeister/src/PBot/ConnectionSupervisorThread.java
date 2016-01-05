package PBot;

import PBot.RalphBot;
import org.jibble.pircbot.IrcException;

import java.io.IOException;

/**
 * Created by Hagen on 08.12.2015.
 */
public class ConnectionSupervisorThread extends Thread {
    private RalphBot ralphBot;
    private volatile boolean active;
    private int _craftWarResetCounter;

    public ConnectionSupervisorThread(RalphBot ralphBot){
        this.ralphBot = ralphBot;
        active = true;
        _craftWarResetCounter = 0;
    }
    public void end_supervision(){
        active = false;
    }

    public void run(){
        while (ralphBot.quit()){
            if(_craftWarResetCounter<60){
                _craftWarResetCounter ++;
            }else{
                _craftWarResetCounter = 0;
                ralphBot.craftWarClear();
            }
            ralphBot.craftWarUpdate();
            System.out.println("Checking if ralph is still connected");
            if(!ralphBot.isConnected()){
                System.out.println("ralph is not connected");
                try {
                    ralphBot.connect_bot();
                } catch (IrcException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("Sleeping for 1 min");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

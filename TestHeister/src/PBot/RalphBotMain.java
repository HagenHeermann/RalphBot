package PBot;

import com.mb3364.twitch.api.Twitch;

/**
 * Created by Hagen on 07.12.2015.
 */
public class RalphBotMain {

    /*
    Looks like the cops have given up the search ... the banks are ripe for hitting!
     */



    public static void main(String[]args) throws Exception{
        //Setuper setuper = new Setuper();
        //setuper.setup();
        //Twitch twitch = setuper.getTwitchObject();
        //String token = setuper.getAccesTokenString();


        RalphBot ralph = new RalphBot(1);
        ConnectionSupervisorThread supervisorThread = new ConnectionSupervisorThread(ralph);
        ralph.setVerbose(true);
        //ralph.connect("irc.twitch.tv", 6667,"oauth:r8jh07tmft9f02crn2cga9f8w06mvo");
        //ralph.joinChannel("#theunbelivableralph");
        ralph.connect_bot();
        supervisorThread.start();

    }

}

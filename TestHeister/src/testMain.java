import PBot.Setuper;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * Created by Hagen on 07.12.2015.
 */
public class testMain {
    public static void main(String[]args){
        Setuper setuper = new Setuper();
        try {
            setuper.setup();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //PBot.RalphBot ralphBot = new PBot.RalphBot(setuper.getTwitchObject(),setuper.getAccesTokenString(),"theunbelivableralph");
        //ralphBot.start();


    }


}


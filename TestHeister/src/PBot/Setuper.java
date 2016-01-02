package PBot;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Hagen on 07.12.2015.
 */
public class Setuper {
    private Twitch twitchObject;
    private String accesTokenString;
    boolean accesTokenSet=false;

    public void setup() throws URISyntaxException, IOException {
        Twitch twitch = new Twitch();
        twitch.setClientId("hhf07yb5yoj1t3tkqswc05kyfisa8td");
        URI callbackURI = new URI("http://127.0.0.1:23522");
        String authUrl = twitch.auth().getAuthenticationUrl(twitch.getClientId(),callbackURI, Scopes.USER_READ, Scopes.CHANNEL_READ);

        URI auth = new URL(authUrl).toURI();
        java.awt.Desktop.getDesktop().browse(auth);

        boolean authSucces = twitch.auth().awaitAccessToken();

        if(authSucces){
            String accesToken = twitch.auth().getAccessToken();
            this.accesTokenString = accesToken;
            this.accesTokenSet = true;
            System.out.println("Acces Token: "+ accesToken);
        }
        else{
            System.out.println(twitch.auth().getAuthenticationError());
        }

        this.twitchObject = twitch;

    }

    public Twitch getTwitchObject(){return twitchObject;}
    public String getAccesTokenString(){return accesTokenString;}
    public boolean getAccesTokenSet(){return accesTokenSet;}

}

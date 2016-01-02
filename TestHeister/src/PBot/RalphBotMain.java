package PBot;
import com.mb3364.twitch.api.Twitch;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jibble.pircbot.IrcException;

import java.io.IOException;


/**
 * GENESIS
 * Created by Hagen on 07.12.2015.
 */
public class RalphBotMain extends Application{

    public static void main(String[]args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ralph");
        Label tokenLabel = new Label("Token: ");
        TextField textField = new TextField();
        Button btn = new Button();
        btn.setText("submit");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String token = textField.getText();
                RalphBot bot = new RalphBot(0,token);
                ConnectionSupervisorThread supervisorThread = new ConnectionSupervisorThread(bot);
                bot.setVerbose(true);
                try {
                    bot.connect_bot();
                    supervisorThread.start();
                } catch (IrcException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox box = new HBox();
        box.getChildren().addAll(tokenLabel,textField,btn);
        box.setSpacing(10);
        primaryStage.setScene(new Scene(box,500,100));
        primaryStage.show();
    }
}

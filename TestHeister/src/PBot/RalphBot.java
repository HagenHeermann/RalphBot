package PBot;

import org.jibble.pircbot.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hagen on 07.12.2015.
 */
public class RalphBot extends PircBot{

    private CDatabase _db;
    private final String twitch_server = "irc.twitch.tv";
    private final int twitch_port_number = 6667;
    private String id_token;
    private FileWriter log_writer;
    private BufferedWriter buffered_log_writer;
    private String _channelName;
    private int log_id;
    private File log_file;
    private boolean quit=true;
    private boolean heist_online;
    private COnMessageHandler _onMessageHandler;
    private COutPutQ _q;

    public RalphBot(int log_id,String oath,String _channelName){
        this._channelName = "#"+_channelName;
        _q = new COutPutQ(this,this._channelName);
        this.id_token = oath;
        heist_online = false;
        this.log_id = log_id;
        create_log_file(this.log_id);
        this.setName("AneleBot");
        _db = new CDatabase();
        try {
            _db.connectDB();
        } catch (Exception e) {
            System.out.println("couldnt initalize db");
            e.printStackTrace();
        }
        _onMessageHandler = new COnMessageHandler(_db,this,_channelName);
    }

    public void connect_bot() throws IrcException, IOException {
        this.connect(twitch_server,twitch_port_number,id_token);
        this.joinChannel(_channelName);
    }

    public void onJoin(String channel, String sender, String login, String hostname){
    }

    public void onMessage(String channel, String sender,String login, String hostname, String message){
        System.out.println("enqueing");
        _q.enque(_onMessageHandler.handleMessage(message,sender));
    }

    private boolean isPlayerListet(String name){
        System.out.println("isPlayerListet was called");
        boolean res = false;
        try {
            ArrayList<String> users = _db.selectUSERS();
            for(int i=0;i<users.size();i++){
                System.out.println("Comparision between: "+users.get(i)+"=="+name);
                if(users.get(i).contains(name)){
                    res = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void create_log_file(int log_id){
        String path = "../TestHeister/src/LogFiles";
        String file = "logfile"+log_id+".txt";
        File logFile = new File(path,file);
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log_file = logFile;
        if(log_file.exists()){
            try {
                log_writer = new FileWriter(log_file.getAbsoluteFile());
                buffered_log_writer = new BufferedWriter(log_writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void heist(){
        if(heist_online){
            Random r = new Random();
            int low = 1000;
            int high = 10000;
            int res = r.nextInt(high-low)+low;
            try {
                Thread.sleep(res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.sendMessage(_channelName,"!bankheist 50");

        }
    }

    public void setHeist_online(boolean val){heist_online=val;}

    public void setQuit(boolean val){quit=val;}

    public boolean quit(){return quit;}

    public void craftWarUpdate(){_onMessageHandler.craftWarUpdate();}

    public void craftWarClear(){_onMessageHandler.craftWarClear();}
}

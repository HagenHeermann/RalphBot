package PBot;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by Hagen on 30.12.2015.
 */
public class COnMessageHandler {

    private CDatabase _db;
    private RalphBot _bot;
    private CCraftWarComponent craftWar;

    public COnMessageHandler(CDatabase _db,RalphBot _bot){
        this.craftWar = new CCraftWarComponent(_db);
        this._db = _db;
        this._bot = _bot;
    }

    public String handleMessage(String message,String sender){
        String res = null;
        if(message.contains("Looks like the cops have given up the search ... the banks are ripe for hitting!")&&sender.contains("winibutt")){
            _bot.heist();
        }
        else if(sender.contains("voodoohood")&&message.contains("ralphbot")){
            res = "I do as you command";
        }
        else if(sender.contains("voodoohood")&&message.contains("#startheist")){
            _bot.heist();
        }
        else if(sender.contains("voodoohood")&&message.contains("#points")){
            res = "!points";
        }
        else if((sender.contains("n1ghtsh0ck")||sender.contains("voodoohood"))&&message.contains("#shutdown")){
            res = "goodbye plebs OpieOP";
            _bot.setQuit(false);
            _bot.disconnect();
        }
        else if(message.contains("#heiston")&&sender.contains("voodoohood")){
            _bot.setHeist_online(true);
        }
        else if(message.contains("heistoff")&&sender.contains("voodoohood")){
            _bot.setHeist_online(false);
        }
        else if(message.contains("#addToDb")&&sender.contains("voodoohood")){
            res = addingToDbCall(message, res);
        }
        else if(sender.contains("winibutt")&&(message.contains("executed the heist flawlessly, sneaking into the Wini Bank through the back entrance and looting")||message.contains("Heist results:")||message.contains("Local security caught"))){
            heistEndedCase(message);

        }else if(message.contains("#stats")&&CStatisticsComponent.isPlayerListet(sender, _db)){
            res = statisticsCall(message, sender, res);

        }else if(message.contains("#delete")&&sender.contains("voodoohood")){
            deleteCase(message);
        }else if(sender.contains("voodoohood")&&message.contains("#registerUserToCraftWar")){
            String[] splittedMessage = message.split("\\s+");
            try {
                res = craftWar.registerUserToCraftWar(splittedMessage[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#baseStats")){
            try {
                boolean regis = craftWar.userRegistered(sender);
                if(regis){
                    res = craftWar.getBaseStats(sender);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#upgradeMine")){
            try {
                boolean regis = craftWar.userRegistered(sender);
                if(regis){
                    res = craftWar.upgradeMine(sender);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#buildBarracks")){
            try {
                boolean regis = craftWar.userRegistered(sender);
                if(regis){
                    res = craftWar.buildBarracks(sender);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#buildUnits")){
            try {
                boolean regis = craftWar.userRegistered(sender);
                if(regis){
                    String[] splitMessage = message.split("\\s+");
                    int val = Integer.parseInt(splitMessage[1]);
                    res = craftWar.buildUnits(sender,val);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#attack")){
            String[] splitMessage = message.split("\\s+");
            try {
                res = craftWar.attack(sender,splitMessage[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(message.contains("#commands")){
            res = "# attack <User(Small letters)> | # buildUnits <Number> | # buildBarracks | # upgradeMine | # baseStats (no space Kappa ) Prices: 1xUnit = 100g , mine = minelevel * 100g, barracks = 200g ";
        }else if(message.contains("#top10")){
            String[]split = message.split("\\s+");
            if(split[0].contains("#top10")){
                try {
                    res = craftWar.top10();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return res;
    }

    private String addingToDbCall(String message, String res) {
        String[] messageSplit = message.split("\\s+");
        try {
            if(!CStatisticsComponent.isPlayerListet(messageSplit[1], _db)){
                _db.insert(messageSplit[1],0,0,0);
                res = messageSplit[1]+" was added to the db";
            }else{
                res = messageSplit[1]+" is already registered";
            }
        } catch (SQLException e) {
            System.out.println("Failed to add user to db");
            e.printStackTrace();
        }
        return res;
    }

    private void heistEndedCase(String message) {
        System.out.println("heist end starting statistics module");
        if(message.contains("executed the heist flawlessly, sneaking into the Wini Bank through the back entrance and looting")){
            try {
                CStatisticsComponent.singleHeist(message, _db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(message.contains("Heist results:")){
            try {
                CStatisticsComponent.multiHeist(message, _db);
                System.out.println("Multi heist ended");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(message.contains("Local security caught")){
            try {
                CStatisticsComponent.singleHeistFail(message, _db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String statisticsCall(String message, String sender, String res) {
        System.out.println("statistics are wished");
        String[] splitMessage = message.split("\\s+");
        if(splitMessage.length>=2){
            try {
                String[] zw = CStatisticsComponent.statistics(splitMessage[1], _db);
                String message0 = zw[0];
                String message1 = zw[1];
                String message2 = zw[2];
                String outputmessage = "Statistics for "+splitMessage[1]+": "+message0 +" " + message1 + " "+ message2;
                res = outputmessage;
            } catch (SQLException e) {e.printStackTrace();} catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                String[] zw = CStatisticsComponent.statistics(sender,_db);
                String message0 = zw[0];
                String message1 = zw[1];
                String message2 = zw[2];
                String outputmessage = "Statistics for "+sender+": "+message0+" "+message1+" "+message2;
                res = outputmessage;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private void deleteCase(String message) {
        String[] splitmsg = message.split("\\s+");
        try {
            CStatisticsComponent.deltePlayer(splitmsg[1], _db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void craftWarUpdate(){
        try {
            craftWar.updateValues();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

package PBot;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Hagen on 30.12.2015.
 *
 * Logging fomrat for this class is System.currentTimeMilis()+" Statistics: "+ text;
 */
public class CStatisticsComponent {

    private static Logger log = RalphBotMain.log;

    /**
     *
     * @param message
     * @param db
     * @throws SQLException
     */
    static void singleHeist(String message,CDatabase db) throws SQLException {
        String[] splitMessage = message.split("\\s+");
        if(isPlayerListet(splitMessage[0],db)){
            int lastW = db.selectWINS(splitMessage[0]);
            int lastH = db.selectHEISTCOUNT(splitMessage[0]);
            db.updateWINS(lastW+1,splitMessage[0]);
            db.updateHEISTCOUNT(lastH+1,splitMessage[0]);
            log.info(System.currentTimeMillis()+" Statistics: Wins for a user updated");
        }
    }

    static void singleHeistFail(String message,CDatabase _db) throws SQLException {
        String[] splitMessage = message.split("\\s+");
        if(isPlayerListet(splitMessage[3],_db)){
            int lastF = _db.selectRIPS(splitMessage[3]);
            int lastH = _db.selectHEISTCOUNT(splitMessage[3]);
            _db.updateHEISTCOUNT(lastH+1,splitMessage[3]);
            _db.updateRIPS(lastF+1,splitMessage[3]);
            log.info(System.currentTimeMillis() + " Statistics: RIPs for a user updated");
        }

    }

    static void multiHeist(String message,CDatabase _db) throws SQLException {
        String[] splitMessage = message.split("\\s+");


        for(int i=2;i<splitMessage.length;i=i+2){
            if(isPlayerListet(splitMessage[i],_db)){
                if(!splitMessage[i+1].contains("rip")){
                    int lastW = _db.selectWINS(splitMessage[i]);
                    _db.updateWINS(lastW+1,splitMessage[i]);
                    log.info(System.currentTimeMillis() + " Statistics: Wins for a user updated");
                }else{
                    int lastR = _db.selectRIPS(splitMessage[i]);
                    _db.updateRIPS(lastR+1,splitMessage[i]);
                    log.info(System.currentTimeMillis() + " Statistics: RIPs for a user updated");
                }
                int lasH = _db.selectHEISTCOUNT(splitMessage[i]);
                _db.updateHEISTCOUNT(lasH+1,splitMessage[i]);
                System.out.println("Players heist count updatet"+splitMessage[i]);
                log.info(System.currentTimeMillis() + " Statistics: heistcount for a user updated");
            }
        }

    }

    static void deltePlayer(String name,CDatabase _db) throws SQLException {
        if (isPlayerListet(name, _db)) {
            _db.deletePlayer(name);
            log.info(System.currentTimeMillis() + " Statistics: User succesfully deleted");
        }
    }

    static String[] statistics(String name,CDatabase _db) throws SQLException, ParseException {
        String[] res = new String[3];
        if(isPlayerListet(name,_db)){
            Integer wins = _db.selectWINS(name);
            double dwins = wins.doubleValue();
            Integer rips = _db.selectRIPS(name);
            double drips = rips.doubleValue();
            res[0] = "Wins: "+wins;
            res[1] = "Rips: "+rips;
            double ges = dwins + drips;
            double winPercentage = 0;
            if(!(ges==0)){
                winPercentage = (wins / ges) * 100;
            }
            winPercentage = round(winPercentage,2);
            res[2] = "Percentage :"+winPercentage;
        } else{
            res[0]="Player not Listed";
            res[1]="_";
            res[2]="_";
        }
        log.info(System.currentTimeMillis() + " Statistics: Statistics for a user succesfully returned");
        return res;
    }

    static boolean isPlayerListet(String name,CDatabase _db){
        boolean res = false;
        try {
            ArrayList<String> users = _db.selectUSERS();
            for(int i=0;i<users.size();i++){
                if(users.get(i).contains(name)){
                    res = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

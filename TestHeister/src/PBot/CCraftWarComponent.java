package PBot;

import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

/**
 * Buildings: Barracks Mine
 * Units: units
 * Ressources: gold
 *
 * Tabulka:
 * Player ID/gold(int)/Barracks(boolean)/MineLevel(int)/UNIT COUNT(int)
 *
 * methods:
 * -build mine
 * -build barracks
 * -build unit number
 * -attack player
 *
 * Created by Hagen on 31.12.2015.
 *
 * Logging format for this class is System.currentTimeMilis() + " CraftWar: "+ text
 */
public class CCraftWarComponent {

    private Logger log = RalphBotMain.log;
    private CDatabase _base;
    private final int mineBaseCost = 100;
    private final int unitCost = 100;
    private final int barracksCost = 200;
    private HashMap<String,Integer> _playerAttackedPerH;
    private ArrayList<String> _activePlayers;


    public CCraftWarComponent(CDatabase base){
        this._base = base;
        this._activePlayers = new ArrayList<>();
        this._playerAttackedPerH = new HashMap<>();
        log.info(System.currentTimeMillis()+" CraftWar: CraftWar component created");
    }

    public void updateValues() throws SQLException {
        ArrayList<String> users = _base.selectUsersCraftWar();
        for(int i=0;i<users.size();i++){
            int minelevel = _base.selectMineCraftWar(users.get(i));
            int madeGold = 5 * minelevel;
            _base.updateGoldCraftWar(users.get(i),_base.selectGoldCraftWar(users.get(i))+madeGold);
        }
        log.info(System.currentTimeMillis()+" CraftWar: Gold values of the players updated relative to their mine level");
    }

    public String getBaseStats(String username) throws SQLException {
        String res;
        if(userRegistered(username)){
            String mineLevel ="Mine Level: "+_base.selectMineCraftWar(username);
            String goldAmount = "Gold: "+_base.selectGoldCraftWar(username);
            String unitAmount = "Units: "+_base.selectUnitsCraftWar(username);
            if(_base.selectBarracksCraftWar(username)==0){
                String barracksBuild = "Barracks: No Barracks";
                res = "Base stats for "+username+" are "+mineLevel+" "+goldAmount+" "+unitAmount+" "+barracksBuild;
                log.info(System.currentTimeMillis()+" CraftWar: Returned the base stats for a player with barracks");
            }else{
                String barracksBuild = "Barracks: Available";
                res = "Base stats for "+username+" are "+mineLevel+" "+goldAmount+" "+unitAmount+" "+barracksBuild;
                log.info(System.currentTimeMillis()+" CraftWar: Returned the base stats for a player with no barracks");
            }
        }else{
            res = "Player isnt registered";
        }

        return res;
    }

    public String upgradeMine(String username) throws SQLException {
        String res;
        if(userRegistered(username)){
            int usersGold = _base.selectGoldCraftWar(username);
            int mineLevel = _base.selectMineCraftWar(username);
            if(usersGold>=mineLevel*mineBaseCost){
                _base.updateMineCraftWar(username,mineLevel+1);
                _base.updateGoldCraftWar(username,usersGold-(mineLevel*100));
                res = "Mine of player "+username+" has been upgraded to level "+(mineLevel+1);
                log.info(System.currentTimeMillis()+" CraftWar: Mine for a player succesfully upgraded and returned");
            }else{
                res = "Not enough Gold "+username;
                log.info(System.currentTimeMillis()+" CraftWar: Mine for a player not upgraded because he has not enough gold");
            }
        }else{
            res = "Player isnt registered";
            log.info(System.currentTimeMillis()+" CraftWar: Not registered user tryed to upgrade his mine");
        }

        return res;
    }

    public String buildBarracks(String username) throws SQLException {
        String res;
        if(userRegistered(username)){
            int usersGold = _base.selectGoldCraftWar(username);
            int usersbarracks = _base.selectBarracksCraftWar(username);
            if(usersbarracks == 0){
                if(usersGold>=barracksCost){
                    _base.updateBarracksCraftWar(username,1);
                    _base.updateGoldCraftWar(username,usersGold-barracksCost);
                    res = "Barracks build "+username;
                    log.info(System.currentTimeMillis()+" CraftWar: Barracks were build for a player");
                }else{
                    res = "You dont have enough gold to build a barrack "+username;
                    log.info(System.currentTimeMillis()+" CraftWar: User tryed to build Barracks but doesnt have enough gold");
                }
            }else{
                res = "You already have a barrack "+username;
                log.info(System.currentTimeMillis()+" CraftWar: User tryed to build Barracks when he already had them");
            }
        }else{
            res = "Player isnt registered";
            log.info(System.currentTimeMillis()+" CraftWar: Non registered user tryed to acces buildBarracks method");
        }

        return res;
    }

    public String buildUnits(String username,int count) throws SQLException {
        String res;
        if(count<1000){
            if(_base.selectBarracksCraftWar(username)>=1){
                if(userRegistered(username)){
                    int usersGold = _base.selectGoldCraftWar(username);
                    int usersUnits = _base.selectUnitsCraftWar(username);
                    if(usersGold>= count*100){
                        _base.updateUnitsCraftWar(username,usersUnits+count);
                        int gold = _base.selectGoldCraftWar(username);
                        _base.updateGoldCraftWar(username,gold-(100*count));
                        res = "You now have "+(usersUnits+count)+" Units "+username;
                        log.info(System.currentTimeMillis()+" CraftWar: Player build sucessfully units");
                    }
                    else{
                        res = "You dont have enough gold to build those units "+username;
                        log.info(System.currentTimeMillis()+" CraftWar: User tryed to build units but hasnt enough gold");
                    }
                }else{
                    res = "Player isnt registered";
                    log.info(System.currentTimeMillis()+" CraftWar: Non registered User tryed to acces buildUnits method");
                }
            }
            else{
                res = "No Barracks in this base";
                log.info(System.currentTimeMillis()+" CraftWar: Player tryed to build Units without Barracks");
            }
        }else{
            res = "Sry no more than 1000 Units at a time";
            log.info(System.currentTimeMillis()+" CraftWar: Player tryed to build more than 1000 Units");
        }

        return res;
    }

    //DEPRICATED
    //________________________________________________________________________________
    public String attack(String attacker,String defender) throws SQLException {
        String res;
        if(userRegistered(attacker)){
            if(userRegistered(defender)){
                if(attackerDefenderBalancing(attacker,defender)){
                    int attackerUnints = _base.selectUnitsCraftWar(attacker);
                    int defenderUnits = _base.selectUnitsCraftWar(defender);
                    Double addedForces = defenderAddedForces() * defenderUnits + defenderUnits;
                    int defEndForces = addedForces.intValue();

                    System.out.println(attackerUnints);
                    System.out.println(defenderUnits);
                    System.out.println(defEndForces);
                    if(attackerUnints>defEndForces){
                        _base.updateUnitsCraftWar(defender,defenderUnits/2);
                        int goldDefender = _base.selectGoldCraftWar(defender);
                        int goldAttacker = _base.selectGoldCraftWar(attacker);
                        _base.updateGoldCraftWar(defender,3*(goldDefender/4));
                        _base.updateGoldCraftWar(attacker,goldAttacker+goldDefender/4);
                        res = "The attacker "+attacker+" won and destroyed half of the defenders "+defender+" army,looting "+ goldDefender/4 +" gold";
                    }else{
                        _base.updateUnitsCraftWar(attacker,0);
                        res = "The defender "+defender+" won and slaughterd the attackers "+attacker+" army to the last man";
                    }
                }
                else{
                    res = "Attacker Defender balancing say's no OMGScoots";
                }
            }else{
                res ="Defender isnt registered";
            }
        }
        else{
            res ="Attacker isnt registered";
        }
        return res;
    }
    //________________________________________________________________________________


    public String newAttack(String attacker,String defender)throws SQLException{
        String res;
        addPlayerToActive(attacker);
        addPlayerToActive(defender);

        if(_playerAttackedPerH.get(defender)<4){
            if(userRegistered(attacker)&&userRegistered(defender)){
                int attackerUnits = _base.selectUnitsCraftWar(attacker);
                int attackerDices=1;
                int defenderUnits = _base.selectUnitsCraftWar(defender);
                int defenderDices=1;

                if(!(attackerUnits==0)){
                    if(attackerUnits>100){
                        attackerDices = attackerUnits / 100;
                    }
                    if(defenderUnits>100){
                        defenderDices = defenderUnits / 100;
                    }

                    int rolledAttackerValue = 0;
                    int rolledDefenderValue = 0;

                    for(int i=0;i<attackerDices;i++ ){
                        rolledAttackerValue = rolledAttackerValue + diceRoll();
                    }
                    for(int i=0;i<defenderDices;i++){
                        rolledDefenderValue = rolledDefenderValue + diceRoll();
                    }

                    if(rolledAttackerValue>rolledDefenderValue){
                        int goldDefender = _base.selectGoldCraftWar(defender);
                        int goldAttacker = _base.selectGoldCraftWar(attacker);
                        _base.updateGoldCraftWar(defender,3*(goldDefender/4));
                        _base.updateGoldCraftWar(attacker,goldAttacker+goldDefender/4);
                        res = "The attacker "+attacker+"(roll "+rolledAttackerValue+") won against the defenders "+defender+"(rolled "+rolledDefenderValue+") army,looting "+ goldDefender/4 +" gold";
                        incrementAttackedCounter(defender);
                        log.info(System.currentTimeMillis()+" CraftWar: Player attacked another Player and won");
                    }else{
                        _base.updateUnitsCraftWar(attacker,attackerUnits/2);
                        res = "The Defender "+defender+"(roll "+rolledDefenderValue+")won and slaughtered the attackers "+attacker+"(roll "+rolledAttackerValue+") army";
                        log.info(System.currentTimeMillis()+" CraftWar: Player attacked another Player and lost");
                    }
                }else{
                    res = "No attacks with 0 Units allowed";
                    log.info(System.currentTimeMillis()+" CraftWar: Player tryed to attack with 0 Units");
                }

            }
            else{
                res = "either attack or defender isn't registered";
                log.info(System.currentTimeMillis()+" CraftWar: User tryed to attack a non registered User or isnt registered");
            }
        }
        else{
            res = "Defender was attacked 4 times already this hour";
            log.info(System.currentTimeMillis()+" CraftWar: Player tryed to attack a player who was already attacked 4 times in an hour");
        }

        return res;
    }

    public String registerUserToCraftWar(String username) throws SQLException {
        String res;
        if(userRegistered(username)){
            res = "User already registered";
            log.info(System.currentTimeMillis()+" CraftWar: Tryed to add a user who is already registered");
        }else{
            _base.createPlayerCraftWar(username);
            res = "User "+username+" succesfully added to the player list";
            log.info(System.currentTimeMillis()+" CraftWar: Registered a user to the CraftWar system");
        }
        return res;
    }

    public boolean userRegistered(String username)throws SQLException{
        boolean res = false;
        try {
            ArrayList<String> users = _base.selectUsersCraftWar();
            for(int i=0;i<users.size();i++){
                if(users.get(i).contains(username)){
                    res = true;
                    log.info(System.currentTimeMillis()+" CraftWar: Asked user is registered");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String giveGold(String user1,String user2,int value) throws SQLException {
        String res;
        int goldUser1 = _base.selectGoldCraftWar(user1);
        int goldUser2 = _base.selectGoldCraftWar(user2);
        if(user1 == user2){
            if(userRegistered(user1)&&userRegistered(user2)){
                if(value<=100000){
                    if(value <= goldUser1){
                        _base.updateGoldCraftWar(user1,goldUser1-value);
                        _base.updateGoldCraftWar(user2,goldUser2+value);
                        res = user1+" donated "+value+" to "+user2;
                        log.info(System.currentTimeMillis()+" CraftWar: Player donated gold to another Player");
                    }else{
                        res = "You dont have enough gold "+user1;
                        log.info(System.currentTimeMillis()+" CraftWar: Player tryed to donate more money than he has to another Player");
                    }
                }else{
                    res = "No donations higher than 100k";
                    log.info(System.currentTimeMillis()+" CraftWar: Player tryed to donate mor than 100k to another Player");
                }
            }else{
                res = "Either "+user1+" or "+user2+" arent registered, check the spelling maybe";
                log.info(System.currentTimeMillis()+" CraftWar: User tryed to donate to another user but either one or both isnt registered");
            }
        }else{
            res = "nice try MingLee";
            log.info(System.currentTimeMillis()+" CraftWar: User tryed to donate to himself");
        }
        return res;
    }

    public String top10() throws SQLException {
        String res = null;
        HashMap<String,Integer> map = new HashMap();
        ArrayList<String> users = _base.selectUSERS();
        ArrayList<Integer> usersValues = new ArrayList<>();
        for(int i=0;i<users.size();i++){
            try{
                Thread.sleep(5);
            }catch (InterruptedException e){}
            map.put(users.get(i), _base.selectUnitsCraftWar(users.get(i)));
            usersValues.add(_base.selectUnitsCraftWar(users.get(i)));
        }
        Map ascUsersValue = new TreeMap();
        ascUsersValue.putAll(map);
        System.out.println(ascUsersValue);


        return res;
    } //TODO

    public String reset() throws SQLException{
        ArrayList<String> list = _base.selectUsersCraftWar();
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
            _base.updateGoldCraftWar(list.get(i),0);
            _base.updateUnitsCraftWar(list.get(i),0);
            _base.updateMineCraftWar(list.get(i),1);
            _base.updateBarracksCraftWar(list.get(i),1);
            System.out.println(this.getBaseStats(list.get(i)));
        }
        log.info(System.currentTimeMillis()+" CraftWar: The DB was cleaned");
        return "Reset done";
    }
    private double defenderAddedForces(){
        Random random = new Random();
        double minRange = 0.2;
        double maxRange = 1;
        return minRange + (maxRange - minRange) * random.nextDouble();
    }

    private int diceRoll(){
        Random numberGenerator = new Random();
        int res;
        int range = 6;
        long fraction = (long)(range * numberGenerator.nextDouble());
        res = (int)(fraction+1);
        return res;
    }
    //CHECKUP SECTION

    private void addPlayerToActive(String username){
        if(!alreadyInList(username)){
            _activePlayers.add(username);
        }
        if(!alreadyInMap(username)){
            _playerAttackedPerH.put(username,0);
        }
    }

    private boolean alreadyInMap(String username){
        boolean res = _playerAttackedPerH.containsKey(username);
        return res;
    }

    private void incrementAttackedCounter(String username){
        if(alreadyInMap(username)){
            int oldNum = _playerAttackedPerH.get(username);
            _playerAttackedPerH.replace(username,oldNum,oldNum+1);
        }
    }

    private boolean alreadyInList(String username){
        boolean res = _activePlayers.contains(username);
        return res;
    }

    public void clear(){
        this._playerAttackedPerH = new HashMap<>();
        this._activePlayers = new ArrayList<>();
    }
    private boolean attackerDefenderBalancing(String attacker,String defender) throws SQLException {
        boolean res;
        int attackersArmy = _base.selectUnitsCraftWar(attacker);
        int defendersArmy = _base.selectUnitsCraftWar(defender);
        if(attackersArmy > defendersArmy*2){
            res = false;
        }else{
            res = true;
        }
        return res;
    }
// BBBBBBB
}

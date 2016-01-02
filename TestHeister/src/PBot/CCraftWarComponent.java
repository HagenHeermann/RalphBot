package PBot;

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
 */
public class CCraftWarComponent {

    private CDatabase _base;
    private final int mineBaseCost = 100;
    private final int unitCost = 100;
    private final int barracksCost = 200;

    public CCraftWarComponent(CDatabase base){
        this._base = base;
    }

    public void updateValues() throws SQLException {
        ArrayList<String> users = _base.selectUsersCraftWar();
        for(int i=0;i<users.size();i++){
            int minelevel = _base.selectMineCraftWar(users.get(i));
            int madeGold = 5 * minelevel;
            _base.updateGoldCraftWar(users.get(i),_base.selectGoldCraftWar(users.get(i))+madeGold);
        }
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
            }else{
                String barracksBuild = "Barracks: Available";
                res = "Base stats for "+username+" are "+mineLevel+" "+goldAmount+" "+unitAmount+" "+barracksBuild;
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
            }else{
                res = "Not enough Gold "+username;
            }
        }else{
            res = "Player isnt registered";
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
                }else{
                    res = "You dont have enough gold to build a barrack "+username;
                }
            }else{
                res = "You already have a barrack "+username;
            }
        }else{
            res = "Player isnt registered";
        }

        return res;
    }

    public String buildUnits(String username,int count) throws SQLException {
        String res;
        if(_base.selectBarracksCraftWar(username)>=1){
            if(userRegistered(username)){
                int usersGold = _base.selectGoldCraftWar(username);
                int usersUnits = _base.selectUnitsCraftWar(username);
                if(usersGold>= count*100){
                    _base.updateUnitsCraftWar(username,usersUnits+count);
                    int gold = _base.selectGoldCraftWar(username);
                    _base.updateGoldCraftWar(username,gold-(100*count));
                    res = "You now have "+(usersUnits+count)+" Units "+username;
                }
                else{
                    res = "You dont have enough gold to build those units "+username;
                }
            }else{
                res = "Player isnt registered";
            }
        }
        else{
            res = "No Barracks in this base";
        }
        return res;
    }

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

    public String registerUserToCraftWar(String username) throws SQLException {
        String res;
        if(userRegistered(username)){
            res = "User already registered";
        }else{
            _base.createPlayerCraftWar(username);
            res = "User "+username+" succesfully added to the player list";
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    private double defenderAddedForces(){
        Random random = new Random();
        double minRange = 0.2;
        double maxRange = 1;
        return minRange + (maxRange - minRange) * random.nextDouble();
    }

    //CHECKUP SECTION

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
}

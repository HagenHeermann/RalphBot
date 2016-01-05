package PBot;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Hagen on 31.12.2015.
 */
public class CraftWartMainTest {
    public static void main(String[]args){
        CDatabase base = new CDatabase();
        CCraftWarComponent comp = new CCraftWarComponent(base);

        try {
            base.connectDB();
            for(int i=0;i<100;i++){
                //System.out.println(comp.diceRoll());
            }
            ArrayList<String> list = base.selectUsersCraftWar();
            for(int i=0;i<list.size();i++){
                System.out.println(list.get(i));
                base.updateGoldCraftWar(list.get(i),0);
                base.updateUnitsCraftWar(list.get(i),0);
                base.updateMineCraftWar(list.get(i),1);
                base.updateBarracksCraftWar(list.get(i),1);
                System.out.println(comp.getBaseStats(list.get(i)));
            }





        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

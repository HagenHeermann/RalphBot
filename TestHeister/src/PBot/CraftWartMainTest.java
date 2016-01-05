package PBot;

import java.sql.SQLException;

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





        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

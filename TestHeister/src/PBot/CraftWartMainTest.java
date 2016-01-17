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
            base.updateGoldCraftWar("voodoohood",100000);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

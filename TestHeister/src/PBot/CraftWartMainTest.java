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
            String values = comp.getBaseStats("perfectionx6");
            System.out.println(values);





        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

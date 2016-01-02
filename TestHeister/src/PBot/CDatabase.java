package PBot;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Hagen on 29.12.2015.
 *
 * TABLES NEEDED
 * USERS
 * USERNAME(STRING) / COUNTS(INT) / WINS(INT) / RIPS(INT)
 *
 */
public class CDatabase {

    private Connection _connection;

    /**
     * Connection to the Database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void connectDB() throws ClassNotFoundException, SQLException {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
        System.out.println("Opened database successfully");
        _connection = c;
        _connection.setAutoCommit(false);
    }

    /**
     * One time use only to create the Table.
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        Statement smt = null;
        smt = _connection.createStatement();
        String sql =  "CREATE TABLE USERS " +
                "(USERNAME TEXT PRIMARY KEY     NOT NULL," +
                " COUNTS            INT, " +
                " WINS        INT , " +
                " RIPS        INT)";
        smt.executeUpdate(sql);
        smt.close();
        System.out.println("Update successfull");
    }

    public void createTableCraftWar() throws SQLException{
        Statement stmt = null;
        stmt = _connection.createStatement();
        String sql = "CREATE TABLE CraftWar"+
                "(USERNAME TEXT PRIMARY KEY NOT NULL,"+
                "GOLD INT NOT NULL,"+
                "BARRACKS INT NOT NULL,"+
                "MINE INT NOT NULL,"+
                "UNITS INT NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("Update succesfull CraftWar");
    }

    /**
     * Insert new user over this method
     * @param username
     * @param heistcount
     * @param wins
     * @param rips
     * @throws SQLException
     */
    public void insert(String username,int heistcount,int wins,int rips) throws SQLException {
        Statement smt = null;
        smt = _connection.createStatement();
        String sql = "INSERT INTO USERS (USERNAME,COUNTS,WINS,RIPS) " +
                "VALUES ("+"'"+username +"'" +"," + heistcount +","+ wins +","+ rips +");";
        smt.execute(sql);
        smt.close();
        _connection.commit();

    }

    /**
     * Use this method to get all registered usernames in the database
     * @return
     * @throws SQLException
     */
    public ArrayList<String> selectUSERS() throws SQLException {
        ArrayList<String> users = new ArrayList<>();
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT USERNAME FROM USERS;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
            String name = resultSet.getString("USERNAME");
            users.add(name);
        }
        resultSet.close();
        stmt.close();
        return users;
    }

    /**
     * use this method to get the number of heists a player has done
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectHEISTCOUNT(String username) throws SQLException {
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT COUNTS FROM USERS WHERE USERNAME="+"'"+username +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("COUNTS");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * use this method to get the number of wone heists of a player
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectWINS(String username) throws SQLException {
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT WINS FROM USERS WHERE USERNAME="+"'"+username +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("WINS");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * use this method to get the number of rips of a player in the heisting system
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectRIPS(String username) throws SQLException {
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT  RIPS FROM USERS WHERE USERNAME="+"'"+username +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("RIPS");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * use this method to update the number of heists a user has done
     * @param value
     * @param username
     * @throws SQLException
     */
    public void updateHEISTCOUNT(int value,String username) throws SQLException {
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE USERS set COUNTS ="+value+" WHERE USERNAME="+"'"+username +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to update the number of wins a user has done
     * @param value
     * @param username
     * @throws SQLException
     */
    public void updateWINS(int value,String username) throws SQLException {
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE USERS set WINS ="+value+" WHERE USERNAME="+"'"+username +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * use this method to update the number of rips a player has experienced
     * @param value
     * @param username
     * @throws SQLException
     */
    public void updateRIPS(int value,String username) throws SQLException {
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE USERS set RIPS ="+value+" WHERE USERNAME="+"'"+username +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to delete a player from the database
     * @param username
     * @throws SQLException
     */
    public void deletePlayer(String username)throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "DELETE from USERS WHERE USERNAME="+"'"+username +"'"+" ;";
        stmt.executeUpdate(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to add a user to the CraftWars table
     * @param username
     * @throws SQLException
     */
    public void createPlayerCraftWar(String username) throws SQLException{
        Statement stmt = null;
        stmt = _connection.createStatement();
        String sql = "INSERT INTO CraftWar (USERNAME,GOLD,BARRACKS,MINE,UNITS)VALUES("+"'"+username.toLowerCase() +"',100,0,1,1);";
        stmt.execute(sql);
        stmt.close();
        _connection.commit();
    }

    /**
     * use this method to get all registered users
     * @return
     * @throws SQLException
     */
    public ArrayList<String> selectUsersCraftWar() throws SQLException{
        ArrayList<String> users = new ArrayList<>();
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT USERNAME FROM CraftWar;";
        resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
            String name = resultSet.getString("USERNAME");
            users.add(name);
        }
        resultSet.close();
        stmt.close();
        return users;
    }
    /**
     * Use this method to get the number of Gold a player has
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectGoldCraftWar(String username) throws SQLException {
        int res;
        Statement stmt;
        stmt = _connection.createStatement();
        ResultSet resultSet;
        String sql = "SELECT GOLD FROM CraftWar WHERE USERNAME ="+"'"+username.toLowerCase() +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("GOLD");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * Use this method to check if a player has build a Barracks
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectBarracksCraftWar(String username) throws SQLException{
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT BARRACKS FROM CraftWar WHERE USERNAME ="+"'"+username.toLowerCase() +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("BARRACKS");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * Use this method to get the mine Level of a player
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectMineCraftWar(String username) throws SQLException{
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT MINE FROM CraftWar WHERE USERNAME ="+"'"+username.toLowerCase() +"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("MINE");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * Use this method to get the number of units a player currently has
     * @param username
     * @return
     * @throws SQLException
     */
    public int selectUnitsCraftWar(String username) throws SQLException {
        int res;
        Statement stmt;
        ResultSet resultSet;
        stmt = _connection.createStatement();
        String sql = "SELECT UNITS FROM CraftWar WHERE USERNAME ="+"'"+username.toLowerCase()+"'"+" ;";
        resultSet = stmt.executeQuery(sql);
        res = resultSet.getInt("UNITS");
        resultSet.close();
        stmt.close();
        return res;
    }

    /**
     * Use this method to change the gold number a player has
     * @param username
     * @param value
     * @throws SQLException
     */
    public void updateGoldCraftWar(String username,int value) throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE CraftWar set GOLD ="+value+" WHERE USERNAME="+"'"+username.toLowerCase() +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to change the barracks status of a player
     * @param username
     * @param value
     * @throws SQLException
     */
    public void updateBarracksCraftWar(String username,int value) throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE CraftWar set BARRACKS ="+value+" WHERE USERNAME="+"'"+username.toLowerCase() +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to update the Mine Level of a player
     * @param username
     * @param value
     * @throws SQLException
     */
    public void updateMineCraftWar(String username,int value) throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE CraftWar set MINE ="+value+" WHERE USERNAME="+"'"+username.toLowerCase() +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to update the unit number of a player
     * @param username
     * @param value
     * @throws SQLException
     */
    public void updateUnitsCraftWar(String username,int value) throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "UPDATE CraftWar set UNITS ="+value+" WHERE USERNAME="+"'"+username.toLowerCase() +"'"+" ;";
        stmt.execute(sql);
        _connection.commit();
        stmt.close();
    }

    /**
     * Use this method to drop a player from the table
     * @param username
     * @throws SQLException
     */
    public void deletePlayerCraftWar(String username) throws SQLException{
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "DELETE from CraftWar WHERE USERNAME="+"'"+username +"'"+" ;";
        stmt.executeUpdate(sql);
        _connection.commit();
        stmt.close();
    }

    public void dropCraftWar() throws SQLException {
        Statement stmt;
        stmt = _connection.createStatement();
        String sql = "DROP TABLE CraftWar";
        stmt.execute(sql);
        stmt.close();
        _connection.commit();

    }

}

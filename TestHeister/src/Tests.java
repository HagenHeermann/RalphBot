import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Hagen on 08.12.2015.
 */
public class Tests {
    public static void main(String[]args){
        String stuff = "ololol";
        File file = new File("../TestHeister/src/LogFiles/test.txt");
        if(file.exists()){
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(stuff);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

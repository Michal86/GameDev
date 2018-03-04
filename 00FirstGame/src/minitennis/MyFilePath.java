/*
 * Manual file separator, wrote to help me with paths for sound files
 * Checked https://www.mkyong.com and obviously stackoverflow.com to help me get an idea of a problem.
 * Program couldn't find sound files after building jar
 */
package minitennis;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public class MyFilePath {
    final static String packageName = "minitennis";
    final static String folderName = "sounds";

    public AudioInputStream getSound(String fileName){
        InputStream inputSound;
        AudioInputStream audioInStr;
        String fs = File.separator;

        try {

            String workingDir = Game.class.getResource("Game.class").toString();
            workingDir = workingDir.substring(6,workingDir.length()-10);
            String absolutePath = "";

            String your_os = System.getProperty("os.name").toLowerCase();

            //--- if windows ---
            if (your_os.indexOf("win") >= 0){
                //--- if .jar ---
                if( workingDir.contains(".jar!")){
                    absolutePath = fs +packageName+ fs +folderName+ fs +fileName;
                    inputSound = getClass().getResourceAsStream(absolutePath);

                    //if null pointer exception try unix, for some reason \\ doesn't work on win 8.1
                    if(inputSound == null) {
                        absolutePath = "/" + packageName + "/" + folderName + "/" + fileName;

                        inputSound = getClass().getResourceAsStream(absolutePath);
                    }

                    audioInStr = AudioSystem.getAudioInputStream(new BufferedInputStream(inputSound));
                    return audioInStr;
                }//end of .jar
                else
                    absolutePath = workingDir + fs + folderName + fs + fileName;

            //--- if unix, mac ---
            } else if (your_os.indexOf("nix") >= 0 ||
                       your_os.indexOf("nux") >= 0 ||
                       your_os.indexOf("mac") >= 0){
                //--- if .jar ---
                if( workingDir.contains(".jar!")){
                    absolutePath = "/" +packageName+ "/" +folderName+ "/" +fileName;
                    inputSound = getClass().getResourceAsStream(absolutePath);
                    audioInStr = AudioSystem.getAudioInputStream(new BufferedInputStream(inputSound));

                    return audioInStr;
                }//end of .jar
                else
                    absolutePath = workingDir+ "/" +folderName + "/" + fileName;
            }
            //--- if unknow os ---
            else {
                absolutePath = workingDir+ fs +folderName + fs + fileName;
            }

            return audioInStr =  AudioSystem.getAudioInputStream(new File(absolutePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

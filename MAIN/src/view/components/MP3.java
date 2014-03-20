package view.components;

/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import javazoom.jl.player.Player;
import org.apache.commons.io.FilenameUtils;
import util.Config;
import util.Logging;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MP3 {
    private File file;
    private Player player;

    // constructor that takes the name of an MP3 file
    public MP3(File file) {
        this.file = file;
    }

    public MP3(String fileName) throws IOException {
        String extension = FilenameUtils.getExtension(fileName);
        File originalFile = extension.isEmpty() ? new File(Config.getAudioFolder() + fileName + ".mp3") : new File(Config.getAudioFolder() + fileName);
        File originalFileAbsolute = extension.isEmpty() ? new File(fileName + ".mp3") : new File(fileName);
        if (originalFile.exists()) this.file = originalFile;
        else if (originalFileAbsolute.exists()) this.file = originalFileAbsolute;
        else throw new IOException("Audio File Not Found");
    }

    public void close() {
        if (player != null) player.close();
    }

    // play the MP3 file to the sound card
    public void play() {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            SwingDialog.error("Error opening audio file " + file.getName(), "Opeb Audio");
            Logging.error("Problem opening audio file " + file.getName() + ": " + e.getMessage());
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try {
                    player.play();
                } catch (Exception e) {
                    SwingDialog.error("Error playing audio file " + file.getName(), "Play Audio");
                }
            }
        }.start();

    }

    // test client
    public static void main(String[] args) {
        String filename = args[0];
        MP3 mp3 = new MP3(new File(filename));
        mp3.play();

        // do whatever computation you like, while music plays
        int N = 4000;
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += Math.sin(i + j);
            }
        }

        // when the computation is done, stop playing it
        mp3.close();

        // play from the beginning
        mp3 = new MP3(new File(filename));
        mp3.play();

    }

}

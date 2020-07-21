/**@author JacobVetere
 * File helper Contains Code for spliting files for send
 */
package streamingThing;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

import static streamingThing.Client.FILE_TO_RECIVE;

public class fileUtil {
    //Helper class to split file
    public static ArrayList<File> split(File f) throws IOException{

        int bitsOfSound = 1;
        int sizeOfFiles = 1024 * 1024;

        byte[] buffer = new byte[sizeOfFiles];

        ArrayList<File> playback = new ArrayList<>();

        String fileName = f.getName();
        int indexOf = fileName.indexOf('.');
        String newName = fileName.substring(0,indexOf);

        try(FileInputStream in = new FileInputStream(f);

                BufferedInputStream bin = new BufferedInputStream(in)) {

            {

                int bytesAmount = 0;
                while ((bytesAmount = bin.read(buffer)) > 0) {

                    String filePieceName = String.format("%S%03d.mp3", newName, bitsOfSound++);
                    File newFile = new File(f.getParent(), filePieceName);
                    playback.add(newFile);

                    try (FileOutputStream out = new FileOutputStream(newFile)){

                        out.write(buffer, 0, bytesAmount);

                    }
                }
            }
        }
        return playback;
    }
    //helper class to reconstruct file
    public static File reconstruct(ArrayList<File> song) throws IOException {
        File output = new File(FILE_TO_RECIVE + "final.mp3");
        try (FileOutputStream fos = new FileOutputStream(output);
             BufferedOutputStream merge = new BufferedOutputStream(fos)) {
            for (File bits : song) {
                Files.copy(bits.toPath(), merge);
            }
        }
        return output;
    }
    //helper class to index songs from a director
    public static ArrayList<File> getSongs (File dir) {

        ArrayList<File> songs = new ArrayList<>();

        File[] folder = dir.listFiles();

        for (File file: Objects.requireNonNull(folder)){
            if(file != null && file.getName().toLowerCase().endsWith(".mp3") ){
                songs.add(file);
            }
        }
        return songs;
    }
}

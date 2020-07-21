package streamingThing;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class mediaPlayer {
    //instantiates media player object with events for onReady and on end
    public static MediaView MPArray(File song){

        Media toPlay = new Media(song.toURI().toString());
        MediaPlayer newPlayInstance = new MediaPlayer(toPlay);
        newPlayInstance.setOnReady(() -> {
            newPlayInstance.play();
            newPlayInstance.setOnEndOfMedia(newPlayInstance::dispose);
        });
        return new MediaView(newPlayInstance);

    }

}

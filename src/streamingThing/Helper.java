package streamingThing;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Helper {
    //Used to create VBox Object with data in it
    public static VBox musicSelection(String[] files){
        VBox rtr = new VBox();
        rtr.setMaxHeight(400);
        rtr.setMaxWidth(200);
        rtr.setPadding(new Insets(5,5,5,5));
        for (String fil: files) {
            int index = fil.indexOf('.');
            Label song = new Label(fil.substring(0, index));
            rtr.getChildren().add(song);
        }
        return rtr;
    }
}

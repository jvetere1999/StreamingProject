/**@author JacobVetere
 * Client side of this little streaming test
 */
package streamingThing;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;




public class Client extends Application {

    private final static int SOCKET_PORT = 8000;
    private final static String SERVER = "localhost";
    public final static String FILE_TO_RECIVE = "C:\\Users\\JacobsDesktop\\Documents\\CS220\\Lab 7 mp3\\src\\streamingThing\\Files Recived\\";

    private DataOutputStream to = null;
    private BorderPane mainPane = new BorderPane();

    private static final ArrayList<File> song = new ArrayList<File>();

    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    private final Font standard = new Font("Arial", 30);

    @Override
    public void start (Stage primaryStage) {//Creates UI elements (Left over from original server program)

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));

        pane.setStyle("-fx-border-color: green");
        Label instructions = new Label("Enter number of song");

        instructions.setFont(standard);
        pane.setLeft(instructions);

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        tf.setFont(standard);
        pane.setCenter(tf);
        mainPane.setTop(pane);

        Scene scene = new Scene(mainPane, 1000, 800);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        tf.setOnAction(e->{
            int bitsOfSound = 1;
            int sizeOfFiles =  8192*8192;
            byte[] buffer = new byte[sizeOfFiles];
            Socket sock = null;

            try {
                sock = new Socket(SERVER, SOCKET_PORT);
                toServer = new DataOutputStream(sock.getOutputStream());
                fromServer = new DataInputStream(sock.getInputStream());
                stringToFX(fromServer.readUTF());
                toServer.writeInt((Integer.parseInt(tf.getText())));
                try(InputStream is = sock.getInputStream();
                    BufferedInputStream bin = new BufferedInputStream(is)) {
                    {//This currently splits the song further will be removed when I have time
                        int bytesAmount;
                        while ((bytesAmount = bin.read(buffer)) > 0) {
                            String filePieceName = String.format("%S%03d.mp3", "jesus", bitsOfSound++);
                            File newFile = new File(FILE_TO_RECIVE, filePieceName);
                            try (FileOutputStream out = new FileOutputStream(newFile)){
                                out.write(buffer, 0, bytesAmount);
                            }
                            song.add(newFile);
                        }
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                buildPlayback();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Conecting...");
        });
    }
   public void buildPlayback() throws IOException {//reconstructs file and builds playback using MPArray
        MediaView x = mediaPlayer.MPArray(fileUtil.reconstruct(song));
        mainPane.setCenter(x);
        x.requestFocus();
    }
    //Supposed to take in server input string and make it a VBox Object
    //Not working
    public void stringToFX(String str){
        System.out.println(str);
        VBox songList = Helper.musicSelection(str.split("\\r?\\n"));
        mainPane.setCenter(songList);
        songList.requestFocus();
    }
    public static void main(String[] args) {
        launch(args);
    }


}
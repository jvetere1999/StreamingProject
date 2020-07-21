/**@author JacobVetere
 * Server side of this little streaming test
 */
package streamingThing;

import javafx.application.Application;
    import javafx.stage.Stage;

    import java.io.*;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.ArrayList;


public class Server extends Application {

    public final static int SOCKET_PORT = 8000;
    public final static File FILE_TO_SEND = new File("C:\\Users\\JacobsDesktop\\Documents\\CS220\\Lab 7 mp3\\src\\streamingThing\\songFiles\\");

    private BufferedInputStream bufferedInput = null;
    private OutputStream outputStream = null;
    private ServerSocket serverSocket = null;
    private Socket sock = null;
    private ArrayList<File> files = new ArrayList<>();

    @Override//Used to start server (no UI atm)
    public void start(Stage primaryStage) throws IOException {
       files = fileUtil.getSongs(FILE_TO_SEND);
       connectToClient();
    }
    //Connects to client over socket sends list of songs to client for display and recives data from client for which
    //song to send
    public void connectToClient() throws IOException {
        String send = sendSongSelection();
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            while (true){
                System.out.println("Waiting...");
                try{
                    sock = serverSocket.accept();
                    System.out.println("Accepted connection: " + sock);
                    DataInputStream fromClient = new DataInputStream(sock.getInputStream());
                    DataOutputStream toClient = new DataOutputStream(sock.getOutputStream());
                    toClient.writeUTF(send);
                    int index = fromClient.readInt();
                    ArrayList<File> file = fileUtil.split(files.get(index));
                    for (File toSend: file) {
                        sendData(toSend);
                    }
                }
                finally {
                    if (bufferedInput != null) bufferedInput.close();
                    if (outputStream != null) outputStream.close();
                    if (sock != null) sock.close();
                }
            }
        }
        finally { if (serverSocket != null) serverSocket.close(); }
    }
    //Creates string to send to client
    public String sendSongSelection(){
        StringBuilder send = new StringBuilder();
        for (File str: files) {
            send.append(str.getName()).append('\n');
        }
        return send.toString();
    }
    //Called to send split data over socket
    public void sendData(File myFile) throws IOException {
        byte [] byteArr = new byte [(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        bufferedInput = new BufferedInputStream(fis);
        bufferedInput.read(byteArr,0, byteArr.length);
        outputStream = sock.getOutputStream();
        System.out.println("Sending "+ FILE_TO_SEND + "(" + byteArr.length+" bytes");
        outputStream.write(byteArr, 0, byteArr.length);
        outputStream.flush();
        System.out.println("Done");
    }

    public static void main( String[] args) { launch(args); }
}

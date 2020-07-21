/**@author JacobVetere
 * Class for creating song file objects to be implemented later
 */
package streamingThing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongFileServer {

    private String name;//Holds name of song
    private File location;//Holds file object for original file
    private ArrayList<File> splitFileSet;//Holds the split file set

    public SongFileServer(){//default constructor

    }
    public SongFileServer(File location){//constructor for creating actual object
        name = location.getName();
        this.location = location;
    }

    public String getName() {//Returns string name
        return name;
    }

    public File getLocation() {//returns file object
        return location;
    }
    public void splitFileSet() throws IOException {//Splits file into smaller pieces
        splitFileSet = fileUtil.split(this.location);
    }
}

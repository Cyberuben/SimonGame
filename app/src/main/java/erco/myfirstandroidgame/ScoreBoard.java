package erco.myfirstandroidgame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Purpose of this class is to allow passing by reference
 *
 * Created by ercoa on 26-11-2016.
 */
public class ScoreBoard {
    // deliberately package scope; it's used as a struct
    int myPoints = 0;
    int adversaryPoints = 0;

    public void writeToStream(DataOutputStream daos) throws IOException {
        daos.writeInt(myPoints);
        daos.writeInt(adversaryPoints);
    }

    public void reverseReadFromStream(DataInputStream dais) throws IOException {
        // reverse score
        adversaryPoints = dais.readInt();
        myPoints = dais.readInt();
    }

}

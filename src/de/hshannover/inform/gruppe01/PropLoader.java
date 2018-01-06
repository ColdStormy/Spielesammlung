package de.hshannover.inform.gruppe01;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by jannis on 19.12.17.
 */
public class PropLoader {

    static final String KEY_IMAGE = "imagePath";
    static final String KEY_DESC = "description";
    static final String KEY_JAR = "jarPath";

    public HashMap<String, HashMap<String, String>> author;

    public void loadConf(String pathToFile) throws FileNotFoundException, URISyntaxException {
        author = new HashMap<>();

        File conf = new File(pathToFile);
        if( !conf.exists() ) {
            System.out.println("Config-Datei fehlt..");
            System.exit(-1);
        }


        Scanner s = new Scanner(conf);

        while(s.hasNextLine()) {
            HashMap<String, String> keyVal = new HashMap<>();

            String next = s.nextLine();
            if( next.contains("set") ) {
                String name = next.substring(4);
                author.put(name, keyVal);
            } else {
                // invalid file format
                continue;
            }

            for(int i=0;i<3;i++) {
                next = s.nextLine();
                String[] keyValAr = next.split("=");
                keyVal.put(keyValAr[0], keyValAr.length == 2 ? keyValAr[1] : "");
            }
        }

        s.close();

    }

}

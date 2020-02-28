package org.smallutil.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 *
 * @author <a href="mailto:almatarm@gmail.com">Mufeed H. AlMatar</a>
 * @version 1.0
 */
public class JsonUtil {
    public static String prettyPrint(String jsonString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson((new JsonParser()).parse(jsonString));
    }
}

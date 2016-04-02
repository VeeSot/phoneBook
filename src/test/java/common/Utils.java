package common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * veesot on 4/2/16.
 */
public class Utils {
    public static String getRandomNumber() {
        Random rand = new Random();
        int number = rand.nextInt(100000000);

        DecimalFormat df10 = new DecimalFormat("0000000000"); // 10 zeros for Russian number

        return "8" + df10.format(number);
    }

    public static String getRandomName() {
        return UUID.randomUUID().toString();// Yeah, it's name =)
    }
    public static HashMap stringToHashMap(String jsonString) {
         HashMap<String,String> map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());
         return map;
    }
}

/**
 * Created by Jacob on 1/3/2016.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Map;

public class diceware {

    public static void main(String[] args) {

        Map<String, String> dicewareMap = getDicewareMap();
        SecureRandom sr = getSecureRandom();

        int n = 5; // default
        try {
            n = Integer.parseInt(args[0]);
        } catch (Exception ignored) {}

        for (int i = 0; i < n; i++) {
            String rollKey = fiveRolls(sr);
            System.out.println(dicewareMap.get(rollKey));
        }
    }

    // Return a string represtenting a roll of five dice, such as "12346"
    private static String fiveRolls(SecureRandom sr) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 5; i++) {
            int roll = sr.nextInt(6) + 1;
            sb.append(roll);
        }
        return sb.toString();
    }

    // Get dicewareMap from resource (jar).
    private static Map<String,String> getDicewareMap() {
        Gson gson = new Gson();
        InputStream IS = diceware.class.getResourceAsStream("diceware-map.json");
        JsonReader reader;
        try {
            reader = new JsonReader(new InputStreamReader(IS, "ASCII"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Type dicewareWordPairType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> dicewareMap = gson.fromJson(reader, dicewareWordPairType);
        return dicewareMap;
    }

    // Perpare to choose random numbers.
    // https://www.cigital.com/blog/proper-use-of-javas-securerandom/
    private static SecureRandom getSecureRandom() {
        SecureRandom sr;
        try {
            try {
                sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            } catch (NoSuchProviderException e) {
                sr = SecureRandom.getInstance("SHA1PRNG");
            }
        } catch (NoSuchAlgorithmException e1) {
            sr = new SecureRandom();
        }
        sr.nextBytes(new byte[8]);
        return sr;
    }


}

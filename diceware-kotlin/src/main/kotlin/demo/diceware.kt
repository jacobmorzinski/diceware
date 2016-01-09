/**
 * Created by Jacob on 1/8/2016.
 */

package demo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom

fun main(args: Array<String>) {
    val dicewareMap = DicewareMap.getDicewareMap()
    val sr = getSecureRandom()

    var n = 5  // default
    try {
        n = Integer.parseInt(args[0])
    } catch(ignored: Exception) {}

    for (i in 1..n) {
        var rollKey = fiveRolls(sr);
        println(dicewareMap.get(rollKey));
    }
}

fun fiveRolls(sr: SecureRandom): String {
    val sb: StringBuilder = StringBuilder("");
    for (i in 1..5) {
        var roll = sr.nextInt(6) + 1;
        sb.append(roll);
    }
    return sb.toString();
}

object DicewareMap {
    fun getDicewareMap(): Map<String, String> {
        val gson = Gson()
        val IS = DicewareMap::class.java.getResourceAsStream("/diceware-map.json")
        val reader = JsonReader(InputStreamReader(IS, "ASCII"))
        val dicewareWordPairType = genericType<Map<String,String>>()
        val dicewareMap: Map<String,String> = gson.fromJson(reader, dicewareWordPairType)
        return dicewareMap
    }
}

// a helper function to make it easier to create a TypeToken
// http://stackoverflow.com/a/33420043/3599738
inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

fun getSecureRandom(): SecureRandom {
    val sr: SecureRandom;
    try {
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN")
        } catch(e: NoSuchProviderException) {
            sr = SecureRandom.getInstance("SHA1PRNG")
        }
    } catch(e: NoSuchAlgorithmException) {
        sr = SecureRandom()
    }
    sr.nextBytes(ByteArray(8))
    return sr
}
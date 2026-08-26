/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.application.services.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility per calcolare hash (MD5) di stringhe. Usato per generare l'hash di una password.
 */
public class HashBuilder {

    /**
     * Converte una stringa in una rappresentazione MD5 esadecimale.
     * @param input stringa di input
     * @return hash MD5 in formato esadecimale (lower-case)
     * @throws NoSuchAlgorithmException se l'algoritmo MD5 non è disponibile
     */
    public static String convertToMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}


package de.marvinleiers.authplugin.enryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security
{
    public static String encrypt(String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder output = new StringBuilder();

            for (byte aByte : bytes)
                output.append(String.format("%02x", aByte));

            return output.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}

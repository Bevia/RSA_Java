package rsa_mutual_authentication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class SignSenderPuK {
    private static byte[] signData(String data, String privateKeyFile) throws Exception {
        // Read the private key from file
        String key = new String(Files.readAllBytes(Paths.get(privateKeyFile)));
        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Print RSA key size
        System.out.println("RSA key size: " + ((RSAPrivateKey) privateKey).getModulus().bitLength() + " bits");

        // Compute the SHA-256 hash of the data
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes("UTF-8"));

        // Print SHA-256 digest size
        System.out.println("SHA-256 digest size: " + hash.length + " bytes");

        // Sign the hash
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(hash);
        return signature.sign();
    }

    public static void main(String[] args) {
        try {
            // Read the sender's public key file
            String senderPubKey = new String(Files.readAllBytes(Paths.get("sender_public.pem")));

            // Sign the sender's public key with the recipient's private key
            byte[] signature = signData(senderPubKey, "recipient_private.pem");

            // Write the signature to a file
            try (FileOutputStream fos = new FileOutputStream("sender_public.sig")) {
                fos.write(signature);
            }

            System.out.println("Signature created and saved to sender_public.sig");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
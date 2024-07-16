package rsa_mutual_authentication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Verifying {


    private static boolean verifySignature(String data, byte[] signature, String publicKeyFile) throws Exception {
        // Read the public key from file
        String key = new String(Files.readAllBytes(Paths.get(publicKeyFile)));
        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Compute the SHA-256 hash of the data
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes("UTF-8"));

        // Verify the signature
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(hash);
        return sig.verify(signature);
    }

    public static void main(String[] args) {
        try {
            // Read the sender's public key file
            String senderPubKey = new String(Files.readAllBytes(Paths.get("sender_public.pem")));

            // Read the signature file
            byte[] signature = Files.readAllBytes(Paths.get("sender_public.sig"));

            // Verify the signature
            boolean valid = verifySignature(senderPubKey, signature, "recipient_public.pem");

            if (valid) {
                System.out.println("Signature is valid.");
            } else {
                System.out.println("Signature is invalid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

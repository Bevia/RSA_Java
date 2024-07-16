package rsa_mutual_authentication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class GenerateKeyPair {

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private static void saveKeyToFile(String filePath, byte[] key) throws IOException {
        try (OutputStream os = new FileOutputStream(filePath)) {
            os.write(key);
        }
    }

    private static String encodeKeyToPEM(String type, byte[] key) {
        String keyBase64 = Base64.getEncoder().encodeToString(key);
        String pemKey = "-----BEGIN " + type + "-----\n";
        pemKey += keyBase64.replaceAll("(.{64})", "$1\n");
        pemKey += "\n-----END " + type + "-----\n";
        return pemKey;
    }

    private static void generateAndPrintKeyPair(String privateKeyFile, String publicKeyFile) {
        try {
            KeyPair keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            String privatePem = encodeKeyToPEM("RSA PRIVATE KEY", privateKey.getEncoded());
            String publicPem = encodeKeyToPEM("RSA PUBLIC KEY", publicKey.getEncoded());

            saveKeyToFile(privateKeyFile, privatePem.getBytes());
            saveKeyToFile(publicKeyFile, publicPem.getBytes());

            System.out.println("Private Key:\n" + privatePem);
            System.out.println("Public Key:\n" + publicPem);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error generating or saving key pair.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Sender Keys:");
        generateAndPrintKeyPair("sender_private.pem", "sender_public.pem");

        System.out.println("\nRecipient Keys:");
        generateAndPrintKeyPair("recipient_private.pem", "recipient_public.pem");
    }
}

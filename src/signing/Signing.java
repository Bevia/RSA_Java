package signing;

import java.security.*;
import java.util.Base64;

public class Signing {

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private static String signMessage(PrivateKey privateKey, String message) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes("UTF-8"));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    private static boolean verifySignature(PublicKey publicKey, String message, String signature) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes("UTF-8"));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPair keyPair = generateKeyPair();

            // Extract and print public key
            PublicKey publicKey = keyPair.getPublic();
            String pubKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("Public Key:\n" + pubKeyBase64);

            // Message to be signed
            String message = "Hello, Bob!";

            // Sign the message
            String signature = signMessage(keyPair.getPrivate(), message);
            System.out.println("Signature (Base64): " + signature);

            // Verify the signature
            boolean isValid = verifySignature(keyPair.getPublic(), message, signature);
            if (isValid) {
                System.out.println("The signature is valid.");
            } else {
                System.out.println("The signature is invalid.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

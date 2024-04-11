// Java Program to Implement the RSA Algorithm

import java.math.*;
import java.util.*;

///https://www.javatpoint.com/rsa-encryption-algorithm

/*
Generating Public Key

1. Select two prime no's. Suppose P = 53 and Q = 59.
Now First part of the Public key  : n = P*Q = 3127.

2. We also need a small exponent say e :
   But e Must be

    -An integer.

    -Not be a factor of n.

    -1 < e < Φ(n) [Φ(n) is discussed below],
     Let us now consider it to be equal to 3.

The public key has been made of n and e

Generating Private Key

1. We need to calculate Φ(n) :
   Such that Φ(n) = (P-1)(Q-1)
      so,  Φ(n) = 3016

2. Now calculate Private Key, d :
   d = (k*Φ(n) + 1) / e for some integer k

3. For k = 2, value of d is 2011.

The private key has been made of d
    Consider two prime numbers p and q.
    Compute n = p*q
    Compute ϕ(n) = (p – 1) * (q – 1)
    Choose e such gcd(e , ϕ(n) ) = 1
    Calculate d such e*d mod ϕ(n) = 1
    Public Key {e,n} Private Key {d,n}
    Cipher text C = Pe mod n where P = plaintext
    For Decryption D = Dd mod n where D will refund the plaintext.
 */

class RSA {
    public static void main(String args[]) {
        int p, q, n, phi, d = 0, e, i;

        // The number to be encrypted and decrypted
        int msg = 2;
        double c;
        BigInteger msgback;

        // 1st prime number p
        p = 2;

        // 2nd prime number q
        q = 7;

        // Value of N
        n = p * q;
        System.out.println("the value of N = " + n);

        // value of phi
        phi = (p - 1) * (q - 1);
        System.out.println("the value of phi = " + phi);

        for (e = 2; e < phi; e++) {

            // e is for public key exponent
            if (gcd(e, phi) == 1) {
                break;
            }
        }

        System.out.println("the value of e = " + e);
        for (i = 0; i <= 9; i++) {
            int x = 1 + (i * phi);

            // d is for private key exponent
            if (x % e == 0 && d != e ) {
                d = x / e;
                break;
            }
        }
        System.out.println("the value of d = " + d);
        System.out.println("the message in clear= " + msg);
        /*
            C = me mod n
            Here, m must be less than n.
         */

        c = (Math.pow(msg, e)) % n;
        System.out.println("Encrypted message is : " + c);

        // converting int value of n to BigInteger
        BigInteger N = BigInteger.valueOf(n);

        // converting float value of c to BigInteger
        BigInteger C = BigDecimal.valueOf(c).toBigInteger();
        msgback = (C.pow(d)).mod(N);
        System.out.println("Decrypted message is : "
                + msgback);
    }

    static int gcd(int e, int z) {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }
}

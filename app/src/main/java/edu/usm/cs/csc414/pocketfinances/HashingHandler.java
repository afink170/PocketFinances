package edu.usm.cs.csc414.pocketfinances;

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.SecureRandom;

public class HashingHandler {

    private static final int ITERATIONS = 100;
    private static final int NUM_BYTES = 20;

    public static byte[] getHash(byte[] salt, byte[] pin) {
        PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator();
        kdf.init(pin, salt, ITERATIONS);
        return ((KeyParameter) kdf.generateDerivedMacParameters(8 * NUM_BYTES)).getKey();
    }


    public static byte[] generateSalt() {
        SecureRandom rng = new SecureRandom();
        return rng.generateSeed(NUM_BYTES);
    }
}

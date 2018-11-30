package edu.usm.cs.csc414.pocketfinances;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashingHandlerTest {

    @Test
    public void testGetHash() {

        final byte[] salt = HashingHandler.generateSalt();
        final char[] pin = {'1','2','3','4'};

        byte[] hash = HashingHandler.getHash(salt, new String(pin).getBytes());

        assertArrayEquals(hash, HashingHandler.getHash(salt, new String(pin).getBytes()));

    }
}
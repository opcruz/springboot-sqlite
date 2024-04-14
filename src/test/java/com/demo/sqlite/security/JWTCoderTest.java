package com.demo.sqlite.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JWTCoderTest {

    private static final String TEST_SUBJECT = "testSubject";
    private static final List<String> TEST_ROLES = Arrays.asList("role1", "role2");
    private static final int TEST_USER_ID = 123;

    @Test
    public void testGenerateJWT() {
        String jwt = JWTCoder.generateJWT(TEST_SUBJECT, TEST_ROLES, TEST_USER_ID);
        assertNotNull(jwt);
    }

    @Test
    public void testParseJWT() {
        String jwt = JWTCoder.generateJWT(TEST_SUBJECT, TEST_ROLES, TEST_USER_ID);
        Claims claims = JWTCoder.parseJWT(jwt);
        assertNotNull(claims);
        assertEquals(TEST_SUBJECT, claims.getSubject());
        assertEquals(TEST_USER_ID, claims.get("userId", Integer.class));
        assertEquals(TEST_ROLES, claims.get("roles", List.class));
    }
}
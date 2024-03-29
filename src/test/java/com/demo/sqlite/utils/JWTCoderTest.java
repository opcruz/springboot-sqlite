package com.demo.sqlite.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
public class JWTCoderTest {

    @Test
    public void testAdd() {
        String jwt = JWTCoder.generateJWT("admin", Collections.singletonList("admin"), 1);
        System.out.println(jwt);
        Claims claims = JWTCoder.parseJWT(jwt);
        System.out.println(claims);
//        assertEquals(5, result);
    }
}
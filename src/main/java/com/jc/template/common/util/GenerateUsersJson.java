package com.jc.template.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.javafaker.Faker;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateUsersJson {
    public static void main(String[] args) throws Exception {
        Faker faker = new Faker();
        List<Map<String, String>> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Map<String, String> user = new LinkedHashMap<>();
            user.put("firstName", faker.name().firstName());
            user.put("lastName", faker.name().lastName());
            user.put("userId", "user" + faker.number().digits(5));
            user.put("password", "Admin@123");
            user.put("email", faker.internet().emailAddress());
            user.put("address1", faker.address().streetAddress());
            user.put("address2", faker.address().secondaryAddress());
            user.put("city", faker.address().city());
            user.put("state", faker.address().state());
            user.put("country", faker.address().country());
            user.put("pinCode", faker.address().zipCode());
            user.put("mobileNumber", faker.phoneNumber().cellPhone());
            user.put("day", String.valueOf(faker.number().numberBetween(1, 29)));
            user.put("month", String.valueOf(faker.number().numberBetween(1, 13)));
            user.put("year", String.valueOf(faker.number().numberBetween(1970, 2010)));
            users.add(user);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File("users.json"), users);
    }
}
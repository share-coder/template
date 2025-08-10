// File: src/main/java/com/jc/envelop/common/util/UserJsonUtils.java
package com.jc.template.feature.usermgmt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserJsonUtils {

    public static class UserJson {
        public String firstName;
        public String lastName;
        public String userId;
        public String password;
        public String email;
        public String address1;
        public String address2;
        public String mobileNumber;
        public String dob;
    }

    public static void generateDummyUsersJson(String filePath) throws IOException {
        List<UserJson> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            UserJson user = new UserJson();
            user.firstName = "First" + i;
            user.lastName = "Last" + i;
            user.userId = "user" + i;
            user.password = "Password@" + i;
            user.email = "user" + i + "@example.com";
            user.address1 = "Address1-" + i;
            user.address2 = "Address2-" + i;
            user.mobileNumber = String.format("9000000%03d", i);
            user.dob = "1/1/1990";
            users.add(user);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filePath), users);
    }

    public static void main(String[] args) {
        String filePath = "dummy_users.json";
        try {
            generateDummyUsersJson(filePath);
            System.out.println("Dummy users JSON generated at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error generating JSON: " + e.getMessage());
        }
    }
}
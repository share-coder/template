// File: src/main/java/com/jc/envelop/common/util/CsvUtils.java
package com.jc.template.common.util;

import java.io.FileWriter;
import java.io.IOException;

public class CsvUtils {

    public static void generateDummyCsv(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("name,mobile,email\n");
            for (int i = 1; i <= 100; i++) {
                String name = "Name" + i;
                String mobile = "9000000" + String.format("%03d", i);
                String email = "user" + i + "@example.com";
                writer.append(name).append(",")
                        .append(mobile).append(",")
                        .append(email).append("\n");
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "dummy_contacts.csv";
        try {
            generateDummyCsv(filePath);
            System.out.println("Dummy CSV file generated at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error generating CSV file: " + e.getMessage());
        }
    }
}
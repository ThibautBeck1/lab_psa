package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream("/toy.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            ParseClass.Parse(br);
        }

    }
}

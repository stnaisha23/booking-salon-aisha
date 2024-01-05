package com.booking.service;

import java.util.Scanner;

public class ValidationService {
    public static String validateInput(Scanner scanner, String question, String error, String regex) {
        String result = "";
        boolean isLooping = true;
        do {
            System.out.print(question);
            String input = scanner.nextLine();
            if (input.matches(regex)) {
                result = input;
                isLooping = false;
            } else {
                System.out.println(error);
            }
        } while (isLooping);
        return result;
    }
}

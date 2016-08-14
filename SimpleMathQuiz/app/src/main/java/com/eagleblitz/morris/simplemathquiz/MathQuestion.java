package com.eagleblitz.morris.simplemathquiz;

import java.util.Locale;
import java.util.Random;

/**
 * Created by mcmor on 2016-08-11.
 */
public class MathQuestion {

    public static final int ADDITION = 0;
    public static final int SUBTRACTION = 1;
    public static final int MULTIPLICATION = 2;
    public static final int DIVISION = 3;
    public static final int MODULUS = 4;

    Random random;

    int num1, num2;
    int operation;

    public MathQuestion() {
        random = new Random();

        operation = random.nextInt(5);

        switch(operation) {
            case ADDITION:
                num1 = random.nextInt(100);
                num2 = random.nextInt(100-num1);
                break;
            case SUBTRACTION:
                num1 = random.nextInt(100);
                num2 = random.nextInt(num1+1);
                break;
            case MULTIPLICATION:
                num1 = random.nextInt(13);
                num2 = random.nextInt(13);
                break;
            case DIVISION:
                num2 = random.nextInt(12) + 1;
                num1 = (random.nextInt(12) + 1) * num2;
                break;
            case MODULUS:
                num2 = random.nextInt(10) + 2;
                num1 = random.nextInt(100-num2) + num2;
                break;
            default:
                num1 = 0;
                num2 = 0;
        }
    }

    public int getAnswer() {
        switch (operation) {
            case ADDITION: return num1 + num2;
            case SUBTRACTION: return num1 - num2;
            case MULTIPLICATION: return num1 * num2;
            case DIVISION: return num1 / num2;
            case MODULUS: return num1 % num2;
            default: return 0;
        }
    }

    @Override
    public String toString() {
        String result = "%d %c %d = ";

        char operator;
        switch (operation) {
            case ADDITION: operator = '+'; break;
            case SUBTRACTION: operator = '-'; break;
            case MULTIPLICATION: operator = '×'; break;
            case DIVISION: operator = '÷'; break;
            case MODULUS: operator = '%'; break;
            default: operator = '?';
        }

        return String.format(Locale.getDefault(), result, num1, operator, num2);
    }
}

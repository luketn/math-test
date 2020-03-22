package com.mycodefu;

import java.util.Arrays;

public class MathTest {
    public static void main(String[] args) {
        int[] numbers = {6, 5, 4, 2};
        int expectedAnswer = -4;

        Operator[] all_operators = {Operator.Divide, Operator.Multiply, Operator.Subtract, Operator.Add};
        for (Operator operator1 : all_operators) {
            for (Operator operator2 : all_operators) {
                for (Operator operator3 : all_operators) {
                    Operator[] operators = {operator1, operator2, operator3};
                    boolean result = runCalculation(numbers, operators, expectedAnswer);
                    if (result) {
                        System.out.println("-------------------");
                        System.out.println("Operators required:");
                        System.out.println("-------------------");
                        System.out.println(Arrays.toString(numbers));
                        System.out.println(Arrays.toString( new Operator[]{operator1, operator2, operator3}));
                        return;
                    }
                }
            }
        }
        System.out.println("-----------------------");
        System.out.printf("No way to get %d result!", expectedAnswer);
        System.out.println("-----------------------");
    }

    private static boolean runCalculation(int[] numbers, Operator[] operators, int expectedAnswer) {
        if (operators.length != numbers.length - 1) {
            throw new IllegalArgumentException("Must have one fewer operators than numbers");
        }

        float[] workingNumbers = new float[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            workingNumbers[i] = numbers[i];
        }

        System.out.println("Working through operators in order of precedence:");
        System.out.println(Arrays.toString(workingNumbers));
        System.out.println(Arrays.toString(operators));

        Operator[] operatorPrecedentList = {Operator.Divide, Operator.Multiply, Operator.Subtract, Operator.Add};
        while (workingNumbers[1] != Float.MAX_VALUE) {
            outer: for (Operator operator : operatorPrecedentList) {
                for (int i = 0; i < operators.length - 1; i++) {
                    if (operators[i] == operator) {
                        float workingNumber1 = workingNumbers[i];
                        float workingNumber2 = workingNumbers[i + 1];
                        if (workingNumber1 == Float.MAX_VALUE || workingNumber2 == Float.MAX_VALUE) {
                            break;
                        }
                        System.out.printf("About to %s %f and %f\n", operator, workingNumber1, workingNumber2);
                        float answer;
                        switch (operator) {
                            case Divide: {
                                answer = workingNumber1 / workingNumber2;
                                break;
                            }
                            case Multiply: {
                                answer = workingNumber1 * workingNumber2;
                                break;
                            }
                            case Add: {
                                answer = workingNumber1 + workingNumber2;
                                break;
                            }
                            case Subtract: {
                                answer = workingNumber1 - workingNumber2;
                                break;
                            }
                            default:
                                throw new IllegalStateException("Should never be another operator.");
                        }
                        workingNumbers[i] = answer;

                        System.arraycopy(workingNumbers, i + 2, workingNumbers, i + 1, workingNumbers.length - i - 2);
                        workingNumbers[workingNumbers.length - 1] = Float.MAX_VALUE;
                        System.arraycopy(operators, i + 1, operators, i, operators.length - i - 1);
                        operators[operators.length - 1] = Operator.None;

                        break outer;
                    }
                }
            }
        }
        float finalAnswer = workingNumbers[0];
        System.out.printf("Final answer: %f\n", finalAnswer);
        return (float) expectedAnswer == finalAnswer;
    }

    private enum Operator {
        Divide,
        Multiply,
        Subtract,
        Add,
        None
    }
}

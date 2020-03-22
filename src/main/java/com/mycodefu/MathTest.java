package com.mycodefu;

import java.util.Arrays;

public class MathTest {
    public static void main(String[] args) {
        int[] numbers = {6, 5, 4, 2};
        int expectedAnswer = 3;

        Operator[] all_operators = {Operator.Divide, Operator.Multiply, Operator.Subtract, Operator.Add};
        for (Operator operator1 : all_operators) {
            for (Operator operator2 : all_operators) {
                for (Operator operator3 : all_operators) {
                    Operator[] operators = {operator1, operator2, operator3};
                    Outcome outcome = runCalculation(numbers, operators, expectedAnswer);
                    if (outcome.success) {
                        System.out.println("--------");
                        System.out.println("Success!");
                        System.out.println("--------");
                        System.out.println(outcome);
                        return;
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("-----------------------");
        System.out.printf("No way to get %d result!\n", expectedAnswer);
        System.out.println("-----------------------");
    }

    private static Outcome runCalculation(int[] numbers, Operator[] operators, int expectedAnswer) {
        if (operators.length != numbers.length - 1) {
            throw new IllegalArgumentException("Must have one fewer operators than numbers");
        }

        float[] workingNumbers = new float[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            workingNumbers[i] = numbers[i];
        }

        Operator[] workingOperators = new Operator[operators.length];
        System.arraycopy(operators, 0, workingOperators, 0, operators.length);

        StringBuilder workingOut = new StringBuilder();

        workingOut.append("Working through operators in order of precedence:\n");
        workingOut.append(Arrays.toString(workingOperators)).append('\n');

        //BODMAS - Bracket, Of (Square/Power), Division, Multiplication, Addition and Subtraction
        Operator[] operatorPrecedentList = {Operator.Divide, Operator.Multiply, Operator.Add, Operator.Subtract};
        while (workingNumbers[1] != Float.MAX_VALUE) {
            outer:
            for (Operator operator : operatorPrecedentList) {
                for (int i = 0; i < workingOperators.length; i++) {
                    if (workingOperators[i] == operator) {
                        float workingNumber1 = workingNumbers[i];
                        float workingNumber2 = workingNumbers[i + 1];
                        if (workingNumber1 == Float.MAX_VALUE || workingNumber2 == Float.MAX_VALUE) {
                            break;
                        }
                        workingOut.append(String.format("%s %.1f and %.1f = ", operator, workingNumber1, workingNumber2));
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
                        workingOut.append(String.format("%.1f", answer)).append('\n');

                        System.arraycopy(workingNumbers, i + 2, workingNumbers, i + 1, workingNumbers.length - i - 2);
                        workingNumbers[workingNumbers.length - 1] = Float.MAX_VALUE;
                        System.arraycopy(workingOperators, i + 1, workingOperators, i, workingOperators.length - i - 1);
                        workingOperators[workingOperators.length - 1] = Operator.None;

                        break outer;
                    }
                }
            }
        }
        float finalAnswer = workingNumbers[0];
        workingOut.append(String.format("Final answer: %.1f\n", finalAnswer));

        return new Outcome((float) expectedAnswer == finalAnswer, workingOut.toString(), numbers, operators);
    }

    private static class Outcome {
        boolean success;
        int[] numbers;
        Operator[] operators;
        String workingOut;

        public Outcome(boolean success, String workingOut, int[] numbers, Operator[] operators) {
            this.success = success;
            this.workingOut = workingOut;
            this.numbers = numbers;
            this.operators = operators;
        }

        @Override
        public String toString() {
            return String.format(
                    "numbers=%s\n" +
                            "operators=%s\n" +
                            "workingOut:\n%s", Arrays.toString(numbers), Arrays.toString(operators), workingOut);
        }
    }

    private enum Operator {
        Divide,
        Multiply,
        Subtract,
        Add,
        None
    }
}

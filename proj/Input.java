import java.util.Scanner;
/**
 * The {@code Input} class provides utility methods for reading validated
 * input from the user via the console. It includes methods to read integers,
 * lines of text, and single words, with optional prompts and error checking.
 */
public class Input {
    private Scanner scanner;
    /**
     * Constructs a new {@code Input} instance using {@code System.in}.
     */
    public Input() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user and reads an integer from the input.
     * If the input is not a valid integer, prompts again until a valid integer is entered.
     *
     * @param prompt the message to prompt the user
     * @return the validated integer input
     */
    public int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("That's not an integer. Try again.");
            scanner.next();
            System.out.print(prompt);
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
    /**
     * Reads an integer from the input without any prompt.
     * If the input is not a valid integer, prompts again until a valid integer is entered.
     *
     * @return the validated integer input
     */
    public int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not an integer. Try again.");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
    /**
     * Prompts the user and reads a line of text from the input.
     * If the input is numeric, prompts again until a non-numeric line is entered.
     *
     * @param prompt the message to prompt the user
     * @return the validated line of text input
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        while (isNumeric(line.trim())) {
            System.out.println("That’s not a sentence. Try again.");
            System.out.print(prompt);
            line = scanner.nextLine();
        }
        return line;
    }
    /**
     * Reads a line of text from the input without any prompt.
     * If the input is numeric, prompts again until a non-numeric line is entered.
     *
     * @return the validated line of text input
     */
    public String readLine() {
        String line = scanner.nextLine();
        while (isNumeric(line.trim())) {
            System.out.println("That’s not a sentence. Try again.");
            line = scanner.nextLine();
        }
        return line;
    }
    /**
     * Prompts the user and reads a single word from the input.
     * If the input is numeric, prompts again until a non-numeric word is entered.
     *
     * @param prompt the message to prompt the user
     * @return the validated word input
     */
    public String readWord(String prompt) {
        System.out.print(prompt);
        String word = scanner.next();
        while (isNumeric(word)) {
            System.out.println("That's a number, not a word. Try again.");
            System.out.print(prompt);
            word = scanner.next();
            scanner.nextLine();
        }
        return word;
    }
    /**
     * Reads a single word from the input without any prompt.
     * If the input is numeric, prompts again until a non-numeric word is entered.
     *
     * @return the validated word input
     */
    public String readWord() {
        String word = scanner.next();
        while (isNumeric(word)) {
            System.out.println("That's a number, not a word. Try again.");
            word = scanner.next();
            scanner.nextLine();
        }
        return word;
    }
    /**
     * Determines whether a given string is numeric.
     *
     * @param str the string to check
     * @return {@code true} if the string is numeric, {@code false} otherwise
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Closes the underlying {@code Scanner} object.
     */
    public void close() {
        scanner.close();
    }
}

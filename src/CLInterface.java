/**
 * CLInterface.java
 * @author Stephen Kyker
 * CIS 22C, Group Project
 */

import java.util.Scanner;

/**
 * Controls the user interface by integrating the Menu class with
 * the various components of the program; this class does not require
 * any arguments to instantiate it.
 */
public class CLInterface {
    /**
     * Scanner for capturing user input via keyboard.
     * This Scanner is intended to be shared across all instances,
     * therefore the object and its wrapped stream should not be
     * closed manually or via AutoCloseable (e.g., try-with-resources).
     */
    private static final Scanner keyboardInput = new Scanner(System.in);

    /**
     * Default Constructor - not used, but explicitly implemented for best practice
     */
    public CLInterface() {}

    /**
     * Initializes the handler loop for a user session.
     */
    public void init() {
        while (this.sessionHandler()) {
            System.out.println("Restarting program...\n");
        }
    }

    /**
     * Handler for a user session.
     * @return true if session finished unsuccessfully and should be
     *         restarted; false for any other exit condition
     */
    private boolean sessionHandler() {
        try {
            this.runSession();
        } catch (Exception e1) {
            System.err.println(e1.getMessage());
            System.err.print(
                "CLInterface(): The program encountered a fatal "
                + "error. Would you like to restart (Y/N)? ");
            try {
                if (keyboardInput.nextLine().equalsIgnoreCase("Y")) {
                    return true;
                }
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
                System.err.println(
                    "CLInterface(): Input capture failed. Exiting.");
            }
        }
        return false;
    }

    /**
     * Logic controller for a user session, which integrates the
     * frontend Menu class with the backend component classes.
     */
    private void runSession() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
            "runSession(): This method is currently a stub.");
    }
}

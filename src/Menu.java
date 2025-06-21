/**
 * Menu.java
 * @author Stephen Kyker
 */
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu Class
 * Handles the menu and UI systems' logic;
 * Instantiation via the zero-argument constructor is explicitly disabled, as a
 * Menu should not be empty; use designated constructors instead.
 *
 * @author Stephen Kyker
 */
public class Menu {
    private static Scanner keyboardInput;
    private String appName;
    private String header;
    private ArrayList<String> rows;

    /* [---CONSTRUCTORS---] */
    /**
     * Default Constructor - explicitly disabled.
     * @throws UnsupportedOperationException if called
     */
    public Menu() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Menu(): This class does not support zero-argument instantiation."
        );
    }

    /**
     * One-arg constructor - Sets keyboardInput and remaining member vars to default values
     * Defaults set:
     *   appName = "UNTITLED APP"
     *   header = "UNTITLED MENU"
     *   rows = Empty ArrayList with a size of 1
     * @param keyboardInput The Scanner to capture keyboard input
     * @throws IllegalArgumentException When keyboardInput == null
     */
    public Menu (Scanner keyboardInput)  throws IllegalArgumentException {
        if (keyboardInput == null) {
            throw new IllegalArgumentException("Menu(): 'keyboardInput' is null.");
        }
        Menu.keyboardInput = keyboardInput;
        this.appName = "UNTITLED APP";
        this.header = "UNTITLED MENU";
        this.rows = new ArrayList<String>(1);
    }

    /**
     * Two-arg constructor - Sets keyboardInput, rows, and remaining member vars to default values.
     * Defaults set:
     *   appName = "UNTITLED APP"
     *   header = "UNTITLED MENU"
     * @param keyboardInput The Scanner to capture keyboard input
     * @param rows A String array representing each row of the menu
     * @throws IllegalArgumentException When keyboardInput == null || rows == null
     */
    public Menu (Scanner keyboardInput, String[] rows) throws IllegalArgumentException {
        this(keyboardInput);
        if (rows == null) {
            throw new IllegalArgumentException("Menu(): 'rows' is null.");
        }
        this.rows = new ArrayList<String>(rows.length);
        for (String row : rows) {
            this.rows.add(row);
        }
    }

    /**
     * Three-arg constructor - Sets keyboardInput, appName, header, and remaining member vars to default values.
     * Defaults set:
     *   rows = Empty ArrayList with a size of 1
     * @param keyboardInput The Scanner to capture keyboard input
     * @param appName A String representing the app's name
     * @param header A String representing the title of the menu
     * @throws IllegalArgumentException When keyboardInput == null || appName == null || header == null
     */
    public Menu (Scanner keyboardInput, String appName, String header) throws IllegalArgumentException {
        this(keyboardInput);
        if (appName == null) throw new IllegalArgumentException("Menu(): 'appName' is null.");
        if (header == null) throw new IllegalArgumentException("Menu(): 'header' is null.");
        this.appName = appName;
        this.header = header;
    }

    /**
     * Four-arg constructor - Sets keyboardInput, appName, header, and rows
     * @param keyboardInput The Scanner to capture keyboard input
     * @param appName A String representing the app's name
     * @param header A String representing the title of the menu
     * @param rows A String array representing each row of the menu
     * @throws IllegalArgumentException When keyboardInput == null || appName == null || header == null || rows == null
     */
    public Menu (Scanner keyboardInput, String appName, String header, String[] rows) throws IllegalArgumentException {
        this(keyboardInput, appName, header);
        if (rows == null) throw new IllegalArgumentException("Menu(): 'rows' is null.");
        this.rows = new ArrayList<String>(rows.length);
        for (String row : rows) {
            this.rows.add(row);
        }
    }

    /* [---GETTERS---] */
    /**
     * Getter for the appName
     * @return this.appName as a String
     */
    public String getAppName() {
        return this.appName;
    }

    /**
     * Getter for the header
     * @return this.header as a String
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * Getter for the rows
     * @return this.rows as a String[]
     */
    public String[] getRows() {
        return this.rows.toArray(new String[0]);
    }

    /* [---SETTERS---] */
    /**
     * Setter for the appName
     * @param appName The menu's updated appName.
     * @throws IllegalArgumentException when appName == null
     */
    public void setAppName(String appName) throws IllegalArgumentException {
        if (appName == null) {
            throw new IllegalArgumentException("setAppName(): 'appName' is null.");
        }
        this.appName = appName;
    }

    /**
     * Setter for the header
     * @param header The menu's updated header.
     * @throws IllegalArgumentException when header == null
     */
    public void setHeader(String header) throws IllegalArgumentException {
        if (header == null) {
            throw new IllegalArgumentException("setHeader(): 'header' is null.");
        }
        this.header = header;
    }

    /**
     * Setter for the rows
     * @param rows The menu's updated rows.
     * @throws IllegalArgumentException when rows == null
     */
    public void setRows(String... rows) throws IllegalArgumentException {
        if (rows == null) {
            throw new IllegalArgumentException("setRows(): 'rows' is null.");
        }
        this.rows = new ArrayList<String>(rows.length);
        for (String row : rows) {
            this.rows.add(row);
        }
    }

    /**
     * Sets a row at a provided index.
     * @param index The index of the row to modify.
     * @param row The updated row String.
     * @return The old row as a String.
     * @throws IllegalArgumentException when index < 0 || index >= this.rows.size() || row == null
     */
    public String setRow(int index, String row) throws IllegalArgumentException {
        if (index < 0 || index >= this.rows.size()) {
            throw new IllegalArgumentException("setRow(): 'index' is out of bounds.");
        }
        if (row == null) throw new IllegalArgumentException("setRow(): 'row' is null.");

        return this.rows.set(index, row);
    }

    /**
     * Sets a row based on value.
     * @param rowQuery The row to search for and update
     * @param row The updated row String.
     * @return The old row as a String.
     * @throws IllegalArgumentException When rowQuery == null || row == null || !this.rows.contains(row)
     */
    public String setRow(String rowQuery, String row) throws IllegalArgumentException {
        if (rowQuery == null) {
            throw new IllegalArgumentException("setRow(): 'rowQuery' is null.");
        }
        if (row == null) {
            throw new IllegalArgumentException("setRow(): 'row' is null.");
        }
        if (!this.rows.contains(rowQuery)) {
            throw new IllegalArgumentException("setRow(): 'rowQuery' could not be found in 'rows'.");
        }
        int index = this.rows.indexOf(rowQuery);
        return this.rows.set(index, row);
    }

    /* [---INSERTERS---] */
    /**
     * Adds a new row to the end of the menu.
     * @param row The row to add.
     * @throws IllegalArgumentException when row == null
     */
    public void addRow(String row) throws IllegalArgumentException {
        if (row == null) {
            throw new IllegalArgumentException("addRow(): 'row' is null.");
        }
        this.rows.add(row);
    }

    /**
     * Adds a new row at the specified index.
     * @param index The index at which to insert the new row.
     * @param row The row to add.
     * @throws IllegalArgumentException when index < 0 || index > this.rows.size() || row == null
     */
    public void addRow(int index, String row) throws IllegalArgumentException {
        if (index < 0 || index > this.rows.size()) {
            throw new IllegalArgumentException("addRow(): 'index' is out of bounds.");
        }
        if (row == null) {
            throw new IllegalArgumentException("addRow(): 'row' is null.");
        }
        this.rows.add(index, row);
    }

    /**
     * Adds a row directly before or after another row (selected by value).
     * @param rowQuery The row to search for and add relative to.
     * @param row The row to add.
     * @param isBefore Whether to place the new row before the relative row instead of after.
     * @throws IllegalArgumentException when rowQuery == null || row == null || !this.rows.contains(row)
     */
    public void addRow(String rowQuery, String row, boolean isBefore) throws IllegalArgumentException {
        if (rowQuery == null) {
            throw new IllegalArgumentException("addRow(): 'rowQuery' is null.");
        }
        if (row == null) {
            throw new IllegalArgumentException("addRow(): 'row' is null.");
        }
        if (!this.rows.contains(rowQuery)) {
            throw new IllegalArgumentException("addRow(): 'rowQuery' could not be found in 'rows'.");
        }

        int index = this.rows.indexOf(rowQuery);
        if (isBefore) {
            this.rows.add(index, row);
        } else {
            this.rows.add(index + 1, row);
        }
    }

    /* [---REMOVERS---] */
    /**
     * Removes a row at the specified index and returns it.
     * @param index The index at which to remove a row.
     * @return The removed row as a String
     * @throws IllegalArgumentException when index < 0 || index >= this.rows.size()
     */
    public String removeRow(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.rows.size()) {
            throw new IllegalArgumentException("removeRow(): 'index' is out of bounds.");
        }
        return this.rows.remove(index);
    }

    /**
     * Removes a row based on value.
     * @param row The row to search for and remove
     * @return The removed row as a String
     * @throws IllegalArgumentException when row == null || !this.rows.contains(row)
     */
    public String removeRow(String row) throws IllegalArgumentException {
        if (row == null) {
            throw new IllegalArgumentException("removeRow(): 'row' is null.");
        }
        if (!this.rows.remove(row)) {
            throw new IllegalArgumentException("removeRow(): 'row' could not be found in 'rows'.");
        }

        return row;
    }

    /* [---PRINTERS---] */
    /**
     * Prints the menu to System.out
     */
    public void display() {
        // Print the title and title borders
        System.out.print("=".repeat(20));
        System.out.print("| " + this.appName.toUpperCase() + " |");
        System.out.println("=".repeat(20));

        // Print the header and header borders
        System.out.print("-".repeat(20));
        System.out.print("| " + this.header.toUpperCase() + " |");
        System.out.println("-".repeat(20));

        // Print the menu options
        int index = 1;
        for (String row : rows) {
            System.out.println(index + ". " + row);
            index++;
        }
        System.out.println("-".repeat(53));
    }

    /* [---INPUT HANDLER---] */
    /**
     * Captures user input via the keyboard and validates the input;
     * Note: this method is a static utility method, but assumes at least 1 object of class Menu
     * has been instantiated and set the static 'keyboardInput' member variable.
     * @param optionCount the number of options a user can choose from
     * @throws NullPointerException when Menu.keyboardInput == null
     * @throws IllegalArgumentException when optionCount < 1
     */
    public static int getSelection(int optionCount) throws NullPointerException {
        if (Menu.keyboardInput == null) {
            throw new NullPointerException("getSelection(): Scanner 'Menu.keyboardInput' is null.");
        }
        if (optionCount < 1) {
            throw new IllegalArgumentException("getSelection(): 'optionCount' must be greater than 0.");
        }

        // Get user's menu selection
        int selection = 0;
        boolean isValidSelection = false;
        while (!isValidSelection) {
            System.out.print("Select an option: ");
            try {
                selection = keyboardInput.nextInt();
                isValidSelection = (
                    selection > 0 && selection <= optionCount);
                if (!isValidSelection) {
                    throw new IndexOutOfBoundsException("getSelection(): user selection out of bounds.");
                }
            } catch (Exception e) {
                keyboardInput.nextLine(); // Clear the input
                System.out.println("Invalid input. Please try again.");
            }
        }
        return selection;
    }
}
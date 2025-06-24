
/**
 * Menu.java
 * @author Stephen Kyker
 * CIS 22C, Final Project
 */
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu Class Handles the menu and UI systems' logic; Instantiation via the
 * zero-argument constructor is explicitly disabled, as a Menu should not be
 * empty; use designated constructors instead.
 *
 * @author Stephen Kyker
 */
public class Menu {
	private static Scanner keyboardInput;
	private String appName;
	private String header;
	private String borderPattern;
	private ArrayList<String> rows;

	/* [---CONSTRUCTORS---] */
	/**
	 * Zero-arg Constructor - If Menu.keyboardInput != null, set remaining member
	 * vars to default values; Defaults set: appName = "UNTITLED APP" header =
	 * "UNTITLED MENU" borderPattern = "" rows = Empty ArrayList with a size of 1
	 *
	 * @throws NullPointerException when keyboardInput has not been defined
	 */
	public Menu() {
		if (Menu.keyboardInput == null) {
			throw new NullPointerException("Menu(): Static member 'keyboardInput' is null.");
		}

		this.appName = "UNTITLED APP";
		this.header = "UNTITLED MENU";
		this.borderPattern = "";
		this.rows = new ArrayList<String>(1);
	}

	/**
	 * One-arg constructor - Sets keyboardInput and remaining member vars to default
	 * values Defaults set: appName = "UNTITLED APP" header = "UNTITLED MENU"
	 * borderPattern = "" rows = Empty ArrayList with a size of 1
	 * 
	 * @param keyboardInput The Scanner to capture keyboard input
	 * @throws IllegalArgumentException When keyboardInput == null
	 */
	public Menu(Scanner keyboardInput) throws IllegalArgumentException {
		// If first object, instantiate the keyboard input, otherwise no need
		if (Menu.keyboardInput == null) {
			if (keyboardInput == null) {
				throw new IllegalArgumentException("Menu(): 'keyboardInput' is null.");
			}
			Menu.keyboardInput = keyboardInput;
		}

		this.appName = "UNTITLED APP";
		this.header = "UNTITLED MENU";
		this.borderPattern = "";
		this.rows = new ArrayList<String>(1);
	}

	/**
	 * Two-arg constructor - Sets keyboardInput, rows, and remaining member vars to
	 * default values. Defaults set: appName = "UNTITLED APP" header = "UNTITLED
	 * MENU" borderPattern = ""
	 * 
	 * @param keyboardInput The Scanner to capture keyboard input
	 * @param rows          A String array representing each row of the menu
	 * @throws IllegalArgumentException When keyboardInput == null || rows == null
	 */
	public Menu(Scanner keyboardInput, String[] rows) throws IllegalArgumentException {
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
	 * Three-arg constructor - Sets keyboardInput, appName, header, and remaining
	 * member vars to default values. Defaults set: borderPattern = "" rows = Empty
	 * ArrayList with a size of 1
	 * 
	 * @param keyboardInput The Scanner to capture keyboard input
	 * @param appName       A String representing the app's name
	 * @param header        A String representing the title of the menu
	 * @throws IllegalArgumentException When keyboardInput == null || appName ==
	 *                                  null || header == null
	 */
	public Menu(Scanner keyboardInput, String appName, String header) throws IllegalArgumentException {
		this(keyboardInput);
		if (appName == null)
			throw new IllegalArgumentException("Menu(): 'appName' is null.");
		if (header == null)
			throw new IllegalArgumentException("Menu(): 'header' is null.");
		this.appName = appName;
		this.header = header;
	}

	/**
	 * Four-arg constructor - Sets keyboardInput, appName, header, and rows Defaults
	 * set: borderPattern = ""
	 * 
	 * @param keyboardInput The Scanner to capture keyboard input
	 * @param appName       A String representing the app's name
	 * @param header        A String representing the title of the menu
	 * @param rows          A String array representing each row of the menu
	 * @throws IllegalArgumentException When keyboardInput == null || appName ==
	 *                                  null || header == null || rows == null
	 */
	public Menu(Scanner keyboardInput, String appName, String header, String[] rows) throws IllegalArgumentException {
		this(keyboardInput, appName, header);
		if (rows == null)
			throw new IllegalArgumentException("Menu(): 'rows' is null.");
		this.rows = new ArrayList<String>(rows.length);
		for (String row : rows) {
			this.rows.add(row);
		}
	}

	/* [---GETTERS---] */
	/**
	 * Getter for the appName
	 * 
	 * @return this.appName as a String
	 */
	public String getAppName() {
		return this.appName;
	}

	/**
	 * Getter for the header
	 * 
	 * @return this.header as a String
	 */
	public String getHeader() {
		return this.header;
	}

	/**
	 * Getter for the border pattern
	 * 
	 * @return this.borderPattern as a String
	 */
	public String getBorderPattern() {
		return this.borderPattern;
	}

	/**
	 * Getter for the rows
	 * 
	 * @return this.rows as a String[]
	 */
	public String[] getRows() {
		return this.rows.toArray(new String[0]);
	}

	/* [---SETTERS---] */
	/**
	 * Setter for the appName
	 * 
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
	 * 
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
	 * Setter for the border pattern
	 * 
	 * @param borderPattern The menu's updated borderPattern.
	 * @throws IllegalArgumentException when borderPattern == null
	 */
	public void setBorderPattern(String borderPattern) throws IllegalArgumentException {
		if (borderPattern == null) {
			throw new IllegalArgumentException("setBorderPattern(): 'borderPattern' is null.");
		}
		this.borderPattern = borderPattern;
	}

	/**
	 * Setter for the rows
	 * 
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
	 * 
	 * @param index The index of the row to modify.
	 * @param row   The updated row String.
	 * @return The old row as a String.
	 * @throws IllegalArgumentException when index < 0 || index >= this.rows.size()
	 *                                  || row == null
	 */
	public String setRow(int index, String row) throws IllegalArgumentException {
		if (index < 0 || index >= this.rows.size()) {
			throw new IllegalArgumentException("setRow(): 'index' is out of bounds.");
		}
		if (row == null)
			throw new IllegalArgumentException("setRow(): 'row' is null.");

		return this.rows.set(index, row);
	}

	/**
	 * Sets a row based on value.
	 * 
	 * @param rowQuery The row to search for and update
	 * @param row      The updated row String.
	 * @return The old row as a String.
	 * @throws IllegalArgumentException When rowQuery == null || row == null ||
	 *                                  !this.rows.contains(row)
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
	 * 
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
	 * 
	 * @param index The index at which to insert the new row.
	 * @param row   The row to add.
	 * @throws IllegalArgumentException when index < 0 || index > this.rows.size()
	 *                                  || row == null
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
	 * 
	 * @param rowQuery The row to search for and add relative to.
	 * @param row      The row to add.
	 * @param isBefore Whether to place the new row before the relative row instead
	 *                 of after.
	 * @throws IllegalArgumentException when rowQuery == null || row == null ||
	 *                                  !this.rows.contains(row)
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
	 * 
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
	 * 
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
	 * Prints the menu to System.out Note: attempts to format rows to a 72 char max,
	 * but does allow overflowing the max if appName or header exceed the limit
	 */
	public void display() {
		// Assess the width of appName and header
		// to calculate padding; using 72 char
		// for the max width of a line as it's
		// a well known terminal standard.
		final int MAX_WIDTH = 72;
		final int LEFT_INDENT = 21;
		int appNameRowRemainder = MAX_WIDTH - this.appName.length();
		int headerRowRemainder = MAX_WIDTH - this.header.length();

		// Padding amount is the required width to either side of a variable
		int appNamePadding = (appNameRowRemainder / 2);
		int headerPadding = (headerRowRemainder / 2);

		// Catch overflow case, but allow it and disable padding;
		if (appNamePadding < 0) {
			appNamePadding = 0;
		}
		if (headerPadding < 0) {
			headerPadding = 0;
		}

		// Concat the appName row
		String appNameRow = "";
		appNameRow += this.borderPattern + "- ".repeat((appNamePadding - this.borderPattern.length()) / 2);
		appNameRow += this.appName.toUpperCase();
		appNameRow += " -".repeat((appNamePadding - this.borderPattern.length()) / 2);
		// Add a space to the right side's padding if the remainder was odd
		if (appNameRowRemainder % 2 != 0) {
			appNameRow += " ";
		}
		appNameRow += this.borderPattern;

		// Concat the header row
		String headerRow = "";
		headerRow += this.borderPattern + " ".repeat(LEFT_INDENT - this.borderPattern.length());
		headerRow += ("[ " + this.header.toUpperCase() + " ]");

		// Calculate end padding for header row
		int repeatCount = headerRowRemainder - LEFT_INDENT - ("[  ]".length()) - (this.borderPattern.length());
		headerRow += " ".repeat(repeatCount) + this.borderPattern;

		// Print the appName and header rows with the border,
		// if the border char count is < 1, skip the border
		String borderRow = "";
		if (this.borderPattern.length() > 0) {
			borderRow += this.borderPattern.repeat(MAX_WIDTH / borderPattern.length()) + "\n";
		}
		System.out.println(borderRow + appNameRow);
		System.out.println(borderRow + headerRow);

		// Print the menu options
		int index = 0;
		for (String row : rows) {
			row = (this.borderPattern + " ".repeat(LEFT_INDENT - this.borderPattern.length()) + ++index + ". " + row);
			repeatCount = MAX_WIDTH - row.length() - this.borderPattern.length();
			if (repeatCount < 0) {
				repeatCount = 0;
			}
			System.out.println(row + " ".repeat(repeatCount) + this.borderPattern);
		}

		// Print the bottom border
		String bottomBorder = this.borderPattern + "-".repeat(MAX_WIDTH - (this.borderPattern.length() * 2))
				+ this.borderPattern;

		System.out.println(bottomBorder);
	}

	/* [---INPUT HANDLER---] */
	/**
	 * Captures user input via the keyboard and validates the input; Note: this
	 * method is a static utility method, but assumes at least 1 object of class
	 * Menu has been instantiated and set the static 'keyboardInput' member
	 * variable.
	 * 
	 * @param optionCount the number of options a user can choose from
	 * @throws NullPointerException     when Menu.keyboardInput == null
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
			System.out.print("   Select an option: ");
			try {
				selection = keyboardInput.nextInt();
				keyboardInput.nextLine();
				isValidSelection = (selection > 0 && selection <= optionCount);
				if (!isValidSelection) {
					throw new IndexOutOfBoundsException("getSelection(): user selection out of bounds.");
				}
			} catch (Exception e) {
				keyboardInput.nextLine(); // Clear the input
				System.out.println("Invalid input. Please try again.");
			}
		}
		System.out.println(); // Line break
		return selection;
	}
}
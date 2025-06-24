
/**
 * FileHandler.java
 * @author Stephen Kyker
 * CIS 22C, Group Project
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Handles logic related to file management and file I/O operations. Implements
 * AutoCloseable to support use within try-with-resources.
 */
public class FileHandler implements AutoCloseable {
	/**
	 * Default Constructor
	 * 
	 * @throws Exception if a general exception occurs
	 */
	public FileHandler() throws Exception {
	}

	/**
	 * Loads all matching data files into memory from a directory; files are matched
	 * by prefix and suffix, where any text in between the two is treated like a
	 * wildcard: E.g., if prefix = "song" and suffix = ".txt", then song*.txt would
	 * match, where * is any character sequence
	 *
	 * @param dirPath    the relative path to the data, e.g., "./data/"
	 * @param filePrefix the prefix of the file name, e.g., "song"
	 * @param fileSuffix the suffix of the file name, e.g., ".txt"
	 * @throws FileNotFoundException if a file cannot be accessed
	 */
	public String readDir(String dirPath, String filePrefix, String fileSuffix) throws FileNotFoundException {

		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new FileNotFoundException();
		}

		StringBuilder rawData = new StringBuilder();
		File[] files = dir.listFiles();

		// Null or empty check
		if (files == null || files.length == 0) {
			throw new FileNotFoundException(
					String.format(
							"loadData(): No files found in " + dirPath + " or "
									+ "the files are inaccessible for reading." + "  Failed Pattern Match: %s*%s%n",
							filePrefix, fileSuffix));
		}

		// Sort the files by name, and if applicable,
		// additionally by number
		this.sortFilesByName(files);

		// Read all files in the directory that match the pattern
		// and prepend the file count at the very end
		int count = 0;
		for (File file : files) {
			String fileName = file.getName();
			String header = "[START OF '" + fileName + "']\n";
			String footer = "[END OF '" + fileName + "']\n";
			if (file.isFile() && fileName.startsWith(filePrefix) && fileName.endsWith(fileSuffix)) {
				rawData.append(header).append(readFile(file)).append(footer);
				count++;
			}
		}
		rawData.insert(0, String.format("File count: %d%n", count));

		return rawData.toString();
	}

	/**
	 * Reads all the data in a file
	 *
	 * @param file the file to read
	 * @return a String of all the data in the file
	 * @throws FileNotFoundException if a file cannot be accessed
	 */
	public String readFile(File file) throws FileNotFoundException {

		// Scan all the text from a file
		StringBuilder rawData = new StringBuilder();
		try (Scanner fileScan = new Scanner(file)) {
			while (fileScan.hasNextLine()) {
				rawData.append(fileScan.nextLine()).append("\n");
			}
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException(String.format("readFile(): " + file + " cannot be found or is "
					+ "inaccessible for reading.%n" + "  Exception: %s%n", fnfe.getMessage()));
		}

		return rawData.toString();
	}

	/**
	 * Insertion sorts a list of files by name; Sorts in natural ascending order by
	 * file name
	 *
	 * @throws NullPointerException if file list is null or empty
	 */
	public void sortFilesByName(File[] files) throws NullPointerException {

		if (files == null || files.length == 0) {
			throw new NullPointerException("sortFilesByName(): The files list is null or empty.");
		}

		// Simple insertion sort
		// (not worrying about maximum efficiency here as we only have
		// a handful of files to process for this project)
		for (int i = 1; i < files.length; i++) {
			File curFile = files[i];
			int j = i;

			while (j > 0 && compareByName(files[j - 1], curFile) > 0) {
				files[j] = files[j - 1];
				j--;
			}

			files[j] = curFile;
		}
	}

	/**
	 * Compares two files by name length, and then lexicographically by file name if
	 * needed (e.g., song2.txt should come before song10.txt); Note that nulls are
	 * moved to the end of a list if calling this to perform a sort, additionally
	 * this method also assumes that the files will only differ at the numeric
	 * identifier between the file prefix and suffix.
	 *
	 * @param file1 the first file to compare
	 * @param file2 the second file to compare
	 * @return 0 if equal, 1 if file1 > file2, and -1 if file1 < file2
	 */
	public int compareByName(File file1, File file2) {
		// Null check
		if (file1 == null && file2 == null) {
			return 0;
		} else if (file1 == null) {
			return 1;
		} else if (file2 == null) {
			return -1;
		}

		// Get the file names and file name lengths
		String name1 = file1.getName();
		String name2 = file2.getName();
		int length1 = name1.length();
		int length2 = name2.length();

		// Compare length first;
		// E.g., song10.txt would incorrectly be calculated
		// as less than song2.txt in a simple lexicographical sort
		if (length1 != length2) {
			return Integer.compare(length1, length2);
		}

		// Compare lexicographically,
		// if they are the same length
		return name1.compareTo(name2);
	}

	/**
	 * AutoCloseable implement for try-with-resources
	 *
	 * @throws Exception if any exception occurs during clean up
	 */
	@Override
	public void close() throws Exception {
		// May eventually add class level streams
		// that will need to be cleaned up here
	}
}

/**
 * This class is responsible for driving the program
 */
public class Driver {
    /**
     * Whether the program should be compiled in production mode
     */
    private static final boolean IS_PROD = false;

    /**
     * The minimum required JRE version supported by this program;
     * Java 11 or later is strongly recommended
     */
    private static final String REQUIRED_VERSION = "11";

    /**
     * Entry point method for driving the program
     * @param args the entry point arguments, if any
     */
    public static void main(String[] args) {
        // Check JRE version
        try {
            checkVersion();
            if (!Driver.IS_PROD) { // Log success if not in production
                System.out.println("JRE valid. Launching program.");
            }
        } catch (NullPointerException |
                 NumberFormatException |
                 IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // Version validated and program can begin...

    }

    /**
     * Checks whether the user's Java Runtime Environment is a version
     * supported by this program. This method is compatible with legacy
     * environments dating back to Java 1.1 (1997). Method implemented
     * by Stephen Kyker. Contact for questions.
     *
     * @throws NullPointerException  if either parameter is null
     * @throws NumberFormatException if either version failed to parse
     * @throws IllegalStateException if version requirement not met
     */
    private static void checkVersion()
        throws NullPointerException,
               NumberFormatException,
               IllegalStateException {

        // Localize
        String reqVersion = Driver.REQUIRED_VERSION;
        String javaVersion = System.getProperty("java.version");

        // Null check
        if (reqVersion == null || javaVersion == null) {
            throw new NullPointerException(String.format(
                "checkVersion(): Strings must be non-null.%n" +
                "  reqVersion:  %s%n" +
                "  javaVersion: %s%n",
                reqVersion, javaVersion));
        }

        // Strip the user's java.version to find the runtime version
        String runtimeVersion;
        if (javaVersion.startsWith("1.")) {
            // Legacy format (prior to Java 9):
            // e.g., "1.8.0_381" -> "8"
            int firstDelim = javaVersion.indexOf('.');
            int secondDelim = javaVersion
                .indexOf('.', firstDelim + 1);
            runtimeVersion = javaVersion
                .substring(firstDelim + 1, secondDelim);
        } else {
            // Modern format (Java 9 and after):
            int delim = javaVersion.indexOf('.');
            if (delim == -1) {
                // e.g., "9" -> "9"
                runtimeVersion = javaVersion;
            } else {
                // e.g., "11.0.2" -> "11"
                runtimeVersion = javaVersion
                    .substring(0, delim);
            }
        }

        // Attempt to parse and compare to the required version
        try {
            if (Integer.parseInt(runtimeVersion) <
                Integer.parseInt(reqVersion)) {
                throw new IllegalStateException(String.format(
                    "checkVersion(): Unsupported Java version.%n" +
                    "  Minimum supported runtime version: %s%n" +
                    "  Current runtime version:           %s%n",
                    reqVersion, runtimeVersion));
            }
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(String.format(
                "checkVersion(): Version parse and compare failed " +
                "due to an invalid format.%n" +
                "  reqVersion:     %s%n" +
                "  runtimeVersion: %s%n",
                reqVersion, runtimeVersion));
        }
    }
}


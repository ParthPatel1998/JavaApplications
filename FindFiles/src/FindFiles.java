import java.util.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindFiles {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("ERROR: filename not provided.");
            printHelp();
            System.exit(0);
        } else {
            if (Arrays.asList(args).contains("-help")) {
                printHelp();
                System.exit(0);
            }
            try {
                java.util.HashMap<String, String> options = parse(args);
                String fileName = args[0];
                String dirPath = "";
                Set<String> fileNames = new HashSet<String>();


                if (options.containsKey("dir")) {
                    dirPath = options.get("dir");
                    if (dirPath.charAt(dirPath.length() - 1) != '/'){
                        dirPath += "/";
                    }
                }
                if (options.containsKey("ext")) {
                    String ext = options.get("ext");
                    String[] extensions = ext.split(",");
                    String extensionConnect = ".";
                    if (options.containsKey("reg")) extensionConnect = "\\.";
                    for (String e : extensions) {
                        fileNames.add(fileName + extensionConnect + e);
                    }
                } else {
                    fileNames.add(fileName);
                }

                if (options.containsKey("r")) {
                    if (!options.containsKey("dir")) {
                        File currDir = new File(".");
                        dirPath = currDir.getCanonicalPath();
                        //dirPath = System.getProperty("user.dir");
                    }
                    recurseSubFolders(dirPath, fileNames, options.containsKey("reg"));
                } else {
                    File currDir = new File(".");
                    if (options.containsKey("dir")) {
                        currDir = new File(dirPath);
                    }
                    File[] foldersList = currDir.listFiles();
                    if (foldersList == null) System.exit(0);
                    for (File file : foldersList) {
                        boolean matches = false;
                        if (options.containsKey("reg")) {
                            for (String f : fileNames) {
                                Pattern p = Pattern.compile(f);
                                Matcher m = p.matcher(file.getName());
                                boolean b = m.matches();
                                if (b) matches = true;
                            }
                            if (matches && file.exists() && !file.isDirectory() && file.isFile()) {
                                System.out.println("Found File: " + file.getCanonicalPath());
                            }
                        } else {
                            if (fileNames.contains(file.getName()) && file.exists() && !file.isDirectory() && file.isFile()) {
                                System.out.println("Found File: " + file.getCanonicalPath());
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error finding file: " + ex.toString());
                printHelp();
            }
        }
        //System.out.println("");
    }

    // Print the help page for the command: java FindFiles -help
    static void printHelp() {
        //System.out.println("Usage: java CommandLine [-arg val]");
        System.out.println("Usage: java FindFiles filetofind [-option arg]");
        System.out.println("-help                     :: print out a help page and exit the program.");
        System.out.println("-r                        :: execute the command recursively in subfiles.");
        System.out.println("-reg                      :: treat `filetofind` as a regular expression when searching.");
        System.out.println("-dir [directory]          :: find files starting in the specified directory.");
        System.out.println("-ext [ext1,ext2,...]      :: find files matching [filetofind] with extensions [ext1, ext2,...].");

    }

    static void recurseSubFolders(String dirPath, Set<String> fileNames, boolean reg) {
        try {
            File dir = new File(dirPath);
            File[] foldersList = dir.listFiles();
            String fileNamesList = String.join(", ", fileNames);
            System.out.println("looking for " + fileNamesList + " in: " + dir.getCanonicalPath());
            if (foldersList == null) System.exit(0);
            for (File file : foldersList) {
                if (reg) {
                    boolean matches = false;
                    for (String f : fileNames) {
                        Pattern p = Pattern.compile(f);
                        Matcher m = p.matcher(file.getName());
                        boolean b = m.matches();
                        if (b) matches = true;
                    }
                    if (matches && file.exists() && !file.isDirectory() && file.isFile()) {
                        System.out.println("---- Found File: " + file.getCanonicalPath());
                    }
                }
                if (fileNames.contains(file.getName()) && file.exists() && !file.isDirectory() && file.isFile()) {
                    System.out.println("---- Found File: " + file.getCanonicalPath());
                }
            }
            for (File file : foldersList) {
                if (file.isDirectory()) {
                    recurseSubFolders(file.getCanonicalPath(), fileNames, reg);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error finding file: " + ex.toString());
            printHelp();
        }
    }

    // Build a dictionary of key:value pairs (without the leading "-")
    static HashMap<String, String> parse(String[] args) {
        HashMap<String, String> arguments = new HashMap<>();
        String lastOption = "";
        String key = null;
        String value = null;
        boolean expectParameter = false;

        if (args[0].startsWith("-")) {
            System.out.println("ERROR: no filename to find provided");
            printHelp();
            System.exit(0);
        }
        for(int i = 1; i < args.length; i++) {
            String entry = args[i];
            if (entry.startsWith("-")) {
                key = entry.substring(1);
                if (expectParameter) {
                    System.out.println("ERROR: Invalid option, " +  lastOption + " requires arguments to be specified.");
                    printHelp();
                    System.exit(0);
                }
                if (entry.equals("-r") || entry.equals("-reg") || entry.equals("-help")) {
                    expectParameter = false;
                    arguments.put(key, "");
                } else if (entry.equals("-dir") || entry.equals("-ext")) {
                    expectParameter = true;
                } else {
                    System.out.println(entry + " is an invalid option. Please supply valid options from the " +
                            "following list. -r, -reg, -help, -dir, -ext.");
                    printHelp();
                    System.exit(0);
                }
                lastOption = key;
            } else {
                if (!expectParameter) {
                    System.out.println("ERROR: " + lastOption + " does not require arguments to be specified.");
                    printHelp();
                    System.exit(0);
                }
                expectParameter = false;
                value = entry;
                arguments.put(key, value);
            }
        }
        if (expectParameter) {
            System.out.println("ERROR: Invalid option, " + lastOption + " requires arguments to be specified.");
            printHelp();
            System.exit(0);
        }
        // return dictionary
        return arguments;
    }
}

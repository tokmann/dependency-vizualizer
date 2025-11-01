package depviz;

public class CLIParser {
    public static Config parseArgs(String[] args) {
        String pkg = null, url = null, version = null, filter = "";
        boolean test = false;
        int depth = 3;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--package" -> pkg = args[++i];
                case "--repo" -> url = args[++i];
                case "--version" -> version = args[++i];
                case "--depth" -> depth = Integer.parseInt(args[++i]);
                case "--filter" -> filter = args[++i];
                case "--test" -> test = true;
                default -> throw new IllegalArgumentException("Unknown option: " + args[i]);
            }
        }

        if (pkg == null) throw new IllegalArgumentException("Missing --package");
        if (url == null) throw new IllegalArgumentException("Missing --repo");
        if (version == null) throw new IllegalArgumentException("Missing --version");
        if (depth < 1) throw new IllegalArgumentException("Depth must be positive");

        return new Config(pkg, url, test, version, depth, filter);
    }
}

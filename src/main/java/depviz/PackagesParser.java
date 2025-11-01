package depviz;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class PackagesParser {
    private final Config config;

    public PackagesParser(Config config) {
        this.config = config;
    }

    public DependencyGraph parseTestRepository() throws IOException {
        Map<String, List<String>> deps = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of(config.getRepoPathOrUrl()));
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split(":");
            String pkg = parts[0].trim();
            List<String> depends = new ArrayList<>();
            if (parts.length > 1 && !parts[1].isBlank())
                depends = Arrays.asList(parts[1].split(","));
            deps.put(pkg, depends);
        }
        return new DependencyGraph(deps);
    }

    public DependencyGraph parseRealRepository() throws IOException {
        InputStream inputStream;
        if (config.getRepoPathOrUrl().endsWith(".gz")) {
            inputStream = new GZIPInputStream(new URL(config.getRepoPathOrUrl()).openStream());
        } else {
            inputStream = new URL(config.getRepoPathOrUrl()).openStream();
        }

        Map<String, List<String>> deps = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line, pkg = null, version = null;
        List<String> depends = null;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("Package: ")) {
                pkg = line.substring(9).trim();
            } else if (line.startsWith("Version: ")) {
                version = line.substring(9).trim();
            } else if (line.startsWith("Depends: ")) {
                String[] arr = line.substring(9).split(",");
                depends = new ArrayList<>();
                for (String s : arr) {
                    s = s.replaceAll("\\(.*?\\)", "").trim();
                    if (!s.isEmpty()) depends.add(s);
                }
            } else if (line.isEmpty() && pkg != null && version != null) {
                if (version.equals(config.getVersion())) {
                    deps.put(pkg, depends == null ? List.of() : depends);
                }
                pkg = null;
                version = null;
                depends = null;
            }
        }

        return new DependencyGraph(deps);
    }
}

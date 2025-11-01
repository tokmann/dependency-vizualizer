package depviz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MermaidGenerator {
    private final DependencyGraph graph;

    public MermaidGenerator(DependencyGraph graph) {
        this.graph = graph;
    }

    public String generateMermaid() {
        StringBuilder sb = new StringBuilder();
        sb.append("graph TD\n");
        for (String pkg : graph.getAllPackages()) {
            List<String> deps = graph.getDependencies(pkg);
            for (String dep : deps) {
                sb.append("    ").append(pkg).append(" --> ").append(dep).append("\n");
            }
            if (deps.isEmpty()) {
                sb.append("    ").append(pkg).append("\n");
            }
        }
        return sb.toString();
    }

    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(generateMermaid());
        }
    }

}

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

        Map<String, String> nodeIds = new HashMap<>();
        int counter = 0;

        for (String pkg : graph.getAllPackages()) {
            String id = pkg.replaceAll("[^a-zA-Z0-9_]", "_");
            if (nodeIds.containsValue(id)) {
                id += "_" + counter++;
            }
            nodeIds.put(pkg, id);
        }

        for (String pkg : graph.getAllPackages()) {
            String fromId = nodeIds.get(pkg);
            List<String> deps = graph.getDependencies(pkg);
            if (deps.isEmpty()) {
                sb.append("    ").append(fromId).append("[\"").append(pkg).append("\"]\n");
            } else {
                for (String dep : deps) {
                    String toId = nodeIds.get(dep);
                    if (toId == null) {
                        // если зависимость не найдена в graph, создаем id на лету
                        toId = dep.replaceAll("[^a-zA-Z0-9_]", "_") + "_" + counter++;
                        nodeIds.put(dep, toId);
                    }
                    sb.append("    ").append(fromId).append(" --> ").append(toId)
                            .append("[\"").append(dep).append("\"]\n");
                }
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

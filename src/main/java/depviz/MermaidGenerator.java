package depviz;

import java.util.*;

public class MermaidGenerator {
    private final DependencyGraph graph;

    public MermaidGenerator(DependencyGraph graph) {
        this.graph = graph;
    }

    public String generateMermaid(String root) {
        StringBuilder sb = new StringBuilder();
        sb.append("graph TD\n");
        for (String pkg : graph.getAllPackages()) {
            List<String> deps = graph.getDependencies(pkg);
            for (String dep : deps) {
                sb.append("    ").append(pkg).append(" --> ").append(dep).append("\n");
            }
        }
        return sb.toString();
    }
}

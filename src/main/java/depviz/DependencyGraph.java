package depviz;

import java.util.*;

public class DependencyGraph {
    private final Map<String, List<String>> dependencies;

    public DependencyGraph(Map<String, List<String>> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getDependencies(String pkg) {
        return dependencies.getOrDefault(pkg, List.of());
    }

    public Set<String> getAllPackages() {
        return dependencies.keySet();
    }

    public void addDependency(String from, String to) {
        dependencies.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public void print() {
        dependencies.forEach((k, v) ->
                System.out.println(k + " -> " + (v.isEmpty() ? "(none)" : String.join(", ", v))));
    }
}

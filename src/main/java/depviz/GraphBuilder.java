package depviz;

import java.util.*;

public class GraphBuilder {
    private final DependencyGraph graph;
    private final Config config;

    public GraphBuilder(DependencyGraph graph, Config config) {
        this.graph = graph;
        this.config = config;
    }

    public DependencyGraph buildDependencyGraph() {
        Map<String, List<String>> result = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        Deque<Integer> depth = new ArrayDeque<>();

        stack.push(config.getPackageName());
        depth.push(0);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            int d = depth.pop();

            if (d > config.getMaxDepth()) continue;
            if (visited.contains(current)) continue;
            visited.add(current);

            List<String> deps = graph.getDependencies(current);
            if (deps == null) deps = List.of();

            List<String> filtered = new ArrayList<>();
            for (String dep : deps) {
                if (!dep.contains(config.getFilterSubstring())) {
                    filtered.add(dep);
                    stack.push(dep);
                    depth.push(d + 1);
                }
            }
            result.put(current, filtered);
        }

        return new DependencyGraph(result);
    }
}

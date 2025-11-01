package depviz;

import java.util.*;

public class DependencyResolver {
    private final DependencyGraph graph;

    public DependencyResolver(DependencyGraph graph) {
        this.graph = graph;
    }

    public List<String> resolveLoadOrder(String root) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            String pkg = stack.peek();

            if (!visited.contains(pkg)) {
                visited.add(pkg);
                inStack.add(pkg);
                List<String> deps = graph.getDependencies(pkg);
                if (deps != null) {
                    for (String dep : deps) {
                        if (inStack.contains(dep)) {
                            System.err.println("Cycle detected: " + pkg + " -> " + dep);
                            continue;
                        }
                        if (!visited.contains(dep)) stack.push(dep);
                    }
                }
            } else {
                stack.pop();
                inStack.remove(pkg);
                if (!result.contains(pkg)) result.add(pkg);
            }
        }

        return result;
    }
}

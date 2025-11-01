package depviz;

public class Main {
    public static void main(String[] args) {
        try {
            Config config = CLIParser.parseArgs(args);
            System.out.println("=== CONFIGURATION ===");
            System.out.println(config);

            PackagesParser parser = new PackagesParser(config);
            DependencyGraph graph;

            if (config.isTestMode()) {
                graph = parser.parseTestRepository();
            } else {
                graph = parser.parseRealRepository();
            }

            System.out.println("\n=== DIRECT DEPENDENCIES ===");
            var deps = graph.getDependencies(config.getPackageName());
            if (deps == null || deps.isEmpty()) {
                System.out.println("(none)");
            } else {
                deps.forEach(System.out::println);
            }

            GraphBuilder builder = new GraphBuilder(graph, config);
            DependencyGraph fullGraph = builder.buildDependencyGraph();

            System.out.println("\n=== FULL DEPENDENCY GRAPH ===");
            fullGraph.print();

            DependencyResolver resolver = new DependencyResolver(fullGraph);
            var order = resolver.resolveLoadOrder(config.getPackageName());

            System.out.println("\n=== LOAD ORDER ===");
            order.forEach(System.out::println);

            MermaidGenerator generator = new MermaidGenerator(fullGraph);
            System.out.println("\n=== MERMAID GRAPH ===");
            System.out.println(generator.generateMermaid(config.getPackageName()));

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}

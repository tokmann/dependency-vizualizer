package depviz;

public class Main {
    public static void main(String[] args) {
        try {
            String saveMermaid = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--saveMermaid") && i + 1 < args.length) {
                    saveMermaid = args[i + 1];
                }
            }

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

            String mermaidFile = config.getSaveMermaid() != null ? config.getSaveMermaid() : "graph.mmd";
            MermaidGenerator mg = new MermaidGenerator(fullGraph);
            mg.saveToFile(mermaidFile);
            System.out.println("\nMermaid diagram saved to " + mermaidFile);


        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

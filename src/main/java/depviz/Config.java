package depviz;

public class Config {
    private final String packageName;
    private final String repoPathOrUrl;
    private final boolean testMode;
    private final String version;
    private final int maxDepth;
    private final String filterSubstring;
    private String saveMermaid;

    public Config(String packageName, String repoPathOrUrl, boolean testMode,
                  String version, int maxDepth, String filterSubstring) {
        this.packageName = packageName;
        this.repoPathOrUrl = repoPathOrUrl;
        this.testMode = testMode;
        this.version = version;
        this.maxDepth = maxDepth;
        this.filterSubstring = filterSubstring;
    }

    public String getPackageName() { return packageName; }
    public String getRepoPathOrUrl() { return repoPathOrUrl; }
    public boolean isTestMode() { return testMode; }
    public String getVersion() { return version; }
    public int getMaxDepth() { return maxDepth; }
    public String getFilterSubstring() { return filterSubstring; }
    public String getSaveMermaid() { return saveMermaid; }
    public void setSaveMermaid(String saveMermaid) { this.saveMermaid = saveMermaid; }

    @Override
    public String toString() {
        return String.format("""
                packageName = %s
                repoPathOrUrl = %s
                testMode = %s
                version = %s
                maxDepth = %d
                filterSubstring = %s
                """,
                packageName, repoPathOrUrl, testMode, version, maxDepth, filterSubstring);
    }
}

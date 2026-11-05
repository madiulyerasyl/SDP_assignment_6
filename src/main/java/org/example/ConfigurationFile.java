
package org.example;

public class ConfigurationFile implements ProjectElement {

    private final String name;
    private final boolean hasExposedCredentials;
    private final boolean debugModeEnabled;
    private final boolean secureConnectionEnabled;

    public ConfigurationFile(String name,
                             boolean hasExposedCredentials,
                             boolean debugModeEnabled,
                             boolean secureConnectionEnabled) {
        this.name = name;
        this.hasExposedCredentials = hasExposedCredentials;
        this.debugModeEnabled = debugModeEnabled;
        this.secureConnectionEnabled = secureConnectionEnabled;
    }

    public String getName() {
        return name;
    }

    public boolean hasExposedCredentials() {
        return hasExposedCredentials;
    }

    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    public boolean isSecureConnectionEnabled() {
        return secureConnectionEnabled;
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);
    }
}


package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Directory implements ProjectElement {

    private final String name;
    private final List<ProjectElement> elements;

    public Directory(String name) {
        this.name = name;
        this.elements = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addElement(ProjectElement element) {
        if (element == null) {
            throw new IllegalArgumentException(
                    "Element cannot be null"
            );
        }

        if (element == this) {
            throw new IllegalArgumentException(
                    "Directory cannot contain itself"
            );
        }

        elements.add(element);
    }

    public List<ProjectElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        visitor.visit(this);

        for (ProjectElement element : elements) {
            element.accept(visitor);
        }
    }
}

package citadel.classes;

import java.util.Set;

public record ClassDefinition(String clasName, Set<String> dependencies) {
}

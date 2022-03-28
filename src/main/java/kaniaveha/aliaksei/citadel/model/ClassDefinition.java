package kaniaveha.aliaksei.citadel.model;

import java.util.Set;

public record ClassDefinition(String clasName, Set<String> dependencies) {
}

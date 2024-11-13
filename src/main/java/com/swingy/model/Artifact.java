package com.swingy.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // Use a name-based identifier for the type
        include = JsonTypeInfo.As.PROPERTY, // Include the type information as a property in JSON
        property = "type" // The property name that indicates the type
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Helm.class, name = "helm"),
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon"),
        @JsonSubTypes.Type(value = Armor.class, name = "armor")
})

abstract public class Artifact {
    public String toString() {
        return "Artifact";
    }

    public Artifact() {
    }
}

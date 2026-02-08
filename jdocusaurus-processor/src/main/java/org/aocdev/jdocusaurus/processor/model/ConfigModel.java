package org.aocdev.jdocusaurus.processor.model;

public class ConfigModel {
    private String key;
    private String description;
    private String defaultValue;
    private boolean required;
    private boolean secret;
    private String example;
    private String declaredInClass;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isSecret() { return secret; }
    public void setSecret(boolean secret) { this.secret = secret; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public String getDeclaredInClass() { return declaredInClass; }
    public void setDeclaredInClass(String declaredInClass) { this.declaredInClass = declaredInClass; }
}

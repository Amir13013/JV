package com.eremon.models.item;

public abstract class Item {
    protected int id;
    protected String name;
    protected String description;
    protected boolean isStackable;

    public Item(int id, String name, String description, boolean isStackable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isStackable = isStackable;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isStackable() { return isStackable; }

    public abstract void use();

    @Override
    public String toString() {
        return "[" + name + "] " + description;
    }
}

package com.eremon.models.item;

public class Documents extends Item {
    private String content;

    public Documents(int id, String name, String content) {
        super(id, name, content, false);
        this.content = content;
    }

    public String getContent() { return content; }

    @Override
    public void use() {
        System.out.println("Grimoire " + name + " :\n" + content);
    }
}

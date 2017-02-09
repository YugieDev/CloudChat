package com.yugiedev.CloudChat;

public class Placeholder
{
    private String placeholder;
    private String variable;

    public Placeholder(String placeholder)
    {
        this.placeholder = placeholder;
    }

    public Placeholder(String placeholder, String variable)
    {
        this.placeholder = placeholder;
        this.variable = variable;
    }

    public void setVariable(String variable)
    {
        this.variable = variable;
    }

    public String getPlaceholder()
    {
        return this.placeholder;
    }

    public String getVariable()
    {
        return this.variable;
    }
}
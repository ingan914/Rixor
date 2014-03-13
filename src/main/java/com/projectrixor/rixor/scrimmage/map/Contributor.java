package com.projectrixor.rixor.scrimmage.map;

public class Contributor
{
    protected final String name;
    protected final String contribution;

    public Contributor(String name)
    {
        this.name = name;
        this.contribution = null;
    }

    public Contributor(String name, String contribution)
    {
        this.name = name;
        this.contribution = contribution;
    }

    public String toString()
    {
        return getName();
    }

    public String getName()
    {
        return this.name;
    }

    public boolean hasContribution()
    {
        return this.contribution != null;
    }

    public String getContribution()
    {
        return this.contribution;
    }
}

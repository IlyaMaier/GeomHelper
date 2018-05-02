package com.example.geomhelper.Content;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Leader extends GenericJson {
    @Key("_id")
    String _id;

    @Key("_acl")
    String _acl;

    @Key("_kmd")
    String _kmd;

    @Key("name")
    String name;

    @Key("experience")
    int experience;

    public Leader(String name, int experience) {
        this.name = name;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }
}

package com.jojo.googlenewsreader.pojo;

import java.io.Serializable;

public class Tag implements Serializable {

    private long id;
    private String label;

    public Tag(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Tag(String label) {
        this.label = label;
    }

    public Tag() {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        return !(label != null ? !label.equals(tag.label) : tag.label != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}

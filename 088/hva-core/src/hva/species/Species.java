package hva.species;

import java.io.Serial;
import java.io.Serializable;

public class Species implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _id;
    private String _name;

    public Species(String id, String name) {
        this._id = id;
        this._name = name;
    }

    public String getName() { return _name; }
}
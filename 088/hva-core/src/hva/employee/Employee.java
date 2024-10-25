package hva.employee;

import java.io.Serial;
import java.io.Serializable;

public abstract class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    protected String _id;
    protected String _name;

    public Employee(String id, String name) {
        this._id = id;
        this._name = name;
    }

    public String getId() { return _id;}
}
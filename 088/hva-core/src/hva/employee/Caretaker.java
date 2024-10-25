package hva.employee;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Caretaker extends Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private List<String> _habitatIDs;

    public Caretaker(String id, String name, List<String> habitatIDs) {
        super(id, name);
        this._habitatIDs = habitatIDs;
    }
    
    public List<String> getHabitats() { return _habitatIDs; }
    public void addHabitat(String habitatId) { _habitatIDs.add(habitatId); }
    public void removeHabitat(String habitatId) { _habitatIDs.remove(habitatId); }

    @Override
    public String toString() {
        if (!this._habitatIDs.isEmpty()) {
            return "TRT|"+super._id+"|"+super._name+"|"+String.join(",", _habitatIDs);
        } else {
            return "TRT|"+super._id+"|"+super._name;
        }
    }
}
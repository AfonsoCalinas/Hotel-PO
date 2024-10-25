package hva.employee;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Veterinarian extends Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private List<String> _speciesIDs;

    public Veterinarian(String id, String name, List<String> speciesIDs) {
        super(id, name);
        this._speciesIDs = speciesIDs;
    }

    public List<String> getSpecies() { return _speciesIDs; }
    public void addSpecies(String speciesId) { _speciesIDs.add(speciesId); }
    public void removeSpecies(String speciesId) { _speciesIDs.remove(speciesId); }

    @Override
    public String toString() {
        if (!this._speciesIDs.isEmpty()) {
            return "VET|"+super._id+"|"+super._name+"|"+String.join(",", _speciesIDs);
        } else {
            return "VET|"+super._id+"|"+super._name;
        }
    }
}
package hva.vaccine;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Vaccine implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _id;
    private String _name;
    private List<String> _speciesIDs;
    private int _nApplications;

    public Vaccine(String id, String name, List<String> speciesIDs) {
        this._id = id;
        this._name = name;
        this._speciesIDs = speciesIDs;
        this._nApplications = 0;
    }

    public String getId() { return _id; }
    public List<String> getSpecies() { return _speciesIDs; }
    public void addApplication() { _nApplications += 1; }

    @Override
    public String toString() {
        if (!this._speciesIDs.isEmpty()) {
            this._speciesIDs.sort(String.CASE_INSENSITIVE_ORDER);
            String species = String.join(",", this._speciesIDs);
            return "VACINA|"+this._id+"|"+this._name+"|"+this._nApplications+"|"+species;
        } else {
            return "VACINA|"+this._id+"|"+this._name+"|"+this._nApplications;
        }
        
    }
}
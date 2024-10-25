package hva.animal;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Animal implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _id;
    private String _name;
    private String _speciesID;
    private String _habitatID;
    private String _healthStatus;
    private String _healthHistory;
    private List<String> _vaccinesTaken;

    public Animal(String id, String name, String speciesID, String habitatID) {
        this._id = id;
        this._name = name;
        this._speciesID = speciesID;
        this._habitatID = habitatID;
        this._healthStatus = "VOID";
        this._healthHistory = "";
        this._vaccinesTaken = new ArrayList<>();
    }

    public String getId() { return _id; }
    public String getSpecies() { return _speciesID; }
    public String getHabitatID() { return _habitatID; }
    public void setHabitat(String habitatId) { _habitatID = habitatId; }
    public void addHealthStatus(String status) {
        if (!_healthHistory.isEmpty()) {
            _healthHistory += ",";
        }
        _healthHistory += status;
        _healthStatus = status;
    }
    public void addVaccineTaken(String vaccineId) { _vaccinesTaken.add(vaccineId); }
    public Collection<String> getVaccinesTaken() { return _vaccinesTaken; };


    @Override
    public String toString() {
        return "ANIMAL|"+this._id+"|"+this._name+"|"+this._speciesID+"|"+_healthStatus+"|"+this._habitatID;
    }
}
package hva.vaccine;

import java.io.Serial;
import java.io.Serializable;

public class VaccineHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _vaccineID;
    private String _vetID;
    private String _speciesID;
    private boolean _wrong;

    public VaccineHistory(String vaccineID, String vetID, String speciesID, boolean wrong) {
        this._vaccineID = vaccineID;
        this._vetID = vetID;
        this._speciesID = speciesID;
        this._wrong = wrong;
    }

    public String getVacId() { return _vaccineID; }
    public String getVetId() { return _vetID; }
    public String getSpeId() { return _speciesID; }
    public boolean getWrong() { return _wrong; }

    @Override
    public String toString() {
        return "REGISTO-VACINA|"+this._vaccineID+"|"+this._vetID+"|"+this._speciesID;
    }
}
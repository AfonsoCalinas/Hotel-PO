package hva.tree;

import java.io.Serial;
import java.io.Serializable;
import hva.Season;

public class Tree implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _id;
    private String _name;
    private double _age;
    private int _baseCleaningDifficulty;
    private String _treeType;
    private Season _currentSeason;

    public Tree(String id, String name, double age, int baseCleaningDifficulty, String treeType, Season currentSeason) {
        this._id = id;
        this._name = name;
        this._age = age;
        this._baseCleaningDifficulty = baseCleaningDifficulty;
        this._treeType = treeType;
        this._currentSeason = currentSeason;
    }

    public void ageTree() { _age+= 0.25; }
    public double getAge() { return _age; }
    public int getBaseCleaningEffort() { return _baseCleaningDifficulty; }
    public String getTreeType() { return _treeType; }

    public void setCurrentSeason(Season currentSeason) {
        this._currentSeason = currentSeason;
    }

    public String getBioCycle() {
        boolean isCaduca = _treeType.equals("CADUCA");

        switch (_currentSeason) {
            case PRIMAVERA:
                return "GERARFOLHAS";

            case VERAO:
                return "COMFOLHAS";

            case OUTONO:
                return isCaduca ? "LARGARFOLHAS" : "COMFOLHAS";

            case INVERNO:
                return isCaduca ? "SEMFOLHAS" : "LARGARFOLHAS";

            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "ÁRVORE|"+this._id+"|"+this._name+"|"+(int) Math.floor(this._age)+"|"+this._baseCleaningDifficulty+"|"+this._treeType+"|"+this.getBioCycle();
    }
}
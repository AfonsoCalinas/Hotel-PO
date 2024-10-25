package hva.habitat;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hva.animal.Animal;
import hva.tree.Tree;

public class Habitat implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private String _id;
    private String _name;
    private int _area;
    private Map<String, Tree> _trees;
    private Map<String, String> _adequacy;
    private Map<String, Animal> _animals;

    public Habitat(String id, String name, int area, Map<String, Tree> trees) {
        this._id = id;
        this._name = name;
        this._area = area;
        this._trees = trees;
        this._adequacy = new TreeMap<>();
        this._animals = new TreeMap<>();
    }

    public String getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getArea() {
        return _area;
    }

    public void setArea(int area) {
        _area = area;
    }

    public Map<String, String> getAdequacy() {
        return _adequacy;
    }

    public void addSpecies(String speciesId) {
        _adequacy.put(speciesId, "NEU");
    }

    public void changeAdequacy(String speciesId, String adequacy) {
        _adequacy.put(speciesId, adequacy);
    }

    public void addTree(String id, Tree tree) {
        _trees.put(id, tree);
    }

    public Map<String, Tree> getAllTrees() {
        return _trees;
    }

    public void addAnimal(String id, Animal animal) {
        _animals.put(id, animal);
    }

    public void removeAnimal(String id) {
        _animals.remove(id);
    }

    public Map<String, Animal> getAllAnimals() {
        return _animals;
    }

    @Override
    public String toString() {
        String habitatString = "HABITAT|" + this._id + "|" + this._name + "|" + this._area + "|" + this._trees.size();
        
        if (!this._trees.isEmpty()) {
            for (Tree tree : _trees.values()) {
                habitatString += "\n" + tree.toString();
            }
        }

        return habitatString;
    }
}
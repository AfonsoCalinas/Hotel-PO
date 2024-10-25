package hva;

import hva.species.Species;
import hva.tree.Tree;
import hva.habitat.Habitat;
import hva.animal.Animal;
import hva.employee.Employee;
import hva.employee.Caretaker;
import hva.employee.Veterinarian;
import hva.vaccine.Vaccine;
import hva.vaccine.VaccineHistory;

import java.io.Serial;
import java.io.Serializable;

import java.io.IOException;
import hva.exceptions.ImportFileException;
import hva.exceptions.IncorrectEntryException;
import hva.exceptions.UnknownAnimalKeyException;
import hva.exceptions.UnknownHabitatKeyException;
import hva.exceptions.UnknownSpeciesKeyException;
import hva.exceptions.UnknownEmployeeKeyException;
import hva.exceptions.UnrecognizedEntryException;
import hva.exceptions.DuplicateSpeciesKeyException;
import hva.exceptions.DuplicateTreeKeyException;
import hva.exceptions.DuplicateHabitatKeyException;
import hva.exceptions.DuplicateAnimalKeyException;
import hva.exceptions.DuplicateEmployeeKeyException;
import hva.exceptions.DuplicateVaccineKeyException;
import hva.exceptions.UnknownVaccineKeyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Hotel implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    /** Hotel's attribute showing if it got modified or not. */
    private boolean _changed = false;

    // Here are all the Maps, of Species, Trees, Habitats, Animal, Employees and
    // Vaccines!
    private Map<String, Species> _allSpecies = new TreeMap<String, Species>();
    private Map<String, Tree> _allTrees = new TreeMap<String, Tree>();
    private Map<String, Habitat> _allHabitats = new TreeMap<>(new IdComparator());
    private Map<String, Animal> _allAnimals = new TreeMap<>(new IdComparator());
    private Map<String, Employee> _allEmployees = new TreeMap<>(new IdComparator());
    private Map<String, Vaccine> _allVaccines = new TreeMap<String, Vaccine>();
    private List<VaccineHistory> _vaccineHistory = new ArrayList<>();

    // Representing seasons
    private Season _currentSeason = Season.PRIMAVERA;

    /**
     * Read text input file and create every single aspect we need.
     *
     * @param filename name of the text input file
     * @throws IOException
     * @throws UnrecognizedEntryException
     */
    void importFile(String filename) throws IOException, UnrecognizedEntryException, IncorrectEntryException {
        try (BufferedReader s = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = s.readLine()) != null) {
                importFromParam(line.split("\\|"));
            }
        }
    }

    /**
     * Parses every line in the file.
     *
     * @param param The parameters of the entry to import
     * @throws UnrecognizedEntryException if the entry isn't refering to any of the
     *                                    possible entries
     * @throws IncorrectEntryException    if the entry doesn't have the right number
     *                                    of fields
     * 
     */
    private void importFromParam(String[] param) throws UnrecognizedEntryException, IncorrectEntryException {
        switch (param[0]) {
            case "ESPÉCIE" -> this.importSpecies(param);
            case "ÁRVORE" -> this.importTree(param);
            case "HABITAT" -> this.importHabitat(param);
            case "ANIMAL" -> this.importAnimal(param);
            case "TRATADOR" -> this.importEmployee(param);
            case "VETERINÁRIO" -> this.importEmployee(param);
            case "VACINA" -> this.importVaccine(param);
            default -> throw new UnrecognizedEntryException(String.join("|", param));
        }
    }

    /**
     * Parses a Species.
     * 
     * A correct Species entry has the following format:
     * {@code ESPÉCIE|id|name}
     *
     * @param param The parameters of the Species to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importSpecies(String[] param) throws IncorrectEntryException {
        if (param.length != 3) {
            throw new IncorrectEntryException(param);
        }
        try {
            this.registerSpecies(param[1], param[2]);
        } catch (DuplicateSpeciesKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Parses a Tree.
     * 
     * A correct Tree entry has the following format:
     * {@code ÁRVORE|id|name|age|baseCleaningDifficulty|treeType}
     *
     * @param param The parameters of the Tree to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importTree(String[] param) throws IncorrectEntryException {
        if (param.length != 6) {
            throw new IncorrectEntryException(param);
        }
        try {
            this.registerTree(param[1], param[2], Double.parseDouble(param[3]), Integer.parseInt(param[4]), param[5],
                    _currentSeason);
        } catch (DuplicateTreeKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Parses a Habitat.
     * 
     * A correct Habitat entries have the following format:
     * {@code HABITAT|id|name|area|trees};{@code HABITAT|id|name|area}
     *
     * @param param The parameters of the Habitat to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importHabitat(String[] param) throws IncorrectEntryException {
        if (param.length != 5 && param.length != 4) {
            throw new IncorrectEntryException(param);
        }
        try {
            Map<String, Tree> trees = new TreeMap<>();

            if (param.length == 5) {
                List<String> treeIDs = Arrays.asList(param[4].split(","));
                for (String treeID : treeIDs) {
                    Tree tree = getTreeByID(treeID);
                    if (tree != null) {
                        trees.put(treeID, tree);
                    }
                }
            }

            this.registerHabitat(param[1], param[2], Integer.parseInt(param[3]), trees);
        } catch (DuplicateHabitatKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Parses an Animal.
     * 
     * A correct Animal entry has the following format:
     * {@code ANIMAL|id|name|speciesID|habitatID}
     *
     * @param param The parameters of the Animal to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importAnimal(String[] param) throws IncorrectEntryException {
        if (param.length != 5) {
            throw new IncorrectEntryException(param);
        }
        try {
            this.registerAnimal(param[1], param[2], param[3], param[4]);
        } catch (DuplicateAnimalKeyException | UnknownHabitatKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Parses an Employee.
     * 
     * A correct Employee entry has the following format:
     * {@code TRATADOR|id|name|habitatIDs};{@code VETERINÁRIO|id|name|speciesIDs}
     * {@code TRATADOR|id|name};{@code VETERINÁRIO|id|name}
     *
     * @param param The parameters of the Employee to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importEmployee(String[] param) throws IncorrectEntryException {
        if (param.length != 4 && param.length != 3) {
            throw new IncorrectEntryException(param);
        }
        try {
            List<String> habitatIDs = new ArrayList<>();
            List<String> speciesIDs = new ArrayList<>();
            if (param.length == 4) {
                if (param[0].equals("TRATADOR")) {
                    habitatIDs = new ArrayList<>(Arrays.asList(param[3].split(",")));
                    this.registerCaretaker(param[1], param[2], habitatIDs);
                } else if (param[0].equals("VETERINÁRIO")) {
                    speciesIDs = new ArrayList<>(Arrays.asList(param[3].split(",")));
                    this.registerVeterinarian(param[1], param[2], speciesIDs);
                }
            } else {
                if (param[0].equals("TRATADOR")) {
                    this.registerCaretaker(param[1], param[2], habitatIDs);
                } else if (param[0].equals("VETERINÁRIO")) {
                    this.registerVeterinarian(param[1], param[2], speciesIDs);
                }
            }

        } catch (DuplicateEmployeeKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Parses a Vaccine.
     * 
     * A correct Vaccine entry has the following format:
     * {@code VACINA|id|name|speciesIDs};
     * {@code VACINA|id|name}
     *
     * @param param The parameters of the Vaccine to import
     * @throws IncorrectEntryException if the entry doesn't have the right number of
     *                                 fields
     * 
     */
    private void importVaccine(String[] param) throws IncorrectEntryException {
        if (param.length != 4 && param.length != 3) {
            throw new IncorrectEntryException(param);
        }
        try {
            String speciesIDs = "";
            if (param.length == 4) {
                speciesIDs = param[3];
                try {
                    this.registerVaccine(param[1], param[2], speciesIDs);
                } catch (UnknownSpeciesKeyException e) {
                    throw new IncorrectEntryException(param);
                }
            } else {
                try {
                    this.registerVaccine(param[1], param[2], speciesIDs);
                } catch (UnknownSpeciesKeyException e) {
                    throw new IncorrectEntryException(param);
                }
            }
        } catch (DuplicateVaccineKeyException e) {
            throw new IncorrectEntryException(param);
        }
    }

    /**
     * Register a new species in this hotel, which will be created from the
     * given parameters.
     *
     * @param id   The key of the species
     * @param name The name of the species
     * @return The Species that was just created
     * @throws DuplicateSpeciesKeyException if a species with the given key already
     *                                      exists
     * 
     */
    public Species registerSpecies(String id, String name) throws DuplicateSpeciesKeyException {
        if (this._allSpecies.containsKey(id)) {
            throw new DuplicateSpeciesKeyException(id);
        }

        Species s = new Species(id, name);
        this._allSpecies.put(id, s);
        setChanged(true);
        return s;
    }

    /**
     * Register a new tree in this hotel, which will be created from the
     * given parameters.
     *
     * @param id                     The key of the tree
     * @param name                   The name of the tree
     * @param age                    The age of the tree
     * @param baseCleaningDifficulty The base cleaning difficulty of the tree
     * @param type                   The type of the tree
     * 
     * @return The Tree that was just created
     * 
     * @throws DuplicateTreeKeyException if a tree with the given key already exists
     * 
     */
    public Tree registerTree(String id, String name, double age, int baseCleaningDifficulty, String type,
            Season currentSeason) throws DuplicateTreeKeyException {
        if (this._allTrees.containsKey(id)) {
            throw new DuplicateTreeKeyException(id);
        }

        Tree t = new Tree(id, name, age, baseCleaningDifficulty, type, currentSeason);
        this._allTrees.put(id, t);
        setChanged(true);
        return t;
    }

    /**
     * Register a new habitat in this hotel, which will be created from the
     * given parameters.
     *
     * @param id       The key of the habitat
     * @param name     The name of the habitat
     * @param area     The area of the habitat
     * @param treesIDs The trees IDs of the habitat
     * 
     * @return The Habitat that was just created
     * 
     * @throws DuplicateHabitatKeyException if a habitat with the given key already
     *                                      exists
     * 
     */
    public Habitat registerHabitat(String id, String name, int area, Map<String, Tree> trees)
            throws DuplicateHabitatKeyException {
        if (this._allHabitats.containsKey(id)) {
            throw new DuplicateHabitatKeyException(id);
        }

        // If trees is null, initialize it as an empty map
        if (trees == null) {
            trees = new TreeMap<String, Tree>();
        }

        Habitat h = new Habitat(id, name, area, trees);
        this._allHabitats.put(id, h);
        setChanged(true);
        return h;
    }

    /**
     * Register a new animal in this hotel, which will be created from the
     * given parameters.
     *
     * @param id        The key of the animal
     * @param name      The name of the animal
     * @param speciesID The species' id of the animal
     * @param habitatID The habitat's id of the animal
     * 
     * @return The Animal that was just created
     * 
     * @throws DuplicateAnimalKeyException if an animal with the given key already
     *                                     exists
     * 
     */
    public Animal registerAnimal(String id, String name, String speciesID, String habitatID)
            throws DuplicateAnimalKeyException,
            UnknownHabitatKeyException {
        if (this._allAnimals.containsKey(id)) {
            throw new DuplicateAnimalKeyException(id);
        }

        if (!this._allHabitats.containsKey(habitatID)) {
            throw new UnknownHabitatKeyException(habitatID);
        }

        Animal a = new Animal(id, name, speciesID, habitatID);
        Habitat h = getHabitatByID(habitatID);
        this._allAnimals.put(id, a);
        if (!h.getAdequacy().containsKey(speciesID)) {
            h.addSpecies(speciesID);
        }
        h.addAnimal(id, a);
        setChanged(true);
        return a;
    }

    /**
     * Register a new caretaker in this hotel, which will be created from the
     * given parameters.
     *
     * @param id         The key of the caretaker
     * @param name       The name of the caretaker
     * @param habitatIDs The habitats ids of the caretaker
     * 
     * @return The Caretaker that was just created
     * 
     * @throws DuplicateEmployeeKeyException if an employee with the given key
     *                                       already exists
     * 
     */
    public Caretaker registerCaretaker(String id, String name, List<String> habitatIDs)
            throws DuplicateEmployeeKeyException {
        if (this._allEmployees.containsKey(id)) {
            throw new DuplicateEmployeeKeyException(id);
        }

        if (habitatIDs == null) {
            habitatIDs = new ArrayList<String>();
        }

        Caretaker c = new Caretaker(id, name, habitatIDs);
        this._allEmployees.put(id, c);
        setChanged(true);
        return c;
    }

    /**
     * Register a new veterinarian in this hotel, which will be created from the
     * given parameters.
     *
     * @param id         The key of the veterinarian
     * @param name       The name of the veterinarian
     * @param speciesIDs The species ids of the veterinarian
     * 
     * @return The Veterinarian that was just created
     * 
     * @throws DuplicateEmployeeKeyException if an employee with the given key
     *                                       already exists
     * 
     */
    public Veterinarian registerVeterinarian(String id, String name, List<String> speciesIDs)
            throws DuplicateEmployeeKeyException {
        if (this._allEmployees.containsKey(id)) {
            throw new DuplicateEmployeeKeyException(id);
        }

        if (speciesIDs == null) {
            speciesIDs = new ArrayList<String>();
        }

        Veterinarian v = new Veterinarian(id, name, speciesIDs);
        this._allEmployees.put(id, v);
        setChanged(true);
        return v;
    }

    /**
     * Register a new vaccine in this hotel, which will be created from the
     * given parameters.
     *
     * @param id         The key of the vaccine
     * @param name       The name of the vaccine
     * @param speciesIDs The species ids of the vaccine
     * 
     * @return The Vaccine that was just created
     * 
     * @throws DuplicateVaccineKeyException if a vaccine with the given key already
     *                                      exists
     * 
     */
    public Vaccine registerVaccine(String id, String name, String speciesIDs)
            throws DuplicateVaccineKeyException, UnknownSpeciesKeyException {
        if (this._allVaccines.containsKey(id.toLowerCase())) {
            throw new DuplicateVaccineKeyException(id);
        }

        List<String> speciesIDsList = new ArrayList<>();

        if (!speciesIDs.equals("")) {
            speciesIDsList = new ArrayList<>(Arrays.asList(speciesIDs.split(",")));
            for (String speciesID : speciesIDsList) {
                Species s = getSpeciesByID(speciesID);
                if (s == null) {
                    throw new UnknownSpeciesKeyException(speciesID);
                }
            }
        }

        Vaccine v = new Vaccine(id, name, speciesIDsList);
        this._allVaccines.put(id.toLowerCase(), v);
        setChanged(true);
        return v;
    }

    /**
     * Gets all the animals that exist in the hotel.
     *
     * @return All Animals that exist
     * 
     */
    public Collection<Animal> getAllAnimals() {
        return this._allAnimals.values();
    }

    /**
     * Gets all the employees that exist in the hotel.
     *
     * @return All Employees that exist
     * 
     */
    public Collection<Employee> getAllEmployees() {
        return this._allEmployees.values();
    }

    /**
     * Gets all the habitats that exist in the hotel.
     *
     * @return All Habitats that exist
     * 
     */
    public Collection<Habitat> getAllHabitats() {
        return this._allHabitats.values();
    }

    /**
     * Gets all the vaccines that exist in the hotel.
     *
     * @return All Vaccines that exist
     * 
     */
    public Collection<Vaccine> getAllVaccines() {
        return this._allVaccines.values();
    }

    /**
     * Gets all the vaccinations that took place.
     *
     * @return VaccineHistory
     * 
     */
    public Collection<VaccineHistory> getAllVaccineHistory() {
        return this._vaccineHistory;
    }

    /**
     * Gets all the vaccinations that took place to the wrong animals.
     *
     * @return VaccineHistory
     * 
     */
    public Collection<VaccineHistory> getAllVaccineMistakes() {
        Collection<VaccineHistory> wrongVaccineHistory = new ArrayList<>();

        for (VaccineHistory history : _vaccineHistory) {
            if (history.getWrong()) {
                wrongVaccineHistory.add(history);
            }
        }

        return wrongVaccineHistory;
    }

    /**
     * Gets an animal by its ID.
     *
     * @param id The ID of the animal.
     * @return The Animal with the given ID.
     * @throws UnknownAnimalKeyException if the animal is not found.
     */
    public Animal getAnimalByID(String id) throws UnknownAnimalKeyException {
        Animal animal = _allAnimals.get(id);
        if (animal == null) {
            throw new UnknownAnimalKeyException(id);
        }
        return animal;
    }

    /**
     * Gets a habitat from hotel by their ID.
     *
     * @param id The ID of the habitat
     * @return A habitat from the hotel
     * 
     * @throws UnknownHabitatKeyException if a habitat with the given id doesn't
     *                                    exist
     */
    public Habitat getHabitatByID(String id) throws UnknownHabitatKeyException {
        Habitat h = _allHabitats.get(id);
        if (h == null) {
            throw new UnknownHabitatKeyException(id);
        }
        return h;
    }

    /**
     * Gets a species from hotel by their ID.
     *
     * @param id The ID of the species
     * @return A species from the hotel
     * 
     * @throws UnknownSpeciesKeyException if a species with the given id doesn't
     *                                    exist
     */
    public Species getSpeciesByID(String id) throws UnknownSpeciesKeyException {
        Species s = _allSpecies.get(id);
        if (s == null) {
            throw new UnknownSpeciesKeyException(id);
        }
        return s;
    }

    /**
     * Gets an employee from hotel by their ID.
     *
     * @param id The ID of the employee
     * @return A employee from the hotel
     * 
     * @throws UnknownEmployeeKeyException if an employee with the given id doesn't
     *                                     exist
     */
    public Employee getEmployeeByID(String id) throws UnknownEmployeeKeyException {
        Employee e = _allEmployees.get(id);
        if (e == null) {
            throw new UnknownEmployeeKeyException(id);
        }
        return e;
    }

    /**
     * Gets a vaccine from hotel by their ID.
     *
     * @param id The ID of the vaccine
     * @return A vaccine from the hotel
     * 
     * @throws UnknownVaccineKeyException if a vaccine with the given id doesn't
     *                                    exist
     */
    public Vaccine getVaccineByID(String vaccineId) throws UnknownVaccineKeyException {
        Vaccine vaccine = _allVaccines.get(vaccineId.toLowerCase());
        if (vaccine == null) {
            throw new UnknownVaccineKeyException(vaccineId);
        }
        return vaccine;
    }

    /**
     * Changes a habitat's area in the hotel.
     *
     * @param id   The ID of the habitat
     * @param area The new area to set for the habitat
     * 
     * @throws UnknownHabitatKeyException if a habitat with the given id doesn't
     *                                    exist
     */
    public void changeHabitatArea(String id, int area) throws UnknownHabitatKeyException {
        Habitat h = getHabitatByID(id);
        h.setArea(area);
    }

    /**
     * Changes a habitat's adequacy on a species
     *
     * @param habitatId The ID of the habitat
     * @param speciesId The ID of the species
     * 
     * @throws UnknownHabitatKeyException if a habitat with the given id doesn't
     *                                    exist
     * @throws UnknownSpeciesKeyException if a species with the given id doesn't
     *                                    exist
     */
    public void changeHabitatAdequacy(String habitatId, String speciesId, String adequacy)
            throws UnknownHabitatKeyException, UnknownSpeciesKeyException {
        Habitat h = getHabitatByID(habitatId);
        Species s = getSpeciesByID(speciesId);

        h.changeAdequacy(speciesId, adequacy);
    }

    /**
     * Gets a tree from hotel by their ID.
     *
     * @return A Tree from the hotel
     * 
     */
    public Tree getTreeByID(String id) {
        Tree t = _allTrees.get(id);
        // Might need to check for exception if null
        return t;
    }

    /**
     * Gets all trees from a specific habitat.
     *
     * @return A Map of Trees from the specific habitat
     * 
     */
    public Collection<Tree> getTreesInHabitat(String habitatId) throws UnknownHabitatKeyException {
        Habitat h = this._allHabitats.get(habitatId);

        if (h == null) {
            throw new UnknownHabitatKeyException(habitatId);
        }
        Map<String, Tree> treeMap = h.getAllTrees();
        return treeMap.values();
    }

    public Tree addTreeToHabitat(String habitatId, String treeId, String treeName, double treeAge,
            int treeDiff, String treeType) throws UnknownHabitatKeyException, DuplicateTreeKeyException {

        Habitat h = this._allHabitats.get(habitatId);

        if (h == null) {
            throw new UnknownHabitatKeyException(habitatId);
        }

        registerTree(treeId, treeName, treeAge, treeDiff, treeType, _currentSeason);

        Tree t = this._allTrees.get(treeId);

        h.addTree(treeId, t);

        return t;
    }

    public int getAdequacyScore(String adequacy) {
        if (adequacy.equals("POS")) {
            return 20;
        } else if (adequacy.equals("NEG")) {
            return -20;
        } else {
            return 0;
        }
    }

    public int calcSeasonalTreeCleaningDiff(String treeType) {
        int cleaningValue = 0;
        switch (treeType) {
            case "CADUCA":
                switch (_currentSeason.getValue()) {
                    case 1:
                        cleaningValue = 1;
                        break;
                    case 2:
                        cleaningValue = 2;
                        break;
                    case 3:
                        cleaningValue = 5;
                        break;
                    case 4:
                        cleaningValue = 0;
                        break;

                    default:
                        break;
                }
                break;

            case "PERENE":
                switch (_currentSeason.getValue()) {
                    case 1:
                        cleaningValue = 1;
                        break;
                    case 2:
                        cleaningValue = 1;
                        break;
                    case 3:
                        cleaningValue = 1;
                        break;
                    case 4:
                        cleaningValue = 2;
                        break;

                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return cleaningValue;
    }

    public int calcAnimalSatisfaction(Animal animal) throws UnknownHabitatKeyException {

        String species = animal.getSpecies();
        Habitat habitat = getHabitatByID(animal.getHabitatID());

        Map<String, Animal> animalsInHabitat = habitat.getAllAnimals();

        int iguais = 0;
        int diferentes = 0;

        int population = habitat.getAllAnimals().values().size();
        int area = habitat.getArea();

        for (Animal otherAnimal : animalsInHabitat.values()) {
            if (getHabitatByID(otherAnimal.getHabitatID()).equals(habitat)) {
                if (!otherAnimal.equals(animal)) {
                    if (otherAnimal.getSpecies().equals(species)) {
                        iguais++;
                    } else {
                        diferentes++;
                    }
                }
            }
        }

        double areaPerAnimal = area / population;

        String adequacyLevel = habitat.getAdequacy().get(species);
        int adequacyScore = getAdequacyScore(adequacyLevel);

        double satisfaction = 20
                + 3 * iguais
                - 2 * diferentes
                + areaPerAnimal
                + adequacyScore;

        return (int) Math.round(satisfaction);
    }

    public int calcEmployeeSatisfaction(Employee employee) throws UnknownEmployeeKeyException {

        if (!_allEmployees.containsKey(employee.getId())) {
            throw new UnknownEmployeeKeyException(employee.getId());
        }

        Employee e = _allEmployees.get(employee.getId());

        double satisfaction = 0.0;

        if (e instanceof Veterinarian) {
            Veterinarian vet = (Veterinarian) e;

            double work = 0.0;

            for (String speciesId : vet.getSpecies()) {
                int totalAnimals = 0;
                int totalVets = 0;

                for (Animal animal : _allAnimals.values()) {
                    if (animal.getSpecies().equals(speciesId)) {
                        totalAnimals++;
                    }
                }

                for (Employee otherE : _allEmployees.values()) {
                    if (otherE instanceof Veterinarian) {
                        Veterinarian otherVet = (Veterinarian) otherE;
                        if (otherVet.getSpecies().contains(speciesId)) {
                            totalVets++;
                        }
                    }
                }

                work += totalAnimals / totalVets;
            }

            satisfaction = 20 - work;

        } else if (e instanceof Caretaker) {
            Caretaker care = (Caretaker) e;

            double work = 0.0;

            for (String habitatId : care.getHabitats()) {
                double habitatWork = 0.0;
                int totalCares = 1;
                Habitat h = _allHabitats.get(habitatId);

                int habitatArea = h.getArea();
                int population = h.getAllAnimals().values().size() * 3;

                double cleaningEffort = 0.0;
                for (Tree t : h.getAllTrees().values()) {
                    cleaningEffort += t.getBaseCleaningEffort()
                            * calcSeasonalTreeCleaningDiff(t.getTreeType())
                            * Math.log(t.getAge() + 1);
                }

                habitatWork += habitatArea + population + cleaningEffort;

                for (Employee otherE : _allEmployees.values()) {
                    if (otherE instanceof Caretaker && !otherE.getId().equals(care.getId())) {
                        Caretaker otherCare = (Caretaker) otherE;
                        if (otherCare.getHabitats().contains(habitatId)) {
                            totalCares++;
                        }
                    }
                }

                work += habitatWork / totalCares;
            }

            satisfaction = 300 - work;
        }

        return (int) Math.round(satisfaction);
    }

    public int calcGlobalSatisfaction() throws UnknownHabitatKeyException, UnknownEmployeeKeyException {
        double globalSatisfaction = 0.0;

        for (Animal animal : _allAnimals.values()) {
            globalSatisfaction += calcAnimalSatisfaction(animal);
        }
        for (Employee employee : _allEmployees.values()) {
            globalSatisfaction += calcEmployeeSatisfaction(employee);
        }

        return (int) Math.round(globalSatisfaction);
    }

    public void changeAnimalHabitat(String animalId, String habitatId)
            throws UnknownAnimalKeyException, UnknownHabitatKeyException {

        Habitat h = getHabitatByID(habitatId);
        Animal a = getAnimalByID(animalId);

        Habitat oldH = getHabitatByID(a.getHabitatID());
        oldH.removeAnimal(animalId);
        h.addAnimal(animalId, a);

        a.setHabitat(habitatId);
    }

    /**
     * Gets all the animals in a specific habitat.
     *
     * @return All Animals in a habitat
     * 
     */
    public Collection<Animal> getAnimalsFromHabitat(String habitatId) throws UnknownHabitatKeyException {
        Habitat h = getHabitatByID(habitatId);
        return h.getAllAnimals().values();
    }

    public void addResponsability(String employeeId, String responsabilityId) throws UnknownEmployeeKeyException {
        if (!_allEmployees.containsKey(employeeId)) {
            throw new UnknownEmployeeKeyException(employeeId);
        }

        Employee e = _allEmployees.get(employeeId);

        if (e instanceof Veterinarian) {
            Veterinarian vet = (Veterinarian) e;
            vet.addSpecies(responsabilityId);
        } else if (e instanceof Caretaker) {
            Caretaker care = (Caretaker) e;
            care.addHabitat(responsabilityId);
        }
    }

    public void removeResponsability(String employeeId, String responsabilityId) throws UnknownEmployeeKeyException {
        if (!_allEmployees.containsKey(employeeId)) {
            throw new UnknownEmployeeKeyException(employeeId);
        }

        Employee e = _allEmployees.get(employeeId);

        if (e instanceof Veterinarian) {
            Veterinarian vet = (Veterinarian) e;
            vet.removeSpecies(responsabilityId);
        } else if (e instanceof Caretaker) {
            Caretaker care = (Caretaker) e;
            care.removeHabitat(responsabilityId);
        }
    }

    /**
     * Calcula o número de caracteres distintos entre os nomes de duas espécies.
     * 
     * @param species1 Nome da primeira espécie.
     * @param species2 Nome da segunda espécie.
     * @return O número de caracteres diferentes entre os dois nomes.
     */
    public int calculateSpeciesDifference(String species1, String species2) {

        species1 = species1.toLowerCase();
        species2 = species2.toLowerCase();

        int maxLength = Math.max(species1.length(), species2.length());

        Map<Character, Integer> species1Dict = new TreeMap<Character, Integer>();
        for (char c : species1.toCharArray()) {
            if (species1Dict.containsKey(c)) {
                species1Dict.put(c, species1Dict.getOrDefault(c, 0) + 1);
            } else {
                species1Dict.put(c, 1);
            }
        }

        Map<Character, Integer> species2Dict = new TreeMap<Character, Integer>();
        for (char c : species2.toCharArray()) {
            if (species2Dict.containsKey(c)) {
                species2Dict.put(c, species2Dict.getOrDefault(c, 0) + 1);
            } else {
                species2Dict.put(c, 1);
            }
        }

        int commonChars = 0;

        for (char c : species1Dict.keySet()) {
            if (species2Dict.containsKey(c)) {
                commonChars += Math.min(species1Dict.get(c), species2Dict.get(c));
            }
        }

        return maxLength - commonChars;
    }

    /**
     * Calcula o dano de uma vacina aplicada a um animal.
     * 
     * @param vaccine A vacina aplicada.
     * @param animal  O animal que recebeu a vacina.
     * @return O valor do dano causado.
     */
    public int calculateDamage(Vaccine vaccine, Animal animal) {
        int dmg = 0;
        if (!vaccine.getSpecies().contains(animal.getSpecies())) {
            String animalSpecies = animal.getSpecies();

            for (String vaccineSpecies : vaccine.getSpecies()) {
                try {
                    int damage = calculateSpeciesDifference(getSpeciesByID(animalSpecies).getName(), getSpeciesByID(vaccineSpecies).getName());
                    dmg = Math.max(dmg, damage);
                } catch (UnknownSpeciesKeyException e) {
                    // Do nothing, at this point getting the species will never throw exceptions here
                }
            }
        }
        return dmg;
    }

    public int vaccinateAnimal(String vaccineId, String vetId, String animalId)
            throws UnknownAnimalKeyException, UnknownEmployeeKeyException, UnknownVaccineKeyException {
        int warning = 0;
        Animal a = getAnimalByID(animalId);
        Employee e = getEmployeeByID(vetId);
        if (!(e instanceof Veterinarian)) {
            throw new UnknownEmployeeKeyException(vetId);
        }
        Vaccine v = getVaccineByID(vaccineId);

        int damage = calculateDamage(v, a);

        if (!v.getSpecies().contains(a.getSpecies())) {
            _vaccineHistory.add(new VaccineHistory(vaccineId, vetId, a.getSpecies(), true));
        } else {
            _vaccineHistory.add(new VaccineHistory(vaccineId, vetId, a.getSpecies(), false));
        }

        addVaccination(v, a, damage);
        if (!v.getSpecies().contains(a.getSpecies())) {
            warning = 1;
        }
        return warning;
    }

    public void addVaccination(Vaccine vaccine, Animal animal, int damage) {
        String status;

        // Determina o status de acordo com o dano causado
        if (damage == 0) {
            if (!vaccine.getSpecies().contains(animal.getSpecies())) {
                status = "CONFUSÃO";
            } else {
                status = "NORMAL";
            }
        } else if (damage >= 1 && damage <= 4) {
            status = "ACIDENTE";
        } else {
            status = "ERRO";
        }

        vaccine.addApplication();
        animal.addHealthStatus(status);
        animal.addVaccineTaken(vaccine.getId());
    }

    public Collection<Vaccine> getAllMedicalActs(String animalId) throws UnknownAnimalKeyException,
        UnknownVaccineKeyException {
        Animal a = getAnimalByID(animalId);
        Collection<Vaccine> vaccines = new ArrayList<>();

        for (String vaccineId : a.getVaccinesTaken()) {
            Vaccine vaccine = getVaccineByID(vaccineId);
            vaccines.add(vaccine);
        }
        
        return vaccines;
    }

    public Collection<VaccineHistory> getAllMedicalActsFromVets(String vetId) throws UnknownEmployeeKeyException,
        UnknownVaccineKeyException {

        if (!(getEmployeeByID(vetId) instanceof Veterinarian)) {
            throw new UnknownEmployeeKeyException(vetId);
        }

        Collection<VaccineHistory> vaccines = new ArrayList<>();

        for (VaccineHistory vaccine : _vaccineHistory) {
            if (vaccine.getVetId().equals(vetId)) {
                vaccines.add(vaccine);
            }
        }
        
        return vaccines;
    }

    /**
     * Advances the season to the next one
     */
    public void advanceSeason() {
        _currentSeason = _currentSeason.nextSeason();

        for (Tree tree : _allTrees.values()) {
            tree.setCurrentSeason(_currentSeason);
            tree.ageTree();
        }
    }

    /**
     * @return integer value of the current season
     */
    public int getSeasonValue() {
        return _currentSeason.getValue();
    }

    /**
     * Gets the changed attribute
     * 
     * @return changed attribute
     */
    public boolean getChange() {
        return _changed;
    }

    /**
     * Sets the changed attribute
     * 
     * @param change Either true or false
     */
    public void setChanged(boolean change) {
        _changed = change;
    }
}

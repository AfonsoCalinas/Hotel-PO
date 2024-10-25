package hva;

import java.io.*;
import hva.exceptions.*;

/**
 * Class that represents the hotel application.
 */
public class HotelManager {

    /** Represents the current hotel in the application. */
    private Hotel _hotel = new Hotel();

    /** File's name if we do anything related to saving or loading */
    private String _filename = "";

    /**
     * Saves the current hotel into the file.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current hotel doesn't have a file.
     * @throws IOException if there is some other error while saving the hotel.
     * 
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_filename == null || _filename.isBlank())
            throw new MissingFileAssociationException();

        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(_filename)))) {
            out.writeObject(this._hotel);
        }
    }

    /**
     * Saves the current hotel into the file.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current hotel doesn't have a file.
     * @throws IOException if there is some other error while saving the hotel.
     * 
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *                                  an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream in = new ObjectInputStream(
            new BufferedInputStream(new FileInputStream(filename)))) {
            this._hotel = (Hotel) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file.
     *
     * @param filename name of the text input file
     * @throws ImportFileException if there is an error importing the data from the file.
     */
    public void importFile(String filename) throws ImportFileException {
        try {
            this._hotel.importFile(filename);
        } catch (IOException | UnrecognizedEntryException | IncorrectEntryException e) {
            throw new ImportFileException(filename);
        }
    }

    public Hotel getHotel() { return _hotel; }

    /**
     * @return changed attribute from the Hotel
     */
    public boolean changed() {
        return _hotel.getChange();
    }

    /**
     * Resets the hotel changed parameter
     */
    public void resetChanged() {
        this._hotel.setChanged(false);
    }

    /**
     * Resets the current hotel to a brand new one
     */
    public void reset() {
        _hotel = new Hotel();
        _filename = "";
    }

    /**
     * Advances the season
     */
    public void nextSeason() {
        this._hotel.advanceSeason();
    }

    public int getSeason() {
        return this._hotel.getSeasonValue();
    }

    public int doCalculation() throws UnknownHabitatKeyException, UnknownEmployeeKeyException {
        return this._hotel.calcGlobalSatisfaction();
    }

}

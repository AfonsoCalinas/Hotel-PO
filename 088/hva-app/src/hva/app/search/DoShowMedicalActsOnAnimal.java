package hva.app.search;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownAnimalKeyException;
import hva.app.exceptions.UnknownVaccineKeyException;

class DoShowMedicalActsOnAnimal extends Command<Hotel> {

    DoShowMedicalActsOnAnimal(Hotel receiver) {
        super(Label.MEDICAL_ACTS_ON_ANIMAL, receiver);
        addStringField("animalId", hva.app.animal.Prompt.animalKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _display.popup(_receiver.getAllMedicalActs(stringField("animalId")));
        } catch (hva.exceptions.UnknownAnimalKeyException e) {
            throw new UnknownAnimalKeyException(e.getKey());
        } catch (hva.exceptions.UnknownVaccineKeyException e) {
            throw new UnknownVaccineKeyException(e.getKey());
        }
        
    }

}

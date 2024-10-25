package hva.app.vaccine;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownAnimalKeyException;
import hva.app.exceptions.UnknownVaccineKeyException;
import hva.app.exceptions.UnknownVeterinarianKeyException;

class DoVaccinateAnimal extends Command<Hotel> {

    DoVaccinateAnimal(Hotel receiver) {
        super(Label.VACCINATE_ANIMAL, receiver);
        addStringField("vaccineId", Prompt.vaccineKey());
        addStringField("vetId", Prompt.veterinarianKey());
        addStringField("animalId", hva.app.animal.Prompt.animalKey());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            if (_receiver.vaccinateAnimal(stringField("vaccineId"), 
                                          stringField("vetId"), 
                                          stringField("animalId")) == 1) {
                _display.popup(Message.wrongVaccine(stringField("vaccineId"), stringField("animalId")));
            }
        } catch (hva.exceptions.UnknownAnimalKeyException e) {
            throw new UnknownAnimalKeyException(e.getKey());
        } catch (hva.exceptions.UnknownVaccineKeyException e) {
            throw new UnknownVaccineKeyException(e.getKey());
        } catch (hva.exceptions.UnknownEmployeeKeyException e) {
            throw new UnknownVeterinarianKeyException(e.getKey());
        }
    } 
}

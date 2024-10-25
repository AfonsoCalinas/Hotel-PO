package hva.app.animal;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.app.exceptions.DuplicateAnimalKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;

/**
 * Command for registering an animal.
 */
class DoRegisterAnimal extends Command<Hotel> {

    DoRegisterAnimal(Hotel receiver) {
        super(Label.REGISTER_ANIMAL, receiver);
        addStringField("id", Prompt.animalKey());
        addStringField("name", Prompt.animalName());
        addStringField("speciesId", Prompt.speciesKey());
        addStringField("habitatId", "Identificador único do habitat: ");
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.registerAnimal(
                stringField("id"),
                stringField("name"),
                stringField("speciesId"),
                stringField("habitatId")
            );
        } catch (hva.exceptions.DuplicateAnimalKeyException e) {
            throw new DuplicateAnimalKeyException(e.getKey());
        } catch (hva.exceptions.UnknownHabitatKeyException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        }
    }
}

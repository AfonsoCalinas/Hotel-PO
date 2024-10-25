package hva.app.habitat;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.DuplicateHabitatKeyException;

class DoRegisterHabitat extends Command<Hotel> {

    DoRegisterHabitat(Hotel receiver) {
        super(Label.REGISTER_HABITAT, receiver);
        addStringField("id", Prompt.habitatKey());
        addStringField("name", Prompt.habitatName());
        addIntegerField("area", Prompt.habitatArea());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.registerHabitat(
                    stringField("id"),
                    stringField("name"),
                    integerField("area"),
                    null);
        } catch (hva.exceptions.DuplicateHabitatKeyException e) {
            throw new DuplicateHabitatKeyException(e.getKey());
        }
    }
}

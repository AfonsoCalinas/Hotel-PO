package hva.app.habitat;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.app.exceptions.UnknownSpeciesKeyException;

class DoChangeHabitatInfluence extends Command<Hotel> {

    DoChangeHabitatInfluence(Hotel receiver) {
        super(Label.CHANGE_HABITAT_INFLUENCE, receiver);
        addStringField("habitatId", Prompt.habitatKey());
        addStringField("speciesId", hva.app.animal.Prompt.speciesKey());
        addStringField("habitatInfluence", Prompt.habitatInfluence());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.changeHabitatAdequacy(stringField("habitatId"), 
                                            stringField("speciesId"), 
                                            stringField("habitatInfluence"));
        } catch (hva.exceptions.UnknownHabitatKeyException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (hva.exceptions.UnknownSpeciesKeyException e) {
            throw new UnknownSpeciesKeyException(e.getKey());
        }
    }

}

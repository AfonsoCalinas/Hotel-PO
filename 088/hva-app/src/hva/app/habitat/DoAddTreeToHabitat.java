package hva.app.habitat;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.DuplicateTreeKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;

class DoAddTreeToHabitat extends Command<Hotel> {

    DoAddTreeToHabitat(Hotel receiver) {
        super(Label.ADD_TREE_TO_HABITAT, receiver);
        addStringField("habitatId", Prompt.habitatKey());
        addStringField("treeId", Prompt.treeKey());
        addStringField("treeName", Prompt.treeName());
        addIntegerField("treeAge", Prompt.treeAge());
        addIntegerField("treeDiff", Prompt.treeDifficulty());
        addStringField("treeType", Prompt.treeType());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _display.popup(_receiver.addTreeToHabitat(stringField("habitatId"), stringField("treeId"),
                                                      stringField("treeName"), integerField("treeAge"),
                                                      integerField("treeDiff"), stringField("treeType")));
        } catch (hva.exceptions.UnknownHabitatKeyException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (hva.exceptions.DuplicateTreeKeyException e) {
            throw new DuplicateTreeKeyException(e.getKey());
        }
    }

}

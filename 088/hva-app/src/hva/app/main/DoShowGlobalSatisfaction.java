package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.app.exceptions.UnknownEmployeeKeyException;

class DoShowGlobalSatisfaction extends Command<HotelManager> {
    DoShowGlobalSatisfaction(HotelManager receiver) {
        super(hva.app.main.Label.SHOW_GLOBAL_SATISFACTION, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _display.popup(_receiver.doCalculation());
        } catch (hva.exceptions.UnknownHabitatKeyException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (hva.exceptions.UnknownEmployeeKeyException e) {
            throw new UnknownEmployeeKeyException(e.getKey());
        }
    }
}

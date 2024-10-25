package hva.app.search;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.UnknownVaccineKeyException;
import hva.app.exceptions.UnknownVeterinarianKeyException;

class DoShowMedicalActsByVeterinarian extends Command<Hotel> {

    DoShowMedicalActsByVeterinarian(Hotel receiver) {
        super(Label.MEDICAL_ACTS_BY_VET, receiver);
        addStringField("vetId", hva.app.employee.Prompt.employeeKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _display.popup(_receiver.getAllMedicalActsFromVets(stringField("vetId")));
        } catch (hva.exceptions.UnknownVaccineKeyException e) {
            throw new UnknownVaccineKeyException(e.getKey());
        } catch (hva.exceptions.UnknownEmployeeKeyException e) {
            throw new UnknownVeterinarianKeyException(e.getKey());
        }
    }

}

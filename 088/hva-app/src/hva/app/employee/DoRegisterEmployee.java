package hva.app.employee;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import hva.app.exceptions.DuplicateEmployeeKeyException;

/**
 * Register new Employee
 */
class DoRegisterEmployee extends Command<Hotel> {

    DoRegisterEmployee(Hotel receiver) {
        super(Label.REGISTER_EMPLOYEE, receiver);
        addStringField("id", Prompt.employeeKey());
        addStringField("name", Prompt.employeeName());
        addOptionField("type", Prompt.employeeType(), "TRT", "VET", "trt", "vet");
    }

    @Override
    protected void execute() throws CommandException {

        try {
            // Convert to lowercase to avoid case-sensitive issues
            if (stringField("type").toLowerCase().equals("trt")) {
                _receiver.registerCaretaker(stringField("id"), stringField("name"), null);
            } else if (stringField("type").toLowerCase().equals("vet")) {
                _receiver.registerVeterinarian(stringField("id"), stringField("name"), null);
            }
        } catch (hva.exceptions.DuplicateEmployeeKeyException e) {
            throw new DuplicateEmployeeKeyException(e.getKey());
        }
    }
}

package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.samples.Utils.FHIRActions;
import com.automationanywhere.botcommand.samples.Utils.FHIRServer;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.HELP;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author James Dickson
 *
 */
@BotCommand
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "FindAppointment",
        label = "Find Apointment",
        node_label = "Find appointment for given time slot in session {{sessionName}}",
        group_label = "Appointment",
        description = "Returns Appointment FHIR IDs and time slots",
        icon = "fhir2.svg",
        comment = true ,
        //text_color = "#7B848B" ,
        return_label = "Assign output to a list",
        return_required = true,
        return_type = DataType.LIST,
        return_description = "List of dictionaries with keys 'id', 'startTime', 'endTime'")

public class FindAppointment {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public ListValue<Value> action(
            @Idx(index = "1", type = HELP)
            @Pkg(label = "Coverage Search", description = "Searches for Appointments")
                    String help,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Start Time", description = "When to start the search - must be in ISO Instant format")
            @NotEmpty String startTime,
            @Idx(index = "4", type = TEXT) @Pkg(label = "End Time", description = "When to end the search - must be in ISO Instant format")
                String endTime,
            @Idx(index = "5", type = TEXT) @Pkg(label = "Family (Last) Name", description = "The patient's family (last) name.")
                    String family,
            @Idx(index = "6", type = TEXT) @Pkg(label = "Given Name", description = "The patient's given name. May include first and middle names.")
                    String given,
            @Idx(index = "7", type = TEXT) @Pkg(label = "Birthdate", description = "The patient's date of birth in the format YYYY-MM-DD.")
                    String birthdate,
            @Idx(index = "8", type = TEXT) @Pkg(label = "Address", description = "The patient's street address.")
                    String street,
            @Idx(index = "9", type = TEXT) @Pkg(label = "City", description = "The city for patient's home address.")
                    String city,
            @Idx(index = "10", type = TEXT) @Pkg(label = "State", description = "The state for the patient's home address.")
                    String state,
            @Idx(index = "11", type = TEXT) @Pkg(label = "Postal Code", description = "The postal code for patient's home address.")
                    String postal,
            @Idx(index = "12", type = TEXT) @Pkg(label = "Country")
                    String country,
            @Idx(index = "13", type = TEXT) @Pkg(label = "Gender", description = "The patient’s legal sex.")
                    String gender,
            @Idx(index = "14", type = TEXT) @Pkg(label = "Marital Status", description = "The patient's marital status. e.g. 'single' or 'married'")
                    String maritalStatus,
            @Idx(index = "15", type = TEXT) @Pkg(label = "Phone", description = "The patient's phone number")
                    String phone
    ) throws IOException, ParseException {
        FHIRServer fhirServer = (FHIRServer) this.sessionMap.get(sessionName);
        String access_token = fhirServer.getToken();
        String url = fhirServer.getURL();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("street", street);
        params.put("city", city);
        params.put("postalCode", postal);
        params.put("state", state);
        params.put("country", country);
        params.put("family", family);
        params.put("given", given);
        params.put("birthDate", birthdate);
        params.put("gender", gender);
        params.put("phone", phone);
        params.put("maritalStatus", maritalStatus);
        List<Value> resMap = FHIRActions.searchAppointments(url, "Bearer " + access_token, startTime, endTime, params);
        ListValue<Value> returnValue = new ListValue<>();
        returnValue.set(resMap);
        return returnValue;
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}

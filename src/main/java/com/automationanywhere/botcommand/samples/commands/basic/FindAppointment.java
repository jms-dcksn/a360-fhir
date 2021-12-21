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
            @Idx(index = "3", type = TEXT) @Pkg(label = "Start Time", description = "When to start the search")
            @NotEmpty String startTime,
            @Idx(index = "4", type = TEXT) @Pkg(label = "End Time", description = "When to end the search")
                String endTime
    ) throws IOException, ParseException {
        FHIRServer fhirServer = (FHIRServer) this.sessionMap.get(sessionName);
        String access_token = fhirServer.getToken();
        String url = fhirServer.getURL();
        List<Value> resMap = FHIRActions.searchAppointments(url, "Bearer " + access_token, startTime, endTime, null);
        ListValue<Value> returnValue = new ListValue<>();
        returnValue.set(resMap);
        return returnValue;
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}

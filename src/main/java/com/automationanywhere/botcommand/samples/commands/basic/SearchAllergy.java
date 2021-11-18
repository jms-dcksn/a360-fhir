package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
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
        name = "SearchAllergy",
        label = "Allergy Intolerance Search",
        node_label = "Search allergy intolerances by patient in FHIR session {{sessionName}}",
        group_label = "Allergy",
        description = "Returns Allergy Intolerance FHIR IDs",
        icon = "fhir2.svg",
        comment = true ,
        //text_color = "#7B848B" ,
        return_label = "Assign output to a list",
        return_type = DataType.LIST,
        return_description = "List of strings containing the found allergy intolerance FHIR IDs")

public class SearchAllergy {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public ListValue<StringValue> action(
            @Idx(index = "1", type = HELP)
            @Pkg(label = "ALlergy Intolerance Search", description = "Searches for Allergy Intolerance resources.")
                    String help,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Patient FHIR ID")
            @NotEmpty String patientId
    ) throws IOException, ParseException {
        FHIRServer fhirServer = (FHIRServer) this.sessionMap.get(sessionName);
        String access_token = fhirServer.getToken();
        String url = fhirServer.getURL();
        List<Value> resMap = FHIRActions.searchAllergyIntolerance(url, "Bearer " + access_token, patientId);
        ListValue<StringValue> returnValue = new ListValue<>();
        returnValue.set(resMap);
        return returnValue;
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}

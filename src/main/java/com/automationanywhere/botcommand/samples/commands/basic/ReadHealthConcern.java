package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.samples.Utils.FHIRActions;
import com.automationanywhere.botcommand.samples.Utils.FHIRServer;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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
        name = "ReadHealthConcern",
        label = "Health Concern Lookup",
        node_label = "Read health concern in FHIR session {{sessionName}}",
        group_label = "Flag",
        description = "Returns Health Concern Details",
        icon = "fhir2.svg",
        comment = true ,
        //text_color = "#7B848B" ,
        return_label = "Assign output to a dictionary",
        return_type = DataType.DICTIONARY,
        return_description = "Keys for dictionary: 'description', 'category', 'patient', 'period'")

public class ReadHealthConcern {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue action(
            @Idx(index = "1", type = HELP)
            @Pkg(label = "Read Allergy", description = "Retrieves allergy resource by its FHIR ID.")
                    String help,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "FHIR ID", description = "The FHIR Health Concern ID")
            @NotEmpty String fhirId
    ) throws IOException, ParseException {
        FHIRServer fhirServer = (FHIRServer) this.sessionMap.get(sessionName);
        String access_token = fhirServer.getToken();
        String url = fhirServer.getURL();
        Map<String, Value> resMap = FHIRActions.readHealthConcern(url, "Bearer " + access_token, fhirId);
        return new DictionaryValue(resMap);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}

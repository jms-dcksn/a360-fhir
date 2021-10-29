package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.record.Record;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.Utils.FHIRActions;
import com.automationanywhere.botcommand.samples.Utils.FHIRServer;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.EntryList.EntryListAddButtonLabel;
import com.automationanywhere.commandsdk.annotations.rules.EntryList.EntryListEmptyLabel;
import com.automationanywhere.commandsdk.annotations.rules.EntryList.EntryListEntryUnique;
import com.automationanywhere.commandsdk.annotations.rules.EntryList.EntryListLabel;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

/**
 * @author James Dickson
 *
 */
@BotCommand
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "SearchPatient",
        label = "Patient Search",
        node_label = "Search for a patient in FHIR session {{sessionName}}",
        group_label = "Patients",
        description = "Returns Patient Details",
        icon = "fhir2.svg",
        comment = true ,
        //text_color = "#7B848B" ,
        return_label = "Assign output to a dictionary",
        return_type = DataType.DICTIONARY,
        return_description = "Keys for dictionary:'results', 'id'")

public class PatientSearch {
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue action(
            @Idx(index = "1", type = HELP)
            @Pkg(label = "Patient Search", description = "The FHIR Patient resource defines demographics, care providers, and other administrative information about a person receiving care at a health organization.\n" +
                    "\n" +
                    "Starting in May 2019, Patient.Search requests require one of the following minimum data sets by default in order to match and return a patient record:\n" +
                    "\n" +
                    "- FHIR ID\n" +
                    "- {IDType}|{ID}\n" +
                    "- SSN identifier\n" +
                    "- Given name, family name, and birthdate\n" +
                    "- Given name, family name, legal sex, and phone number/email")
                    String help,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default")
            @NotEmpty String sessionName,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Address", description = "The patient's street address.")
                    String address,
            @Idx(index = "4", type = TEXT) @Pkg(label = "City", description = "The city for patient's home address.")
                    String city,
            @Idx(index = "5", type = TEXT) @Pkg(label = "State", description = "The state for the patient's home address.")
                    String state,
            @Idx(index = "6", type = TEXT) @Pkg(label = "Postal Code", description = "The postal code for patient's home address.")
                    String postal,
            @Idx(index = "7", type = TEXT) @Pkg(label = "Family (Last) Name", description = "The patient's family (last) name.")
                    String family,
            @Idx(index = "8", type = TEXT) @Pkg(label = "Given Name", description = "The patient's given name. May include first and middle names.")
                    String given,
            @Idx(index = "9", type = TEXT) @Pkg(label = "Birthdate", description = "The patient's date of birth in the format YYYY-MM-DD.")
                    String birthdate,
            @Idx(index = "10", type = TEXT) @Pkg(label = "Gender", description = "The patient’s legal sex.")
                    String gender,
            @Idx(index = "11", type = TEXT) @Pkg(label = "Phone or Email", description = "The patient's phone number or email.")
                    String telecom,
            @Idx(index = "12", type = TEXT) @Pkg(label = "FHIR ID")
                    String fhir_id
    ) throws IOException, ParseException {
        FHIRServer fhirServer = (FHIRServer) this.sessionMap.get(sessionName);
        String access_token = fhirServer.getToken();
        String url = fhirServer.getURL();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("address", address);
        params.put("address-city", city);
        params.put("address-postalcode", postal);
        params.put("address-state", state);
        params.put("family", family);
        params.put("given", given);
        params.put("birthdate", birthdate);
        params.put("legal-sex", gender);
        params.put("telecom", telecom);
        params.put("_id", fhir_id);

        Map<String, Value> resMap = FHIRActions.searchPatient(url, "Bearer " + access_token, params);
        return new DictionaryValue(resMap);
    }
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}

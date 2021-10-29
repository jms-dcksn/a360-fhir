package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class FHIRActions {

    public static Map<String, Value> searchPatient(String url, String auth, Map<String, String> params) throws IOException, ParseException {
        String patientUrl = url + "api/FHIR/R4/Patient";
        Map<String, Value> resMap = new LinkedHashMap<>();

        String searchResponse = HTTPRequest.HttpGetWithParams(patientUrl, auth, params);
        Object obj = new JSONParser().parse(searchResponse);
        JSONObject response = (JSONObject) obj;
        resMap.put("results", (new StringValue(String.valueOf(response.get("total")))));
        JSONArray entries = (JSONArray) response.get("entry");
        if(String.valueOf(response.get("total")).equals("0")){
            //The search didn't return any results, so report back the message
            JSONObject firstEntry = (JSONObject) entries.get(0);
            JSONObject resource = (JSONObject) firstEntry.get("resource");
            JSONArray issue = (JSONArray) resource.get("issue");
            JSONObject firstIssue = (JSONObject) issue.get(0);
            JSONObject details = (JSONObject) firstIssue.get("details");
            String text = (String) details.get("text");
            resMap.put("message", new StringValue(text));
        }
        else{
            //Code to parse entries/list of results
            for (int i=0; i < entries.size(); i++){
                JSONObject entry = (JSONObject) entries.get(i);
                JSONObject resource = (JSONObject) entry.get("resource");
                resMap.put("id", new StringValue(String.valueOf(resource.get("id"))));
                JSONArray names = (JSONArray) resource.get("name");
                JSONObject name = (JSONObject) names.get(0); //only getting first name object
                resMap.put("name", new StringValue(String.valueOf(name.get("text"))));
                resMap.put("gender", new StringValue(String.valueOf(resource.get("gender"))));
                JSONObject maritalStatus = (JSONObject) resource.get("maritalStatus");
                resMap.put("maritalStatus", new StringValue(String.valueOf(maritalStatus.get("text"))));
                resMap.put("birthDate", new StringValue(String.valueOf(resource.get("birthDate"))));
                //get home address
                JSONArray addresses = (JSONArray) resource.get("address");
                for (int j=0; j < addresses.size(); j++){
                    JSONObject address = (JSONObject) addresses.get(j);
                    if(String.valueOf(address.get("use")).equals("home")){
                        List streets = (List) address.get("line");
                        String street = (String) streets.get(0);
                        resMap.put("street", new StringValue(street));
                        resMap.put("city", new StringValue(String.valueOf(address.get("city"))));
                        resMap.put("state", new StringValue(String.valueOf(address.get("state"))));
                        resMap.put("postalCode", new StringValue(String.valueOf(address.get("postalCode"))));
                        resMap.put("country", new StringValue(String.valueOf(address.get("country"))));
                    } else {
                        resMap.put("street", new StringValue("no home address found"));
                        resMap.put("city", new StringValue(String.valueOf(address.get("no home address found"))));
                        resMap.put("state", new StringValue(String.valueOf(address.get("no home address found"))));
                        resMap.put("postalCode", new StringValue(String.valueOf(address.get("no home address found"))));
                        resMap.put("country", new StringValue(String.valueOf(address.get("no home address found"))));
                    }
                }
                //phone number
                JSONArray telecom = (JSONArray) resource.get("telecom");
                JSONObject phoneInfo = (JSONObject) telecom.get(0); //getting only first result
                resMap.put("phone", new StringValue(String.valueOf(phoneInfo.get("value"))));
                break; //ignoring array right now...
            }
        }
        return resMap;
    }

}
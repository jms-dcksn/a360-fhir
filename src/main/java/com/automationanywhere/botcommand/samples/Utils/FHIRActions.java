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
                resMap.put("gender", new StringValue(String.valueOf(resource.get("gender"))));
                JSONObject maritalStatus = (JSONObject) resource.get("maritalStatus");
                resMap.put("maritalStatus", new StringValue(String.valueOf(maritalStatus.get("text"))));
            }
        }
        return resMap;
    }

    public static String createUser(String url, String token, String name, String department, String email, JSONArray addresses, String title, String password, String divisionId, String state) throws IOException {
        url = "https://api." + url + "/api/v2/users";
        String auth = "Bearer " + token;
        JSONObject postBody = new JSONObject();
        postBody.put("name", name);
        postBody.put("department", department);
        postBody.put("email", email);
        postBody.put("addresses", addresses);
        postBody.put("title", title);
        postBody.put("password", password);
        postBody.put("divisionId", divisionId);
        postBody.put("state", state);
        String response = HTTPRequest.SEND(auth, url, "POST", String.valueOf(postBody));
        return response;
    }


    public static String addContactToList(String url, String token, String contactListId, List<Value> fields) throws IOException {
        url = "https://api." + url + "/api/v2/outbound/contactlists/" + contactListId + "/contacts";
        JSONObject jsonContactData = new JSONObject();

        if(fields!=null && fields.size()>0){
            for (Value element : fields){
                Map<String, Value> customValuesMap = ((DictionaryValue)element).get();
                String name = customValuesMap.containsKey("NAME") ? ((StringValue)customValuesMap.get("NAME")).get() : "";
                String value = (customValuesMap.getOrDefault("VALUE", null) == null) ? null : ((StringValue)customValuesMap.get("VALUE")).get();
                if(!value.equals(null)){
                    jsonContactData.put(name, value);
                }
            }
        }
        String jsonBody = "[{ \"id\": \"\", \"contactListId\": \"" + contactListId + "\", \"data\": " + jsonContactData + ", \"callable\": true, \"phoneNumberStatus\": {}}]";
        //System.out.println(jsonBody);
        String auth = "Bearer " + token;
        String response = "";
        response = HTTPRequest.SEND(auth, url, "POST", jsonBody);
        return response;
    }
}
package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opensaml.xml.signature.J;

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

    public static String createPatient(String url, String auth, Map<String, String> params) throws IOException, ParseException {
        String patientUrl = url + "api/FHIR/R4/Patient";
        JSONObject body = new JSONObject();
        body.put("resourceType", "Patient");
        //Code to build JSON structure for body...
        body.put("active", "true");
        //JSON structure for SSN...
        JSONArray identifier = new JSONArray();
        JSONObject jsonIdentifier = new JSONObject();
        jsonIdentifier.put("use", "usual");
        jsonIdentifier.put("system", "urn:oid:2.16.840.1.113883.4.1");
        jsonIdentifier.put("value", params.get("ssn"));
        identifier.add(jsonIdentifier);
        body.put("identifier", identifier);

        //JSON array for name...
        JSONArray name = new JSONArray();
        JSONObject nameDetails = new JSONObject();
        nameDetails.put("use", "usual");
        nameDetails.put("family", params.get("family"));
        List<String> givenName = new ArrayList<>();
        givenName.add(params.get("given"));
        nameDetails.put("given", givenName);
        name.add(nameDetails);
        body.put("name", name);

        //JSON array for telecom
        JSONArray telecom = new JSONArray();
        JSONObject phone = new JSONObject();
        phone.put("system", "phone");
        phone.put("value", params.get("phone"));
        phone.put("use", "home");
        JSONObject email = new JSONObject();
        email.put("system", "email");
        email.put("value", params.get("email"));
        telecom.add(phone);
        telecom.add(email);
        body.put("telecom", telecom);

        body.put("gender", params.get("gender"));
        body.put("birthDate", params.get("birthDate"));

        //JSOn Array for address
        JSONArray address = new JSONArray();
        JSONObject addressDetails = new JSONObject();
        addressDetails.put("use", "home");
        List<String> line = new ArrayList<>();
        line.add(params.get("street")); //params to hole street address at key 'street'
        addressDetails.put("line", line);
        addressDetails.put("city", params.get("city"));
        addressDetails.put("state", params.get("state"));
        addressDetails.put("postalCode", params.get("postalCode"));
        addressDetails.put("country", params.get("country"));
        address.add(addressDetails);
        body.put("address", address);

        //marital status
        JSONObject maritalStatus = new JSONObject();
        maritalStatus.put("text", params.get("maritalStatus"));
        body.put("maritalStatus", maritalStatus);

        //System.out.println(body);
        String response = HTTPRequest.CREATE(auth, patientUrl, "POST", body.toString());
        List patientResults = new ArrayList();
        patientResults = List.of(response.split("/"));
        return String.valueOf(patientResults.get(1));
    }

    public static Map<String, Value> readCoverage(String url, String auth, String fhirID) throws IOException, ParseException {
        String coverageUrl = url + "api/FHIR/R4/Coverage/" + fhirID;
        Map<String, Value> resMap = new LinkedHashMap<>();

        String coverageResponse = HTTPRequest.HttpGetWithParams(coverageUrl, auth, null);
        Object obj = new JSONParser().parse(coverageResponse);
        JSONObject response = (JSONObject) obj;
        //System.out.println(response);
        //subscriber
        JSONObject subscriber = (JSONObject) response.get("subscriber");
        resMap.put("subscriber", new StringValue(String.valueOf(subscriber.get("display"))));
        String patientID = (String) subscriber.get("reference");
        String patientFHIRId = patientID.split("/")[1];
        resMap.put("patientFHIRId", new StringValue(patientFHIRId));
        //coverage period
        JSONObject period = (JSONObject) response.get("period");
        resMap.put("coverageStart", new StringValue(String.valueOf(period.get("start"))));
        resMap.put("coverageEnd", new StringValue(String.valueOf(period.get("end"))));
        //payer
        JSONArray payors = (JSONArray) response.get("payor");
        JSONObject payer = (JSONObject) payors.get(0); //only getting first payor right now...
        resMap.put("payer", new StringValue(String.valueOf(payer.get("display"))));
        //beneficiary
        JSONObject beneficiary = (JSONObject) response.get("beneficiary");
        resMap.put("beneficiary", new StringValue(String.valueOf(beneficiary.get("display"))));
        //subsciber ID
        resMap.put("subscriberID", new StringValue(String.valueOf(response.get("subscriberId"))));
        //relationship
        JSONObject relationship = (JSONObject) response.get("relationship");
        resMap.put("relationship", new StringValue(String.valueOf(relationship.get("text"))));
        //Patient FHIR ID

        return resMap;
    }
}
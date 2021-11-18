package com.automationanywhere.botcommand.samples.Utils;

import com.automationanywhere.bot.service.Bot;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import org.apache.velocity.runtime.directive.Parse;
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
        try {
            Object obj = new JSONParser().parse(searchResponse);
            JSONObject response = (JSONObject) obj;
            resMap.put("results", (new StringValue(String.valueOf(response.get("total")))));
            JSONArray entries = (JSONArray) response.get("entry");
            if (String.valueOf(response.get("total")).equals("0")) {
                //The search didn't return any results, so report back the message
                JSONObject firstEntry = (JSONObject) entries.get(0);
                JSONObject resource = (JSONObject) firstEntry.get("resource");
                JSONArray issue = (JSONArray) resource.get("issue");
                JSONObject firstIssue = (JSONObject) issue.get(0);
                JSONObject details = (JSONObject) firstIssue.get("details");
                String text = (String) details.get("text");
                resMap.put("message", new StringValue(text));
            } else {
                //Code to parse entries/list of results
                for (int i = 0; i < entries.size(); i++) {
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
                    for (int j = 0; j < addresses.size(); j++) {
                        JSONObject address = (JSONObject) addresses.get(j);
                        if (String.valueOf(address.get("use")).equals("home")) {
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
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain patient info or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
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
        try {
            Object obj = new JSONParser().parse(coverageResponse);
            JSONObject response = (JSONObject) obj;
            //System.out.println(response);
            //subscriber
            JSONObject subscriber = (JSONObject) response.get("subscriber");
            resMap.put("subscriber", new StringValue(String.valueOf(subscriber.get("display"))));
            if (subscriber.containsKey("reference")) {
                String patientID = (String) subscriber.get("reference");
                String patientFHIRId = patientID.split("/")[1];
                resMap.put("patientFHIRId", new StringValue(patientFHIRId));
            } else {
                resMap.put("patientFHIRId", new StringValue("No Value"));
            }
            //coverage period
            JSONObject period = (JSONObject) response.get("period");
            resMap.put("coverageStart", new StringValue(String.valueOf(period.get("start"))));
            resMap.put("coverageEnd", new StringValue(String.valueOf(period.get("end"))));
            //payer
            JSONArray payors = (JSONArray) response.get("payor");
            if (payors.size() >= 1) {
                JSONObject payer = (JSONObject) payors.get(0); //only getting first payor right now...
                resMap.put("payer", new StringValue(String.valueOf(payer.get("display"))));
            } else {
                resMap.put("payer", new StringValue("No value for payer found"));
            }
            //beneficiary
            JSONObject beneficiary = (JSONObject) response.get("beneficiary");
            resMap.put("beneficiary", new StringValue(String.valueOf(beneficiary.get("display"))));
            //subscriber ID
            resMap.put("subscriberID", new StringValue(String.valueOf(response.get("subscriberId"))));
            //relationship
            if (response.containsKey("relationship")) {
                JSONObject relationship = (JSONObject) response.get("relationship");
                resMap.put("relationship", new StringValue(String.valueOf(relationship.get("text"))));
            } else {
                resMap.put("relationship", new StringValue("No Value"));
            }
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain coverage info or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }

        return resMap;
    }

    public static List<Value> searchCoverage(String url, String auth, String patientID, String beneficiaryID) throws IOException, ParseException {
        String coverageUrl = url + "api/FHIR/R4/Coverage?patient=" + patientID + "&beneficiary=" + beneficiaryID;
        List<Value> coverageIds = new ArrayList<>();

        String coverageResponse = HTTPRequest.HttpGetWithParams(coverageUrl, auth, null);
        try {
            Object obj = new JSONParser().parse(coverageResponse);
            JSONObject response = (JSONObject) obj;
            //System.out.println(response);
            //entry list
            JSONArray entries = (JSONArray) response.get("entry");
            for (int i = 0; i < entries.size(); i++) {
                JSONObject entry = (JSONObject) entries.get(i);
                JSONObject resource = (JSONObject) entry.get("resource");
                coverageIds.add(new StringValue(String.valueOf(resource.get("id")))); //add StringValue to list
            }
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain coverage IDs or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }
        return coverageIds;
    }

    public static List<Value> searchHealthConcern(String url, String auth, String patientID, String status) throws IOException, ParseException {
        String flagUrl = url + "api/FHIR/R4/Flag?category=health-concern&patient="+ patientID + "&status=" + status;
        List<Value> healthConcernIds = new ArrayList<>();

        String coverageResponse = HTTPRequest.HttpGetWithParams(flagUrl, auth, null);
        try {
            Object obj = new JSONParser().parse(coverageResponse);
            JSONObject response = (JSONObject) obj;
            //entry list
            JSONArray entries = (JSONArray) response.get("entry");
            for (int i = 0; i < entries.size(); i++) {
                JSONObject entry = (JSONObject) entries.get(i);
                JSONObject resource = (JSONObject) entry.get("resource");
                healthConcernIds.add(new StringValue(String.valueOf(resource.get("id")))); //add StringValue to list
            }
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain health concern IDs or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }
        return healthConcernIds;
    }

    public static Map<String, Value> readHealthConcern(String url, String auth, String fhirID) throws IOException, ParseException {
        String allergyUrl = url + "api/FHIR/R4/Flag/" + fhirID;
        Map<String, Value> resMap = new LinkedHashMap<>();

        String coverageResponse = HTTPRequest.HttpGetWithParams(allergyUrl, auth, null);
        try {
            Object obj = new JSONParser().parse(coverageResponse);
            JSONObject response = (JSONObject) obj;
            //System.out.println(response);
            //description
            JSONObject code = (JSONObject) response.get("code");
            resMap.put("description", new StringValue(String.valueOf(code.get("text"))));
            //category
            JSONArray category = (JSONArray) response.get("category");
            JSONObject cat = (JSONObject) category.get(0);
            resMap.put("category", new StringValue(String.valueOf(cat.get("text"))));
            //patient
            JSONObject patient = (JSONObject) response.get("subject");
            resMap.put("patient", new StringValue(String.valueOf(patient.get("display"))));
            //period
            JSONObject onset = (JSONObject) response.get("period");
            resMap.put("period", new StringValue(String.valueOf(onset.get("start"))));
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain health concern info or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }

        return resMap;
    }

    public static List<Value> searchAllergyIntolerance (String url, String auth, String patientID) throws IOException, ParseException {
        String allergyURL = url + "api/FHIR/R4/AllergyIntolerance?patient=" + patientID + "&clinical-status=active";
        List<Value> allergyIds = new ArrayList<>();

        String response = HTTPRequest.HttpGetWithParams(allergyURL, auth, null);
        try{
            Object obj = new JSONParser().parse(response);
            JSONObject json = (JSONObject) obj;
            //System.out.println(response);
            //entry list
            JSONArray entries = (JSONArray) json.get("entry");
            for (int i = 0; i < entries.size(); i++) {
                JSONObject entry = (JSONObject) entries.get(i);
                JSONObject resource = (JSONObject) entry.get("resource");
                allergyIds.add(new StringValue(String.valueOf(resource.get("id")))); //add StringValue to list
            }
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain any allergy info. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }
        return allergyIds;
    }

    public static Map<String, Value> readAllergyIntolerance(String url, String auth, String fhirID) throws IOException, ParseException {
        String allergyUrl = url + "api/FHIR/R4/AllergyIntolerance/" + fhirID;
        Map<String, Value> resMap = new LinkedHashMap<>();

        String coverageResponse = HTTPRequest.HttpGetWithParams(allergyUrl, auth, null);
        try {
            Object obj = new JSONParser().parse(coverageResponse);
            JSONObject response = (JSONObject) obj;
            //System.out.println(response);
            //description
            JSONObject code = (JSONObject) response.get("code");
            resMap.put("description", new StringValue(String.valueOf(code.get("text"))));
            //category
            JSONArray category = (JSONArray) response.get("category");
            String cat = (String) category.get(0);
            resMap.put("category", new StringValue(cat));
            //patient
            JSONObject patient = (JSONObject) response.get("patient");
            resMap.put("patient", new StringValue(String.valueOf(patient.get("display"))));
            //onsetPeriod
            JSONObject onset = (JSONObject) response.get("onsetPeriod");
            resMap.put("onset", new StringValue(String.valueOf(onset.get("start"))));
            //reaction
            if (response.containsKey("reaction")) {
                JSONArray reaction = (JSONArray) response.get("reaction");
                JSONObject manifestation = (JSONObject) reaction.get(0);
                resMap.put("reaction", new StringValue(String.valueOf(manifestation.get("description"))));
            } else {
                resMap.put("reaction", new StringValue("No reaction description provided"));
            }
        } catch (Exception e) {
            throw new BotCommandException("The response from the FHIR server didn't contain allergy info or responded unexpectedly. Check your inputs and ensure " +
                    "your app is authorized to access this resource. Error: " + e);
        }
        return resMap;
    }

}
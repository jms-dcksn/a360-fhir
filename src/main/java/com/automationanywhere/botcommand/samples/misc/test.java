package com.automationanywhere.botcommand.samples.misc;

import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.samples.Utils.FHIRActions;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;


import com.automationanywhere.botcommand.samples.Utils.PemUtils;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.model.User;
import com.mypurecloud.sdk.v2.model.UserMe;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.security.Key;


public class test {

    public static void main(String[] args) throws Exception {

        String clientId = "9dca6746-d56e-4d88-9736-ad94a608ddc1";
        String url = "https://fhir.epic.com/interconnect-fhir-oauth/";

        String authURL = url + "oauth2/token";
        Instant now = Instant.now();

        // create  instance object
        // print Instant Value
        //System.out.println("Instant: " + now);

        // get epochValue using getEpochSecond
        long epochValue = now.getEpochSecond() + 240;
        int epochInt = (int) epochValue;
        //System.out.println(epochInt);


        PrivateKey key = PemUtils.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File("C:\\Users\\jamesdickson\\Documents\\FHIR\\PEM\\privatekey.pem"));

        String jwt = Jwts.builder()
                .claim("iss",clientId)
                .claim("sub",clientId)
                .claim("aud", authURL)
                .claim("jti", UUID.randomUUID().toString())
                .claim("exp", epochInt)
                .signWith(key, SignatureAlgorithm.RS384)
                .compact();
        //System.out.print(jwt);

        String response = HTTPRequest.oAuthMethod(authURL, jwt);
        Object obj = new JSONParser().parse(response);
        JSONObject jsonResponse = (JSONObject) obj;
        String accessToken = (String) jsonResponse.get("access_token");

        //?family=Lin&given=Derrick&birthdate=1973-06-03"
        String patientUrl = url + "api/FHIR/R4/Patient";
        String body = "{\"resourceType\":\"Patient\"," +
                "\"identifier\":[{\"use\":\"usual\",\"system\":\"urn:oid:2.16.840.1.113883.4.1\",\"value\":\"161-71-1711\"}]," +
                "\"active\":\"true\",\"name\":[{\"use\":\"usual\",\"family\":\"FHIRcreate\",\"given\":[\"Elle\"]}]," +
                "\"telecom\":[{\"system\":\"phone\",\"value\":\"321-555-1234\",\"use\":\"home\"},{\"system\":\"email\",\"value\":\"elle.FHIRcreate@fhirtest.edu\"}]," +
                "\"gender\":\"female\",\"birthDate\":\"1988-07-30\",\"address\":[{\"use\":\"home\",\"line\":[\"100 Milky Way\"]," +
                "\"city\":\"Verona\",\"state\":\"WI\",\"postalCode\":\"53593\",\"country\":\"USA\"}]," +
                "\"maritalStatus\":{\"text\":\"Single\"}," +
                "\"generalPractitioner\":[{\"display\":\"Physician Family Medicine, MD\",\"reference\":\"eM5CWtq15N0WJeuCet5bJlQ3\"}]," +
                "\"extension\":[{\"url\":\"http://open.epic.com/FHIR/STU3/StructureDefinition/patient-preferred-provider-language\"," +
                "\"valueCodeableConcept\":{\"coding\":[{\"system\":\"urn:oid:2.16.840.1.113883.6.99\",\"code\":\"ja\"}],\"text\":\"Japanese\"}}," +
                "{\"url\":\"http://open.epic.com/FHIR/STU3/StructureDefinition/patient-preferred-provider-sex\",\"valueCode\":\"female\"}]}";

        String patient = HTTPRequest.SEND("Bearer " + accessToken, patientUrl, "POST", body);
        System.out.println(patient);

        /*Map<String, String> params = new LinkedHashMap<>();
        params.put("address", "");
        params.put("address-city", "");
        params.put("address-postalcode", "");
        params.put("address-state", "");
        params.put("family", "");
        params.put("given", "");
        params.put("birthdate", "");
        params.put("gender", "");
        params.put("legal-sex", "");
        params.put("partner-name", "");
        params.put("telecom", "");
        params.put("_id", "eq081-VQEgP8drUUqCWzHfw3");

        Map<String, Value> resMap = new LinkedHashMap<>();

        String searchResponse = HTTPRequest.HttpGetWithParams(patientUrl, "Bearer " + accessToken, params);
        Object obj2 = new JSONParser().parse(searchResponse);
        JSONObject response2 = (JSONObject) obj2;
        resMap.put("results", (new StringValue(String.valueOf(response2.get("total")))));
        JSONArray entries = (JSONArray) response2.get("entry");
        if(String.valueOf(response2.get("total")).equals("0")){
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
        System.out.println(resMap);*/
    }
}




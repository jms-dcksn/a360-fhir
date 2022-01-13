package com.automationanywhere.botcommand.samples.misc;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.*;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.samples.Utils.FHIRActions;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;

import java.io.File;


import com.automationanywhere.botcommand.samples.Utils.PemUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class test {

    public static void main(String[] args) throws Exception {


        //***************EPIC Sandbox params**********************
        String clientId = "148bd214-e264-497e-a768-336a16baceba";
        String url = "https://fhir.epic.com/interconnect-fhir-oauth/";

        String authURL = url + "oauth2/token";

        //*****************JWT Generation*****************************
        Instant now = Instant.now();

        // create  instance object
        // print Instant Value
        //System.out.println("Instant: " + now);

        // get epochValue using getEpochSecond
        long epochValue = now.getEpochSecond() + 240;
        int epochInt = (int) epochValue;
        //System.out.println(epochInt);


        PrivateKey key = PemUtils.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File("C:\\Users\\jamesdickson\\Documents\\FHIR\\STU3\\privatekey.pem"));

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
        //System.out.println(accessToken);

        //String allergyinfo = HTTPRequest.HttpGetWithParams(url + "api/FHIR/R4/AllergyIntolerance?patient=e63wRTbPfr1p8UW81d8Seiw3&clinical-status=active", "Bearer " + accessToken, null);

        //List<Value> map = FHIRActions.searchHealthConcern(url, "Bearer " + accessToken, "e63wRTbPfr1p8UW81d8Seiw3", "active");
        String apptUrl = url + "api/FHIR/STU3/Appointment/$find";
        String apptBookUrl = url + "api/FHIR/STU3/Appointment/$book";
        //Map<String, Value> resMap = FHIRActions.readHealthConcern(url, "Bearer " + accessToken, "erEwpagBquISLpnVVsNdUVeXy4Ik1.oYWPByJ4WN9FxY3");
        String apptBody = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"patient\",\"resource\":" +
                "{\"resourceType\":\"Patient\",\"active\":true," +
                "\"name\":[{\"use\":\"usual\",\"text\":\"Correct Professional Billing\",\"family\":\"Professional Billing\",\"given\":[\"Correct\"]}]," +
                "\"telecom\":[{\"system\":\"phone\",\"value\":\"608-271-9000\",\"use\":\"home\"},{\"system\":\"phone\",\"value\":\"608-271-9000\",\"use\":\"work\"}]," +
                "\"gender\":\"male\",\"birthDate\":\"1983-06-08\",\"deceasedBoolean\":false," +
                "\"address\":[{\"use\":\"home\",\"line\":[\"1979 Milky Way\"],\"city\":\"VERONA\",\"district\":\"DANE\",\"state\":\"WI\",\"postalCode\":\"53593\",\"country\":\"US\"}]," +
                "\"maritalStatus\":{\"text\":\"Single\"}," +
                "\"generalPractitioner\":[{\"reference\":\"https://apporchard.epic.com/interconnect-aocurprd-oauth/api/FHIR/STU3/Practitioner/eM5CWtq15N0WJeuCet5bJlQ3\",\"display\":\"Physician Family Medicine, MD\"}]," +
                "\"managingOrganization\":{\"reference\":\"https://apporchard.epic.com/interconnect-aocurprd-oauth/api/FHIR/STU3/Organization/enRyWnSP963FYDpoks4NHOA3\"," +
                "\"display\":\"Epic Hospital System\"}}}," +
/*appt Time*/   "{\"name\":\"startTime\",\"valueDateTime\":\"2022-2-22T06:00:00Z\"},{\"name\":\"endTime\",\"valueDateTime\":\"2022-2-22T22:00:00Z\"}," +
                "{\"name\":\"service-type\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"urn:oid:1.2.840.114350.1.13.0.1.7.3.808267.11\",\"code\":\"95014\",\"display\":\"Office Visit\"}]}}," +
                "{\"name\":\"indications\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"urn:oid:2.16.840.1.113883.6.96\",\"code\":\"46866001\",\"display\":\"Fracture of lower limb (disorder)\"}," +
                "{\"system\":\"urn:oid:2.16.840.1.113883.6.90\",\"code\":\"S82.90XA\",\"display\":\"Broken arm\"}," +
                "{\"system\":\"urn:oid:1.2.840.114350.1.13.861.1.7.2.696871\",\"code\":\"121346631\",\"display\":\"Broken arm\"}]," +
                "\"text\":\"Broken arm\"}},{\"name\":\"location-reference\",\"valueReference\":{\"reference\":\"https://apporchard.epic.com/interconnect-aocurprd-oauth/api/FHIR/STU3/Location/e4W4rmGe9QzuGm2Dy4NBqVc0KDe6yGld6HW95UuN-Qd03\"}}]}";

        String apptBookBody = "{\"resourceType\": \"Parameters\",\"parameter\": [{\"name\": \"patient\",\"valueIdentifier\": " +
                "{\"value\": \"eq081-VQEgP8drUUqCWzHfw3\"}},{\"name\": \"appointment\",\"valueIdentifier\": {\"value\": \"epoyNBKvTk8R8G9Osh5eNt5VC1bU2ylkfRER9tbxrKlPZfvb-9s7BMYVbVWlosLdM3\"}},{\n" +
                "\"name\": \"appointmentNote\",\"valueString\": \"Note text containing info related to the appointment.\"}]}";

        //String apptFindResponse = HTTPRequest.Request(apptUrl,"Bearer " + accessToken,"POST", apptBody);
        //DictionaryValue resMap = FHIRActions.bookAppointment(url, "Bearer " + accessToken, "eCIBvgwA6kIwfp1ZpdDIyd8svmrafxw6L3mKwmh0pCfzhTdx7zWzxDl08stOsc-nz3");
        //System.out.println(apptFindResponse);
        //System.out.println(resMap.get());

        Map<String, String> params = new LinkedHashMap<>();
        //params.put("ssn", "161-96-0987");
        params.put("street", "7324 Roosevelt Ave");
        params.put("city", "INDIANAPOLIS");
        params.put("postalCode", "46201");
        params.put("state", "IN");
        params.put("country", "USA");
        params.put("family", "Lin");
        params.put("given", "Derrick");
        params.put("birthDate", "1973-06-03");
        params.put("gender", "male");
        params.put("phone", "785-555-5555");
        params.put("maritalStatus", "Married");
        String patientResource = FHIRActions.patientResource(params);

        String startTime = "2022-2-22T06:00:00Z";
        String endTime = "2022-2-22T22:00:00Z";

        String apptBody2 = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"patient\",\"resource\":" + patientResource +
                "}," +
                /*appt Time*/   "{\"name\":\"startTime\",\"valueDateTime\":\"" + startTime + "\"},{\"name\":\"endTime\",\"valueDateTime\":\"" + endTime + "\"}," +
                "{\"name\":\"service-type\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"urn:oid:1.2.840.114350.1.13.0.1.7.3.808267.11\",\"code\":\"95014\",\"display\":\"Office Visit\"}]}}," +
                "{\"name\":\"indications\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"urn:oid:2.16.840.1.113883.6.96\",\"code\":\"46866001\",\"display\":\"Fracture of lower limb (disorder)\"}," +
                "{\"system\":\"urn:oid:2.16.840.1.113883.6.90\",\"code\":\"S82.90XA\",\"display\":\"Broken arm\"}," +
                "{\"system\":\"urn:oid:1.2.840.114350.1.13.861.1.7.2.696871\",\"code\":\"121346631\",\"display\":\"Broken arm\"}]," +
                "\"text\":\"Broken arm\"}},{\"name\":\"location-reference\",\"valueReference\":{\"reference\":\"https://apporchard.epic.com/interconnect-aocurprd-oauth/api/FHIR/STU3/Location/e4W4rmGe9QzuGm2Dy4NBqVc0KDe6yGld6HW95UuN-Qd03\"}}]}";
        //*******************************************************
        //System.out.println(apptBody2);

        String apptFindResponse = HTTPRequest.Request(apptUrl,"Bearer " + accessToken,"POST", apptBody2);
        System.out.println(apptFindResponse);


        /*String fhir_id = FHIRActions.createPatient(url, "Bearer " + accessToken, params);
        System.out.println(fhir_id);*/

        /*Map<String, String> params = new LinkedHashMap<>();
        params.put("address", "");
        params.put("address-city", "");
        params.put("address-postalcode", "");
        params.put("address-state", "");
        params.put("family", "Lin");
        params.put("given", "Derrick");
        params.put("birthDate", "1981-09-08");
        params.put("gender", "male");
        params.put("legal-sex", "");
        params.put("partner-name", "");
        params.put("telecom", "");
        params.put("ssn", "111-23-3335");*/

        /*JSONObject body = new JSONObject();
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
        JSONArray name = new JSONArray();
        JSONObject nameDetails = new JSONObject();
        nameDetails.put("use", "usual");
        nameDetails.put("family", params.get("family"));
        List<String> givenName = new ArrayList<>();
        givenName.add(params.get("given"));
        nameDetails.put("given", givenName);
        name.add(nameDetails);
        body.put("name", name);
        //
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

        body.put("gender", params.get("gender"));
        body.put("birthDate", params.get("birthDate"));

        System.out.println(body.toString());*/


    }
}




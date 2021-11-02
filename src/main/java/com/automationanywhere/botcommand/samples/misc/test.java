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
        Map<String, Value> coverage = FHIRActions.readCoverage(url, "Bearer " + accessToken, "eS72vnDj387lBv1vJqjUKhGFkkNw3RVMhZzABgnZ0kwk3");
        System.out.println(coverage);



        /*Map<String, String> params = new LinkedHashMap<>();
        params.put("ssn", "161-96-0987");
        params.put("street", "321 Nora");
        params.put("city", "Argyle");
        params.put("postalCode", "76225");
        params.put("state", "TX");
        params.put("country", "USA");
        params.put("family", "FHIRcreate");
        params.put("given", "Bill");
        params.put("birthDate", "1971-09-08");
        params.put("gender", "male");
        params.put("phone", "555-345-5654");
        params.put("email", "bduncan@healthcare.com");
        params.put("maritalStatus", "Single");*/

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




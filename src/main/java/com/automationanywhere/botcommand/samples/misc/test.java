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

        Map<String, String> params = new LinkedHashMap<>();
        params.put("address", "");
        params.put("address-city", "");
        params.put("address-postalcode", "");
        params.put("address-state", "");
        params.put("family", "Lin");
        params.put("given", "Derrick");
        params.put("birthdate", "1973-06-03");
        params.put("gender", "");
        params.put("legal-sex", "");
        params.put("partner-name", "");
        params.put("telecom", "");

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
                resMap.put("gender", new StringValue(String.valueOf(resource.get("gender"))));
                JSONObject maritalStatus = (JSONObject) resource.get("maritalStatus");
                resMap.put("maritalStatus", new StringValue(String.valueOf(maritalStatus.get("text"))));
            }
        }
        System.out.println(resMap);
    }
}




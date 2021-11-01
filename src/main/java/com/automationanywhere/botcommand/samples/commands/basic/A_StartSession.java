/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */

/**
 * @author James Dickson
 */
package com.automationanywhere.botcommand.samples.commands.basic;

import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.Utils.FHIRServer;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.automationanywhere.botcommand.samples.Utils.PemUtils;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.core.security.SecureString;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author James Dickson
 */
@BotCommand
@CommandPkg(label = "Start Session",
		description = "Starts Session with FHIR APIs using SMART Backend Services Authorization",
		icon = "fhir2.svg",
		name = "startFHIRsession",
		node_label = "Start Session {{sessionName}}",
		group_label="Admin",
		comment = true
		)
public class A_StartSession {

	@Sessions
	private Map<String, Object> sessionMap;

	@Execute
	public void execute(
			@Idx(index = "1", type = TEXT)
			@Pkg(label = "Start Session", default_value_type = STRING, default_value = "Default")
			@NotEmpty String sessionName,
			@Idx(index = "2", type = TEXT) @Pkg(label = "URL", description = "https://{hostname}/{instance}/ " +
					"e.g. for EPIC sandbox https://fhir.epic.com/interconnect-fhir-oauth/") @NotEmpty String url,
			@Idx(index = "3", type = CREDENTIAL) @Pkg(label = "Client ID") @NotEmpty SecureString clientId,
			@Idx(index = "4", type = AttributeType.FILE) @Pkg(label = "Private Key File (.pem)", description="Private Key file associated with the RSA public key " +
					"used to register the application with the FHIR server") @NotEmpty String pemFile
	) throws GeneralSecurityException, IOException {
		String ins_clientId = clientId.getInsecureString();
		String response= "";
		String accessToken= "";
		String authURL = url + "oauth2/token";
		Instant now = Instant.now();
		long epochValue = now.getEpochSecond() + 240;
		int epochInt = (int) epochValue;
		if (!sessionMap.containsKey(sessionName))
			try {
				PrivateKey key = PemUtils.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File(pemFile));
				String jwt = Jwts.builder()
						.claim("iss",ins_clientId)
						.claim("sub",ins_clientId)
						.claim("aud", authURL)
						.claim("jti", UUID.randomUUID().toString())
						.claim("exp", epochInt)
						.signWith(key, SignatureAlgorithm.RS384)
						.compact();
				response = HTTPRequest.oAuthMethod(authURL, jwt);
				Object obj = new JSONParser().parse(response);
				JSONObject jsonResponse = (JSONObject) obj;
				accessToken = (String) jsonResponse.get("access_token");

			} catch (Exception e){
				throw new BotCommandException("The response from the server did not contain an access token. Please check your credentials and ensure your instance as awake. Exception message: " + response + e);
			}
		FHIRServer fhirServer = new FHIRServer(url, accessToken);
		sessionMap.put(sessionName, fhirServer);
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

}

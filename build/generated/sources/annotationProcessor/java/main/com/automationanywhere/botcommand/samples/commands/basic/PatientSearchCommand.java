package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PatientSearchCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(PatientSearchCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    PatientSearch command = new PatientSearch();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("help") && parameters.get("help") != null && parameters.get("help").get() != null) {
      convertedParameters.put("help", parameters.get("help").get());
      if(convertedParameters.get("help") !=null && !(convertedParameters.get("help") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","help", "String", parameters.get("help").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("sessionName") && parameters.get("sessionName") != null && parameters.get("sessionName").get() != null) {
      convertedParameters.put("sessionName", parameters.get("sessionName").get());
      if(convertedParameters.get("sessionName") !=null && !(convertedParameters.get("sessionName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sessionName", "String", parameters.get("sessionName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("sessionName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sessionName"));
    }

    if(parameters.containsKey("address") && parameters.get("address") != null && parameters.get("address").get() != null) {
      convertedParameters.put("address", parameters.get("address").get());
      if(convertedParameters.get("address") !=null && !(convertedParameters.get("address") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","address", "String", parameters.get("address").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("city") && parameters.get("city") != null && parameters.get("city").get() != null) {
      convertedParameters.put("city", parameters.get("city").get());
      if(convertedParameters.get("city") !=null && !(convertedParameters.get("city") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","city", "String", parameters.get("city").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("state") && parameters.get("state") != null && parameters.get("state").get() != null) {
      convertedParameters.put("state", parameters.get("state").get());
      if(convertedParameters.get("state") !=null && !(convertedParameters.get("state") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","state", "String", parameters.get("state").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("postal") && parameters.get("postal") != null && parameters.get("postal").get() != null) {
      convertedParameters.put("postal", parameters.get("postal").get());
      if(convertedParameters.get("postal") !=null && !(convertedParameters.get("postal") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","postal", "String", parameters.get("postal").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("family") && parameters.get("family") != null && parameters.get("family").get() != null) {
      convertedParameters.put("family", parameters.get("family").get());
      if(convertedParameters.get("family") !=null && !(convertedParameters.get("family") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","family", "String", parameters.get("family").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("given") && parameters.get("given") != null && parameters.get("given").get() != null) {
      convertedParameters.put("given", parameters.get("given").get());
      if(convertedParameters.get("given") !=null && !(convertedParameters.get("given") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","given", "String", parameters.get("given").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("birthdate") && parameters.get("birthdate") != null && parameters.get("birthdate").get() != null) {
      convertedParameters.put("birthdate", parameters.get("birthdate").get());
      if(convertedParameters.get("birthdate") !=null && !(convertedParameters.get("birthdate") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","birthdate", "String", parameters.get("birthdate").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("gender") && parameters.get("gender") != null && parameters.get("gender").get() != null) {
      convertedParameters.put("gender", parameters.get("gender").get());
      if(convertedParameters.get("gender") !=null && !(convertedParameters.get("gender") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","gender", "String", parameters.get("gender").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("telecom") && parameters.get("telecom") != null && parameters.get("telecom").get() != null) {
      convertedParameters.put("telecom", parameters.get("telecom").get());
      if(convertedParameters.get("telecom") !=null && !(convertedParameters.get("telecom") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","telecom", "String", parameters.get("telecom").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("fhir_id") && parameters.get("fhir_id") != null && parameters.get("fhir_id").get() != null) {
      convertedParameters.put("fhir_id", parameters.get("fhir_id").get());
      if(convertedParameters.get("fhir_id") !=null && !(convertedParameters.get("fhir_id") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","fhir_id", "String", parameters.get("fhir_id").get().getClass().getSimpleName()));
      }
    }

    command.setSessionMap(sessionMap);
    try {
      Optional<Value> result =  Optional.ofNullable(command.action((String)convertedParameters.get("help"),(String)convertedParameters.get("sessionName"),(String)convertedParameters.get("address"),(String)convertedParameters.get("city"),(String)convertedParameters.get("state"),(String)convertedParameters.get("postal"),(String)convertedParameters.get("family"),(String)convertedParameters.get("given"),(String)convertedParameters.get("birthdate"),(String)convertedParameters.get("gender"),(String)convertedParameters.get("telecom"),(String)convertedParameters.get("fhir_id")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}

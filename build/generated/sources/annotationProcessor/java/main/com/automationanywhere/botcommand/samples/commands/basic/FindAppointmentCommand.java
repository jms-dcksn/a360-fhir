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

public final class FindAppointmentCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(FindAppointmentCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.entrySet().stream().filter(en -> !Arrays.asList( new String[] {}).contains(en.getKey()) && en.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    FindAppointment command = new FindAppointment();
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

    if(parameters.containsKey("startTime") && parameters.get("startTime") != null && parameters.get("startTime").get() != null) {
      convertedParameters.put("startTime", parameters.get("startTime").get());
      if(convertedParameters.get("startTime") !=null && !(convertedParameters.get("startTime") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","startTime", "String", parameters.get("startTime").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("startTime") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","startTime"));
    }

    if(parameters.containsKey("endTime") && parameters.get("endTime") != null && parameters.get("endTime").get() != null) {
      convertedParameters.put("endTime", parameters.get("endTime").get());
      if(convertedParameters.get("endTime") !=null && !(convertedParameters.get("endTime") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","endTime", "String", parameters.get("endTime").get().getClass().getSimpleName()));
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

    if(parameters.containsKey("street") && parameters.get("street") != null && parameters.get("street").get() != null) {
      convertedParameters.put("street", parameters.get("street").get());
      if(convertedParameters.get("street") !=null && !(convertedParameters.get("street") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","street", "String", parameters.get("street").get().getClass().getSimpleName()));
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

    if(parameters.containsKey("country") && parameters.get("country") != null && parameters.get("country").get() != null) {
      convertedParameters.put("country", parameters.get("country").get());
      if(convertedParameters.get("country") !=null && !(convertedParameters.get("country") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","country", "String", parameters.get("country").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("gender") && parameters.get("gender") != null && parameters.get("gender").get() != null) {
      convertedParameters.put("gender", parameters.get("gender").get());
      if(convertedParameters.get("gender") !=null && !(convertedParameters.get("gender") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","gender", "String", parameters.get("gender").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("maritalStatus") && parameters.get("maritalStatus") != null && parameters.get("maritalStatus").get() != null) {
      convertedParameters.put("maritalStatus", parameters.get("maritalStatus").get());
      if(convertedParameters.get("maritalStatus") !=null && !(convertedParameters.get("maritalStatus") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","maritalStatus", "String", parameters.get("maritalStatus").get().getClass().getSimpleName()));
      }
    }

    if(parameters.containsKey("phone") && parameters.get("phone") != null && parameters.get("phone").get() != null) {
      convertedParameters.put("phone", parameters.get("phone").get());
      if(convertedParameters.get("phone") !=null && !(convertedParameters.get("phone") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","phone", "String", parameters.get("phone").get().getClass().getSimpleName()));
      }
    }

    command.setSessionMap(sessionMap);
    try {
      Optional<Value> result =  Optional.ofNullable(command.action((String)convertedParameters.get("help"),(String)convertedParameters.get("sessionName"),(String)convertedParameters.get("startTime"),(String)convertedParameters.get("endTime"),(String)convertedParameters.get("family"),(String)convertedParameters.get("given"),(String)convertedParameters.get("birthdate"),(String)convertedParameters.get("street"),(String)convertedParameters.get("city"),(String)convertedParameters.get("state"),(String)convertedParameters.get("postal"),(String)convertedParameters.get("country"),(String)convertedParameters.get("gender"),(String)convertedParameters.get("maritalStatus"),(String)convertedParameters.get("phone")));
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

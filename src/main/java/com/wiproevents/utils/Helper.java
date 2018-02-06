package com.wiproevents.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.documentdb.core.query.Criteria;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.aop.LogAspect;
import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.entities.NewPassword;
import com.wiproevents.entities.User;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.security.CustomUserDetails;
import com.wiproevents.security.UserAuthentication;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class provides help methods used in this application.
 */
public class Helper {
    /**
     * Mysql ST_Distance_Sphere will return distance in meters so we have to multiply 1000 to compare as kilometers.
     */
    public static final String KILOMETERS = "1000";

    /**
     * Represents the mysql function to calculate distance between two points.
     */
    private static final String CALCULATE_DISTANCE = "calculate_distance";

    /**
     * Represents the classes that there is no need to log.
     */
    private static final List<Class> NOLOGS = Arrays.asList(HttpServletRequest.class,
            HttpServletResponse.class, ModelAndView.class, NewPassword.class, MultipartFile[].class, ResponseEntity.class);

    /**
     * The object mapper.
     */
    public static final ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();

    /**
     * Represents the utf8 encoding fullName.
     */
    public static final String UTF8 = "UTF-8";

    /**
     * The private constructor.
     */
    private Helper() { }

    /**
     * It checks whether a given object is null.
     *
     * @param object the object to be checked
     * @param name the fullName of the object, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the object is null
     */
    public static void checkNull(Object object, String name) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(String.format("%s should be provided", name));
        }
    }

    /**
     * It checks whether a given identifiable entity is valid.
     *
     * @param object the object to be checked
     * @param name the fullName of the object, used in the exception message
     * @param <T> the entity class
     * @throws IllegalArgumentException the exception thrown when the object is null or id of object is not positive
     */
    public static <T extends IdentifiableEntity> void checkEntity(T object, String name)
            throws IllegalArgumentException {
        checkNull(object, name);
        checkNullOrEmpty(object.getId(), name + ".id");
    }

    /**
     * It checks whether a given string is valid email address.
     *
     * @param str the string to be checked
     * @return true if a given string is valid email address
     */
    public static boolean isEmail(String str) {
        return new EmailValidator().isValid(str, null);
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @return true if a given string is null or empty
     * @throws IllegalArgumentException throws if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) throws IllegalArgumentException {
        return str == null || str.trim().isEmpty();
    }

    /**
     * It checks whether a given string is null or empty.
     *
     * @param str the string to be checked
     * @param name the fullName of the string, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the given string is null or empty
     */
    public static void checkNullOrEmpty(String str, String name) throws IllegalArgumentException {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(String.format("%s should be valid string(not null and not empty)", name));
        }
    }

    /**
     * Check if the value is positive.
     *
     * @param value the value to be checked
     * @param name the fullName of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not positive
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s should be positive", name));
        }
    }

    /**
     * Check if the value is valid email.
     *
     * @param value the value to be checked
     * @param name the fullName of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not email
     */
    public static void checkEmail(String value, String name) {
        checkNullOrEmpty(value, name);
        if (!isEmail(value)) {
            throw new IllegalArgumentException(String.format("%s should be valid email address", name));
        }
    }

    /**
     * Check if the configuration state is true.
     *
     * @param state the state
     * @param message the error message if the state is not true
     * @throws ConfigurationException if the state is not true
     */
    public static void checkConfigState(boolean state, String message) {
        if (!state) {
            throw new ConfigurationException(message);
        }
    }

    /**
     * Check if the configuration is null or not.
     *
     * @param object the object
     * @param name the fullName
     * @throws ConfigurationException if the configuration is null
     */
    public static void checkConfigNotNull(Object object, String name) {
        if (object == null) {
            throw new ConfigurationException(String.format("%s should be provided", name));
        }
    }


    /**
     * Check if the configuration is positive or not.
     *
     * @param value the configuration  value
     * @param name the fullName
     * @throws ConfigurationException if the configuration value is  not positive
     */
    public static void checkConfigPositive(long value, String name) {
        if (value <= 0) {
            throw new ConfigurationException(String.format("%s should be positive", name));
        }
    }

    /**
     * Logs message with <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param message the log message.
     */
    public static void logDebugMessage(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * Logs for entrance into public methods at <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param signature the signature.
     * @param paramNames the names of parameters to log (not Null).
     * @param params the values of parameters to log (not Null).
     */
    public static void logEntrance(Logger logger, String signature, String[] paramNames, Object[] params) {
        if (logger.isDebugEnabled()) {
            String msg = String.format("Entering method %s.", signature) + toString(paramNames, params);
            logger.debug(msg);
        }
    }

    /**
     * Logs for exit from public methods at <code>DEBUG</code> level.
     *
     * @param logger the logger.
     * @param signature the signature of the method to be logged.
     * @param value the return value to log.
     */
    public static void logExit(Logger logger, String signature, Object value) {
        if (logger.isDebugEnabled()) {
            String msg = String.format("Exiting method %s.", signature);
            if (value != null) {
                msg += "Output parameter :" + toString(value);
            }
            logger.debug(msg);
        }
    }

    /**
     * Logs the given exception and message at <code>ERROR</code> level.
     *
     * @param <T> the exception type.
     * @param logger the logger.
     * @param signature the signature of the method to log.
     * @param ex the exception to log.
     */
    public static <T extends Throwable> void logException(Logger logger, String signature, T ex) {
        StringBuilder sw = new StringBuilder();
        sw.append(String.format("Error in method %s. Details:", signature)).append(": ").append(ex.getMessage());
        logger.error(sw.toString(), ex);
    }

    /**
     * Converts the parameters to string.
     *
     * @param paramNames the names of parameters.
     * @param params the values of parameters.
     * @return the string
     */
    private static String toString(String[] paramNames, Object[] params) {
        StringBuilder sb = new StringBuilder("Input parameters:");
        sb.append("{");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(paramNames[i]).append(":").append(toString(params[i]));
            }
        }
        sb.append("}.");
        return sb.toString();
    }

    /**
     * Converts the object to string.
     *
     * @param obj the object
     * @return the string representation of the object.
     */
    public static String toString(Object obj) {
        String result;
        try {
            if (NOLOGS.stream().anyMatch(s -> s.isInstance(obj))) {
                result = obj.getClass().getSimpleName();
            } else {
                result = MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            Helper.logException(LogAspect.LOGGER, "com.wiproevents.utils"
                    + ".Helper#toString", e);
            
            result = "The object can not be serialized by Jackson JSON mapper, error: " + e.toString();
        }
        return result;
    }

    /**
     * Get password encoder.
     *
     * @return the BC crypt password encoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Encode password for user.
     * @param user the user entity.
     * @param isUpdating the updating flag.
     * @return the user with encrypted password field.
     */
    public static User encodePassword(User user, boolean isUpdating) {
        Helper.checkNull(user, "user");
        String rawPassword = user.getPassword();
        boolean checkPassword = !isUpdating || rawPassword != null;
        if (checkPassword) {
            Helper.checkNullOrEmpty(rawPassword, "user.password");
            PasswordEncoder encoder = getPasswordEncoder();
            user.setPassword(encoder.encode(rawPassword));
        }
        return user;
    }

    public static String encodeToken(String token) {
        return DigestUtils.sha256Hex(token);
    }

    public static Query buildEqualPredict(Query query, Map<String, Object> values, String key, Object value) {
        if (value != null) {
            values.put(key, value);
            query.addCriteria(Criteria.where(key, values));
        }
        return query;
    }


    /**
     * Get id of entity.
     *
     * @param entity the entity.
     * @param <T> the entity class
     * @return if of entity if exists otherwise null.
     */
    public static <T extends IdentifiableEntity> String getId(T entity) {
        String id = null;
        if (entity != null) {
            id = entity.getId();
        }
        return id;
    }

    /**
     * Check whether value has been updated.
     *
     * @param oldValue the old value
     * @param newValue the new value.
     * @return true if value has been updated.
     */
    public static boolean isUpdated(Object oldValue, Object newValue) {
        return (oldValue != null && !oldValue.equals(newValue)) || (newValue != null && !newValue.equals(oldValue));
    }

    /**
     * Check whether BigDecimal value has been updated.
     *
     * @param oldValue the old value
     * @param newValue the new value.
     * @return true if value has been updated.
     */
    public static boolean isUpdated(BigDecimal oldValue, BigDecimal newValue) {
        return (oldValue != null && (newValue == null || oldValue.compareTo(newValue) != 0))
                || (newValue != null && (oldValue == null || newValue.compareTo(oldValue) != 0));
    }

    /**
     * Check whether both values is null.
     *
     * @param oldValue the old value
     * @param newValue the new value.
     * @return true if both values is null
     */
    public static boolean isBothNull(Object oldValue, Object newValue) {
        return oldValue == null && newValue == null;
    }

    /**
     * Check whether identifiable entity list has been updated.
     *
     * @param oldValues the old values
     * @param newValues the new values.
     * @param <T> the entity class
     * @return true if value has been updated.
     */
    public static <T extends IdentifiableEntity> boolean isUpdated(List<T> oldValues, List<T> newValues) {
        List<String> oldIds = oldValues == null ? Collections.emptyList()
                : oldValues.stream().map(IdentifiableEntity::getId).collect(Collectors.toList());
        return newValues == null && !oldIds.isEmpty()
                || newValues != null
                && (oldIds.size() != newValues.size() || newValues.stream().anyMatch(a -> !oldIds.contains(a.getId())));
    }

    /**
     * Get user from authentication.
     * @return user if exists valid user authentication otherwise null
     */
    public static User getAuthUser()  {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserAuthentication) {
            UserAuthentication userAuth = (UserAuthentication) auth;
            user = (User) userAuth.getPrincipal();
        } else if (auth != null  && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            user = userDetails.getUser();
        }
        return user;
    }


    /**
     * Get user id match given role .
     * @param role the role fullName
     * @return the user id if exist valid user role otherwise null.
     */
    public static String getUserRoleId(String role)  {
        String id = null;
        User user = getAuthUser();
        if (user != null && user.getRoles().stream().anyMatch(r -> role.equals(r.getName()))) {
           id = user.getId();
        }
        return id;
    }
    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @param <T> the entity class
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     */
    public static <T extends IdentifiableEntity> void checkUpdate(String id, T entity) {
        checkNullOrEmpty(id, "id");
        checkNull(entity, "entity");
        checkNullOrEmpty(entity.getId(), "entity.id");
        if (!id.equals(entity.getId())) {
            throw new IllegalArgumentException("id and id of passed entity should be same");
        }
    }
}

package com.livingprogress.mentorme.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livingprogress.mentorme.aop.LogAspect;
import com.livingprogress.mentorme.entities.AuditableUserEntity;
import com.livingprogress.mentorme.entities.IdentifiableEntity;
import com.livingprogress.mentorme.entities.NewPassword;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.security.CustomUserDetails;
import com.livingprogress.mentorme.security.UserAuthentication;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
     * Represents the utf8 encoding name.
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
     * @param name the name of the object, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the object is null
     */
    public static void checkNull(Object object, String name) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(CustomMessageSource.getMessage("checkNull.error", name));
        }
    }

    /**
     * It checks whether a given identifiable entity is valid.
     *
     * @param object the object to be checked
     * @param name the name of the object, used in the exception message
     * @param <T> the entity class
     * @throws IllegalArgumentException the exception thrown when the object is null or id of object is not positive
     */
    public static <T extends IdentifiableEntity> void checkEntity(T object, String name)
            throws IllegalArgumentException {
        checkNull(object, name);
        checkPositive(object.getId(), name + ".id");
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
     * @param name the name of the string, used in the exception message
     * @throws IllegalArgumentException the exception thrown when the given string is null or empty
     */
    public static void checkNullOrEmpty(String str, String name) throws IllegalArgumentException {
        if (isNullOrEmpty(str)) {
            throw new IllegalArgumentException(
                    CustomMessageSource.getMessage("checkNullOrEmpty.error", name));
        }
    }

    /**
     * Check if the value is positive.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not positive
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(CustomMessageSource.getMessage("checkPositive.error", name));
        }
    }

    /**
     * Check if the value is valid email.
     *
     * @param value the value to be checked
     * @param name the name of the value, used in the exception message
     * @throws IllegalArgumentException if the value is not email
     */
    public static void checkEmail(String value, String name) {
        checkNullOrEmpty(value, name);
        if (!isEmail(value)) {
            throw new IllegalArgumentException(CustomMessageSource.getMessage("checkEmail.error", name));
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
     * @param name the name
     * @throws ConfigurationException if the configuration is null
     */
    public static void checkConfigNotNull(Object object, String name) {
        if (object == null) {
            throw new ConfigurationException(CustomMessageSource.getMessage("checkNull.error", name));
        }
    }

    /**
     * Check if the directory configuration is valid.
     *
     * @param path the path
     * @param name the name
     * @throws ConfigurationException if the configuration is null or empty or valid directory not exist.
     */
    public static void checkDirectory(String path, String name) {
        if (Helper.isNullOrEmpty(path)) {
            throw new ConfigurationException(CustomMessageSource.getMessage("checkNullOrEmpty.error", name));
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            throw new ConfigurationException(CustomMessageSource.getMessage("checkDirectory.error", name));
        }
    }

    /**
     * Check if the configuration is positive or not.
     *
     * @param value the configuration  value
     * @param name the name
     * @throws ConfigurationException if the configuration value is  not positive
     */
    public static void checkConfigPositive(long value, String name) {
        if (value <= 0) {
            throw new ConfigurationException(CustomMessageSource.getMessage("checkPositive.error", name));
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
            String msg = CustomMessageSource.getMessage("log.entering", signature) + toString(paramNames, params);
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
            String msg = CustomMessageSource.getMessage("log.exiting", signature);
            if (value != null) {
                msg += CustomMessageSource.getMessage("log.output") + toString(value);
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
        sw.append(CustomMessageSource.getMessage("log.error", signature)).append(": ").append(ex.getMessage());
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
        StringBuilder sb = new StringBuilder(CustomMessageSource.getMessage("log.input"));
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
            Helper.logException(LogAspect.LOGGER, "com.livingprogress.mentorme.utils"
                    + ".Helper#toString", e);
            
            result = CustomMessageSource.getMessage("json.error", e.getMessage());
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


    /**
     * Build predicate to match ids in identifiable entity list.
     *
     * @param val the list value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <T> the identifiable entity
     * @return the match predicate
     */
    public static <T extends IdentifiableEntity> Predicate
    buildInPredicate(List<T> val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null && !val.isEmpty()) {
            List<Long> ids = val.stream().map(IdentifiableEntity::getId).collect(Collectors.toList());
            return cb.and(pd, path.in(ids));
        }
        
        return pd;
    }

    /**
     * Build >= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate
    buildGreaterThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.greaterThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build <= predicate.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @param <Y> the comparable entity
     * @return the match predicate
     */
    public static <Y extends Comparable<? super Y>> Predicate
    buildLessThanOrEqualToPredicate(Y val, Predicate pd, Path<? extends Y> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.lessThanOrEqualTo(path, val));
        }
        return pd;
    }

    /**
     * Build equal predicate for object value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(Object val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (val != null) {
            return cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build equal predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildEqualPredicate(String val, Predicate pd, Path<?> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            return cb.and(pd, cb.equal(path, val));
        }
        return pd;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param pd the predicate
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLikePredicate(String val, Predicate pd, Path<String> path, CriteriaBuilder cb) {
        if (!isNullOrEmpty(val)) {
            return cb.and(pd, buildLike(val, path, cb));
        }
        return pd;
    }

    /**
     * Build like predicate for string value.
     *
     * @param val the value
     * @param path the path
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildLike(String val, Path<String> path, CriteriaBuilder cb) {
        return cb.like(path, "%" + val + "%");
    }

    /**
     * Build name predicate..
     *
     * @param name the name
     * @param pd the predicate
     * @param root the root
     * @param cb the criteria builder.
     * @return the match predicate
     */
    public static Predicate buildNamePredicate(String name, Predicate pd, Root<?> root, CriteriaBuilder cb) {
        if (!isNullOrEmpty(name)) {
            return cb.and(pd, cb.or(Helper.buildLike(name,
                    root.get("firstName"), cb), Helper.buildLike(name, root.get("lastName"), cb)));
        }
        return pd;
    }


    /**
     * Get id of entity.
     *
     * @param entity the entity.
     * @param <T> the entity class
     * @return if of entity if exists otherwise null.
     */
    public static <T extends IdentifiableEntity> Long getId(T entity) {
        Long id = null;
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
        List<Long> oldIds = oldValues == null ? Collections.emptyList()
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
     * Audit with entity with created by and createdOn, last modified on/by information.
     * @param entity the  entity
     * @param <T> the auditable entity
     */
    public static <T extends AuditableUserEntity> void audit(T entity)  {
        User user = getAuthUser();
        if (user != null) {
            Date now = new Date();
            entity.setCreatedOn(now);
            entity.setLastModifiedOn(now);
            entity.setCreatedBy(user.getId());
            entity.setLastModifiedBy(user.getId());
        }
    }

    /**
     * Get user id match given role .
     * @param role the role name
     * @return the user id if exist valid user role otherwise null.
     */
    public static Long getUserRoleId(String role)  {
        Long id = null;
        User user = getAuthUser();
        if (user != null && user.getRoles().stream().anyMatch(r -> role.equals(r.getValue()))) {
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
    public static <T extends IdentifiableEntity> void checkUpdate(long id, T entity) {
        checkPositive(id, "id");
        checkNull(entity, "entity");
        checkPositive(entity.getId(), "entity.id");
        if (entity.getId() != id) {
            throw new IllegalArgumentException(CustomMessageSource.getMessage("update.notSameId.error"));
        }
    }
}

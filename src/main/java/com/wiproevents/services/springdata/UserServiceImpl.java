package com.wiproevents.services.springdata;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserServiceImpl extends BaseService<User, UserSearchCriteria> implements UserService {
    /**
     * The expiration time in millis.
     */
    @Value("${forgotPassword.expirationTimeInMillis}")
    private long forgotPasswordExpirationTimeInMillis;

    /**
     * The maxTimes to send forgot password request for single user.
     */
    @Value("${forgotPassword.maxTimes}")
    private long forgotPasswordMaxTimes;

    /**
     * The token expires milliseconds for 10 days.
     */
    @Value("${token.expirationTimeInMillis}")
    private long tokenExpirationTimeInMillis;


    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserServiceImpl() {
    }

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(forgotPasswordRepository, "forgotPasswordRepository");
        Helper.checkConfigNotNull(userRepository, "userRepository");
        Helper.checkConfigPositive(forgotPasswordExpirationTimeInMillis,
                "forgotPasswordExpirationTimeInMillis");
        Helper.checkConfigPositive(forgotPasswordMaxTimes, "forgotPasswordMaxTimes");
        Helper.checkConfigPositive(tokenExpirationTimeInMillis, "tokenExpirationTimeInMillis");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<User> getSpecification(UserSearchCriteria criteria) throws AttendeeException {
        return new UserSpecification(criteria);
    }


    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public User create(User entity) throws AttendeeException {
        Helper.encodePassword(entity, false);
        // check unique of the email
        if (entity.getEmail() != null) {
            List<User> user = userRepository.findByEmail(entity.getEmail());
            if (!user.isEmpty()) {
                throw new IllegalArgumentException("The email has been registered.");
            }
        }

        User result = super.create(entity);

        // create the user preference for the new user
        UserPreference userPreference = new UserPreference();
        userPreference.setUserId(result.getId());

        // save the user preference
        userPreferenceRepository.save(userPreference);

        return result;
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public User update(String id, User entity) throws AttendeeException {
        User existing = super.checkUpdate(id, entity);
        if (Helper.isUpdated(existing, entity)) {
            return getRepository().save(existing);
        }
        return existing;
    }

    /**
     * This method is used to create the forgot password entity for the given user.
     *
     * @param userId the user id.
     * @return the created forgot password entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AccessDeniedException if does not allow to perform action
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public ForgotPassword forgotPassword(String userId) throws AttendeeException {
        Helper.checkNullOrEmpty(userId, "userId");
        long count = forgotPasswordRepository.countByUserId(userId);
        if (count > forgotPasswordMaxTimes) {
            throw new AccessDeniedException("Reach max times to send forgot password request!");
        }
        ForgotPassword forgotPassword = new ForgotPassword();
        String token = UUID.randomUUID().toString();
        Date expiredOn = new Date(System.currentTimeMillis() + forgotPasswordExpirationTimeInMillis);
        forgotPassword.setUserId(userId);
        forgotPassword.setToken(token);
        forgotPassword.setExpiredOn(expiredOn);
        return forgotPasswordRepository.save(forgotPassword);
    }

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public boolean updatePassword(NewPassword newPassword) throws AttendeeException {
        Helper.checkNull(newPassword, "newPassword");
        String token = newPassword.getToken();
        String newPass = newPassword.getNewPassword();
        Helper.checkNullOrEmpty(token, "newPassword.token");
        Helper.checkNullOrEmpty(newPass, "newPassword.newPassword");
        ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(token);
        if (forgotPassword != null) {
            Date currentDate = new Date();
            if (currentDate.before(forgotPassword.getExpiredOn())) {
                User user = get(forgotPassword.getUserId());
                user.setPassword(newPass);
                Helper.encodePassword(user, false);
                getRepository().save(user);
                forgotPasswordRepository.deleteByUserId(forgotPassword.getUserId());
                return true;
            }
        } else {
            throw new IllegalArgumentException("Token is not correct");
        }
        return false;
    }


    @Override
    public User getMe() throws AttendeeException {
        // get the user from the tokens
        User user = Helper.getAuthUser();
        if (user == null) {
            return null;
        }

        // update the user info in db
        user = super.get(user.getId());

        return user;
    }

    @Override
    public String createTokenForUser(User user) {
        long expires = System.currentTimeMillis() + tokenExpirationTimeInMillis;

        AccessToken token = new AccessToken();

        token.setUpdatedOn(new Date());
        token.setCreatedOn(new Date());
        token.setUserId(user.getId());
        token.setExpires(new Date(expires));

        String tokenString = UUID.randomUUID().toString();
        String hashedToken = Helper.encodeToken(tokenString);

        // hash the token to store in db
        token.setToken(hashedToken);

        // save the token to db
        accessTokenRepository.save(token);

        return tokenString;
    }

    @Override
    public User getUserByAccessToken(String accessToken){
        String hashedToken = Helper.encodeToken(accessToken);

        List<AccessToken> tokens = accessTokenRepository.findByToken(hashedToken);
        if (tokens.size() == 0) {
            return null;
        }

        AccessToken token = tokens.get(0);
        if (token.getExpires().getTime() < new Date().getTime()) {
            // the access token has been expires
            return null;
        }

        // get by the id of the user
        return userRepository.findOne(token.getUserId());
    }

    @Override
    public User getUserByEmail(String email) throws AttendeeException {
        List<User> users = userRepository.findByEmail(email);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }
}


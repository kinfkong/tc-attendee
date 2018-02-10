package com.wiproevents.services.springdata;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.SocialUserService;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import com.wiproevents.utils.springdata.extensions.SearchResult;
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
     * The token expires milliseconds for 10 days.
     */
    @Value("${token.expirationTimeInMillis}")
    private long tokenExpirationTimeInMillis;

    @Value("${user.defaultRoleName}")
    private String defaultUserRoleName;

    @Autowired
    private SocialUserService socialUserService;


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
    private UserRoleRepository userRoleRepository;

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
        Helper.encodePassword(entity);
        // check unique of the email
        if (entity.getEmail() != null) {
            List<User> user = userRepository.findByEmail(entity.getEmail());
            if (!user.isEmpty()) {
                throw new IllegalArgumentException("The email has been registered.");
            }
        }

        // create the user role, set to the normal "USER" role first
        List<UserRole> list = userRoleRepository.findByName(defaultUserRoleName);
        if (list.size() == 0) {
            throw new AttendeeException("Cannot find the role with name [" + defaultUserRoleName + "]." +
                    " please set the user_role in database correctly.");
        }

        entity.setRoles(list);
        entity.setStatus(UserStatus.ACTIVE);

        User result = super.create(entity);

        // create the user preference for the new user
        UserPreference userPreference = new UserPreference();
        userPreference.setUserId(result.getId());

        // save the user preference
        userPreferenceRepository.save(userPreference);

        return result;
    }



    @Override
    protected void handleNestedUpdate(User entity, User oldEntity) throws AttendeeException {
        super.handleNestedUpdate(entity, oldEntity);

        // don't update the password in this api
        entity.setPassword(oldEntity.getPassword());
        if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Should contains at lease one role.");
        }
        // validate the email
        if (entity.getEmail() != null) {
            boolean ok = true;
            List<User> usersOfEmail = userRepository.findByEmail(entity.getEmail());
            if (usersOfEmail.size() > 0) {
                ok = false;
            }
            for (User u : usersOfEmail) {
                if (u.getId().equals(entity.getId())) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                throw new IllegalArgumentException("Email has been taken by other users.");
            }
        }

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
    public boolean updatePasswordWithForgotPasswordToken(NewPassword newPassword) throws AttendeeException {
        Helper.checkNull(newPassword, "newPassword");
        String token = newPassword.getForgotPasswordToken();
        String newPass = newPassword.getNewPassword();
        Helper.checkNullOrEmpty(token, "newPassword.token");
        Helper.checkNullOrEmpty(newPass, "newPassword.newPassword");
        List<ForgotPassword> forgotPasswords = forgotPasswordRepository.findByToken(token);
        if (forgotPasswords != null && forgotPasswords.size() > 0) {
            ForgotPassword forgotPassword = forgotPasswords.get(0);
            Date currentDate = new Date();
            if (currentDate.before(forgotPassword.getExpiredOn())) {
                User user = get(forgotPassword.getUserId());
                if (!newPassword.getEmail().equals(user.getEmail())) {
                    throw new IllegalArgumentException("The token is not bind with this email.");
                }
                user.setPassword(newPass);
                Helper.encodePassword(user);
                getRepository().save(user);
                forgotPasswordRepository.delete(forgotPassword.getId());
                return true;
            }
        } else {
            throw new IllegalArgumentException("Token is not correct");
        }
        return false;
    }

    @Override
    public boolean updatePasswordWithOldPassword(NewPassword newPassword) throws AttendeeException {
        Helper.checkNull(newPassword, "newPassword");
        Helper.checkNullOrEmpty("old password", newPassword.getOldPassword());
        Helper.checkNullOrEmpty("New password", newPassword.getNewPassword());
        User user = Helper.getAuthUser();
        if (!Helper.getPasswordEncoder().matches(newPassword.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("The old password is not correct");
        }
        user.setPassword(newPassword.getNewPassword());
        Helper.encodePassword(user);
        getRepository().save(user);
        return true;
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
    public User getUserByAccessToken(String accessToken) throws AttendeeException {
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

        // populate the fields use get
        return this.get(token.getUserId());
    }

    @Override
    public User getUserByEmail(String email) throws AttendeeException {
        List<User> users = userRepository.findByEmail(email);
        if (users.size() == 0) {
            return null;
        }
        // populate the fields use get
        return this.get(users.get(0).getId());
    }

    @Override
    public User getUserBySocial(String providerId, String providerUserId) throws AttendeeException {
        SocialUserSearchCriteria criteria = new SocialUserSearchCriteria();
        criteria.setProviderId(providerId);
        criteria.setProviderUserId(providerUserId);
        SearchResult<SocialUser> result = socialUserService.search(criteria, null);
        if (result.getEntities().isEmpty()) {
            return null;
        }
        return this.get(result.getEntities().get(0).getUserId());
    }

    @Override
    public User createSocialUser(SocialUser socialUser, User user) throws AttendeeException {
        if (getUserBySocial(socialUser.getProviderId(), socialUser.getProviderUserId()) != null) {
            throw new AttendeeException(
                    "The social user: " + socialUser.getProviderId() + " provider user id: "
                            + socialUser.getProviderUserId() + " already exists.");
        }

        User existing = null;
        if (user.getEmail() != null) {
            // find existing user by email
            existing = getUserByEmail(user.getEmail());

        }

        if (existing == null) {
            // create a new one for the user
            existing = create(user);
        }

        socialUser.setUserId(existing.getId());

        // create the social user
        socialUserService.create(socialUser);

        return existing;
    }
}


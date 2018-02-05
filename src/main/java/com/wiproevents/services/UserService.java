package com.wiproevents.services;

import com.wiproevents.entities.ForgotPassword;
import com.wiproevents.entities.NewPassword;
import com.wiproevents.entities.User;
import com.wiproevents.entities.UserSearchCriteria;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.exceptions.AttendeeException;

/**
 * The user service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface UserService extends GenericService<User, UserSearchCriteria> {
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
    ForgotPassword forgotPassword(String userId) throws AttendeeException;

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws AttendeeException if any other error occurred during operation
     */
    boolean updatePassword(NewPassword newPassword) throws AttendeeException;


    /**
     * Gets my profile.
     * @return my profile content.
     * @throws AttendeeException if there are any errors.
     */
    User getMe() throws AttendeeException;

    /**
     * Creates the token for user.
     * @param user the user.
     * @return the token.
     */
    String createTokenForUser(User user);
}


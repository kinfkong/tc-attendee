package mentorme.services.springdata;

import com.livingprogress.mentorme.entities.*;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.LookupService;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Spring Data JPA implementation of LookupService. Is effectively thread safe.
 */
@Service
@NoArgsConstructor
public class LookupServiceImpl implements LookupService {
    /**
     * The country repository for Country CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private CountryRepository countryRepository;

    /**
     * The state repository for State CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private StateRepository stateRepository;

    /**
     * The repository for ProfessionalConsultantArea CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ProfessionalConsultantAreaRepository professionalConsultantAreaRepository;

    /**
     * The goal category repository for GoalCategory CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private GoalCategoryRepository goalCategoryRepository;

    /**
     * The program category repository for ProgramCategory CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ProgramCategoryRepository programCategoryRepository;

    /**
     * The personal interest repository for PersonalInterest CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private PersonalInterestRepository personalInterestRepository;

    /**
     * The professional interest repository for ProfessionalInterest CRUD operations. Should be non-null after
     * injection.
     */
    @Autowired
    private ProfessionalInterestRepository professionalInterestRepository;

    /**
     * The document type repository for DocumentType CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    /**
     * The user role repository for UserRole CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(countryRepository, "countryRepository");
        Helper.checkConfigNotNull(stateRepository, "stateRepository");
        Helper.checkConfigNotNull(professionalConsultantAreaRepository, "professionalConsultantAreaRepository");
        Helper.checkConfigNotNull(goalCategoryRepository, "goalCategoryRepository");
        Helper.checkConfigNotNull(programCategoryRepository, "programCategoryRepository");
        Helper.checkConfigNotNull(personalInterestRepository, "personalInterestRepository");
        Helper.checkConfigNotNull(professionalInterestRepository, "professionalInterestRepository");
        Helper.checkConfigNotNull(documentTypeRepository, "documentTypeRepository");
        Helper.checkConfigNotNull(userRoleRepository, "userRoleRepository");
    }

    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<UserRole> getUserRoles() throws MentorMeException {
        return userRoleRepository.findAll();
    }

    /**
     * This method is used to get country lookups.
     *
     * @return the lookups for country.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<Country> getCountries() throws MentorMeException {
        List<Country> result = countryRepository.findAll();
        result.sort((c1, c2) -> c1.getValue().compareTo(c2.getValue()));
        return result;
    }

    /**
     * This method is used to get state lookups.
     *
     * @return the lookups for state.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<State> getStates(Long countryId) throws MentorMeException {
        List<State> result;
        if (countryId == null || countryId == 0) {
            result = stateRepository.findAll();
        } else {
            result = stateRepository.findByCountryId(countryId);
        }
        result.sort((c1, c2) -> c1.getValue().compareTo(c2.getValue()));
        return result;
    }

    /**
     * This method is used to get professional consultant area lookups.
     *
     * @return the lookups for professional consultant area.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProfessionalConsultantArea> getProfessionalConsultantAreas() throws MentorMeException {
        return professionalConsultantAreaRepository.findAll();
    }

    /**
     * This method is used to get goal category lookups.
     *
     * @return the lookups for goal category
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<GoalCategory> getGoalCategories() throws MentorMeException {
        return goalCategoryRepository.findAll();
    }

    /**
     * This method is used to program category lookups.
     *
     * @return the lookups for program category
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProgramCategory> getProgramCategories() throws MentorMeException {
        return programCategoryRepository.findAll();
    }

    /**
     * This method is used to personal interest lookups.
     *
     * @return the lookups for personal interest
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<PersonalInterest> getPersonalInterests() throws MentorMeException {
        return personalInterestRepository.findAll();
    }

    /**
     * This method is used to professional interest lookups.
     *
     * @return the lookups for professional interest
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProfessionalInterest> getProfessionalInterests() throws MentorMeException {
        return professionalInterestRepository.findAll();
    }

    /**
     * This method is used to document type lookups.
     *
     * @return the lookups for document type
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<DocumentType> getDocumentTypes() throws MentorMeException {
        return documentTypeRepository.findAll();
    }

    @Override
    public List<Skill> getSkills() throws MentorMeException {
        return skillRepository.findAll();
    }
}


package mentorme.services.springdata;

import com.livingprogress.mentorme.entities.InstitutionalProgram;
import com.livingprogress.mentorme.entities.InstitutionalProgramSearchCriteria;
import com.livingprogress.mentorme.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query InstitutionalProgram by criteria.
 */
@AllArgsConstructor
public class InstitutionalProgramSpecification implements Specification<InstitutionalProgram> {
    /**
     * The criteria. Final.
     */
    private final InstitutionalProgramSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity
     * in form of a Predicate for the given Root and CriteriaQuery.
     *
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<InstitutionalProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildLikePredicate(criteria.getProgramName(), pd, root.get("programName"), cb);
        pd = Helper.buildEqualPredicate(criteria.getInstitutionId(), pd, root.get("institution").get("id"), cb);
        if (criteria.getProgramCategory() != null) {
            pd = Helper.buildEqualPredicate(criteria.getProgramCategory().getId(),
                    pd, root.get("programCategory").get("id"), cb);
        }
        pd = Helper.buildGreaterThanOrEqualToPredicate(criteria.getMinDurationInDays(),
                pd, root.get("durationInDays"), cb);
        pd = Helper.buildLessThanOrEqualToPredicate(criteria.getMaxDurationInDays(),
                pd, root.get("durationInDays"), cb);
        return pd;
    }
}


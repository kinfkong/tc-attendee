package mentorme.services.springdata;

import com.livingprogress.mentorme.entities.MenteeMentorResponsibility;
import com.livingprogress.mentorme.entities.MenteeMentorResponsibilitySearchCriteria;
import com.livingprogress.mentorme.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query mentee mentor responsibility by criteria.
 */
@AllArgsConstructor
public class MenteeMentorResponsibilitySpecification implements Specification<MenteeMentorResponsibility> {
    /**
     * The criteria. Final.
     */
    private final MenteeMentorResponsibilitySearchCriteria criteria;

    /**
     /**
     * Creates a WHERE clause for a query of the referenced entity
     * in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<MenteeMentorResponsibility> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getMenteeMentorProgramId(), pd,
                root.get("menteeMentorProgramId"), cb);
        pd = Helper.buildEqualPredicate(criteria.getResponsibilityId(), pd, root.get("responsibilityId"), cb);
        return pd;
    }
}


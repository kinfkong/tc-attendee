package mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Image;
import com.livingprogress.mentorme.entities.ImageSearchCriteria;
import com.livingprogress.mentorme.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query mentee mentor goal by criteria.
 */
@AllArgsConstructor
public class ImageSpecification implements Specification<Image> {
    /**
     * The criteria. Final.
     */
    private final ImageSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity
     * in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getUrl(), pd,
                root.get("url"), cb);
        return pd;
    }
}


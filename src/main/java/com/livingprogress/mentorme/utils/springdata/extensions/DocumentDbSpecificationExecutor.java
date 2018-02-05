package com.livingprogress.mentorme.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by wangjinggang on 2018/2/5.
 */
@NoRepositoryBean
public interface DocumentDbSpecificationExecutor<T, ID extends Serializable> extends DocumentDbRepository<T, ID> {
    /**
     * Returns a {@link SearchResult} of entities matching the given {@link DocumentDbSpecification}.
     *
     * @param spec
     * @param paging
     * @return
     */
    SearchResult<T> fxndAll(DocumentDbSpecification<T> spec, Paging paging);

    /**
     * Returns the number of instances that the given {@link DocumentDbSpecification} will return.
     *
     * @param spec the {@link DocumentDbSpecification} to count instances for
     * @return the number of instances
     */
    long cxuntAll(DocumentDbSpecification<T> spec);
}

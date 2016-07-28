package co.xarx.trix.persistence.custom;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.MultiTenantEntity;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JpaMultiTenantRepository<T extends MultiTenantEntity> extends QueryDslJpaRepository<T, Integer> implements
		MultiTenantRepository<T, Integer> {

	EntityManager entityManager;
	JpaEntityInformation<T, Integer> entityInformation;

	public JpaMultiTenantRepository(JpaEntityInformation<T, Integer> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
	}

	private void applyMultitenant() {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		((Session) entityManager.getDelegate()).enableFilter("tenantFilter").setParameter("tenantId", tenantId);
	}

	protected class ByIdsSpecification implements Specification {
		public ParameterExpression<Iterable> parameter;

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Iterable.class);
			return path.in(parameter);
		}
	}

	protected class SingleIdSpecification implements Specification {
		public ParameterExpression<Serializable> parameter;

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Serializable.class);
			return path.in(parameter);
		}
	}

	@Override
	public T findOne(Integer integer) {
		applyMultitenant();
		SingleIdSpecification specification = new SingleIdSpecification();
		TypedQuery<T> query = getQuery(specification, (Pageable) null);
		query.setParameter(specification.parameter, integer);
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean exists(Integer integer) {
		applyMultitenant();
		return super.exists(integer);
	}

	@Override
	public List<T> findAll() {
		applyMultitenant();
		return super.findAll();
	}

	@Override
	public List<T> findAll(Iterable<Integer> integers) {
		applyMultitenant();
		return super.findAll(integers);
	}

	@Override
	public long count() {
		applyMultitenant();
		return super.count();
	}

	@Override
	protected Map<String, Object> getQueryHints() {
		applyMultitenant();
		return super.getQueryHints();
	}

	@Override
	protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
		applyMultitenant();
		return super.getQuery(spec, pageable);
	}

	@Override
	protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
		applyMultitenant();
		return super.getQuery(spec, sort);
	}

	@Override
	protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
		applyMultitenant();
		return super.readPage(query, pageable, spec);
	}

	@Override
	public T getOne(Integer integer) {
		applyMultitenant();
		return super.getOne(integer);
	}

	@Override
	protected TypedQuery<Long> getCountQuery(Specification<T> spec) {
		applyMultitenant();
		return super.getCountQuery(spec);
	}
}

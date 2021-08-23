package id.learn.dynamicwhere.search;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;


import id.learn.dynamicwhere.utils.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;

/**
 * GenericSpecification.
 *
 * @author Ravikumar.Beli@blueyonder.com
 * @project plan-sop-process-orchestration
 * @created on 13 Aug, 2021 9:28 PM
 **/

@Slf4j
public class GenericSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    private final transient List<SearchCriteria> list;

    public GenericSpecification() {
        this.list = new ArrayList<>();
    }

    /**
     * Add search criteria to the list.
     *
     * @param criteria the search criteria to build JPA specification
     */
    public void add(SearchCriteria criteria) {
        if (nonNull(criteria.getPair().getValue())) {
            list.add(criteria);
        }
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        log.info("List of filters added = {}", list);
        List<Predicate> predicates = new ArrayList<>();

        //add criteria to predicates
        for (SearchCriteria criteria : list) {

            switch (criteria.getOperation()) {
                case EQUAL:
                    predicates.add(equalTo(root, builder, criteria));
                    break;
                case GREATER_THAN:
                    predicates.add(greaterThan(root, builder, criteria));
                    break;
                case LESS_THAN:
                    predicates.add(lessThan(root, builder, criteria));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(greaterThanOrEqualTo(root, builder, criteria));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(lessThanOrEqualTo(root, builder, criteria));
                    break;
                case NOT_EQUAL:
                    predicates.add(notEqual(root, builder, criteria));
                    break;
                case MATCH:
                    predicates.add(match(root, builder, criteria));
                    break;
                case MATCH_START:
                    predicates.add(matchStart(root, builder, criteria));
                    break;
                case MATCH_END:
                    predicates.add(matchEnd(root, builder, criteria));
                    break;
                case BETWEEN_INT:
                    Predicate betweenIntPair = betweenIntPair(root, builder, criteria);
                    if (nonNull(betweenIntPair)) {
                        predicates.add(betweenIntPair);
                    }
                    break;
                case BETWEEN_DATES:
                    Predicate betweenDatePair = betweenDatePair(root, builder, criteria);
                    if (nonNull(betweenDatePair)) {
                        predicates.add(betweenDatePair);
                    }
                    break;
                case IN:
                    log.debug("column = {}, values = {}", criteria.getPair().getKey(),
                        (List) criteria.getPair().getValue());
                    predicates.add(root.get(criteria.getPair().getKey()).in((List) criteria.getPair().getValue()));
                    break;

                default:
                    //do nothing
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Convert list of generic type to specified data type in function.
     *
     * @param from list to be converted
     * @param func lambda function example: s -> Integer::valueOf
     * @param <T>  from T data type
     * @param <U>  to U data type
     * @return
     */
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(toList());
    }

    protected Predicate betweenDatePair(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        Pair<Date, Date> datePair = DateUtil.getDatePair((String) criteria.getPair().getValue());
        if (nonNull(datePair)) {
            return builder.between(
                root.get(criteria.getPair().getKey()).as(Date.class), datePair.getLeft(), datePair.getRight());
        }
        return null;
    }

    protected Predicate betweenIntPair(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        Pair<Integer, Integer> intPair = getIntPair((String) criteria.getPair().getValue());
        if (nonNull(intPair)) {
            return builder.between(root.get(criteria.getPair().getKey()), intPair.getLeft(), intPair.getRight());
        }
        return null;
    }

    protected Predicate matchEnd(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return builder.like(
            builder.lower(root.get(criteria.getPair().getKey())),
            criteria.getPair().getValue().toString().toLowerCase() + "%");
    }

    protected Predicate matchStart(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return builder.like(
            builder.lower(root.get(criteria.getPair().getKey())),
            "%" + criteria.getPair().getValue().toString().toLowerCase());
    }

    protected Predicate match(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return builder.like(
            builder.lower(root.get(criteria.getPair().getKey())),
            "%" + criteria.getPair().getValue().toString().toLowerCase() + "%");
    }

    protected Predicate notEqual(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.notEqual(root.get(criteria.getPair().getKey()).as(Date.class),
                criteria.getPair().getValue());
        } else {
            return builder.notEqual(root.get(criteria.getPair().getKey()),
                criteria.getPair().getValue().toString());
        }
    }

    protected Predicate lessThanOrEqualTo(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.lessThanOrEqualTo(root.get(criteria.getPair().getKey()).as(Date.class),
                (Date) criteria.getPair().getValue());
        } else {
            return builder.lessThanOrEqualTo(root.get(criteria.getPair().getKey()),
                criteria.getPair().getValue().toString());
        }
    }

    protected Predicate greaterThanOrEqualTo(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getPair().getKey()).as(Date.class),
                (Date) criteria.getPair().getValue());
        } else {
            return builder.greaterThanOrEqualTo(root.get(criteria.getPair().getKey()),
                criteria.getPair().getValue().toString());
        }
    }

    protected Predicate lessThan(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.lessThan(root.get(criteria.getPair().getKey()).as(Date.class),
                (Date) criteria.getPair().getValue());
        } else {
            return builder.lessThan(root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString());
        }
    }

    protected Predicate greaterThan(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.greaterThan(root.get(criteria.getPair().getKey()).as(Date.class),
                (Date) criteria.getPair().getValue());
        } else {
            return builder.greaterThan(root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString());
        }
    }

    protected Predicate equalTo(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        if (criteria.getPair().getValue() instanceof Date) {
            return builder.equal(root.get(criteria.getPair().getKey()).as(Date.class),
                (Date) criteria.getPair().getValue());
        } else {
            return builder.equal(root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString());
        }
    }

    protected Predicate in(Root<T> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return root.get(criteria.getPair().getKey()).in(criteria.getPair().getValue());
    }

    protected Pair<Integer, Integer> getIntPair(String rangeValues) {
        String[] values = new String[2];
        if (nonNull(rangeValues)) {
            values = StringUtils.split(rangeValues, ",", 2);
        }
        return values.length == 2 ? Pair.of(Integer.parseInt(values[0]), Integer.parseInt(values[1])) : null;
    }

}

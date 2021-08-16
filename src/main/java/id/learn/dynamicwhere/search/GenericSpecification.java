package id.learn.dynamicwhere.search;

import static id.learn.dynamicwhere.utils.DateUtil.toDate;
import static java.util.Objects.nonNull;

import id.learn.dynamicwhere.utils.DateUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;

/**
 * Project Name     : dynamic-where
 * Date Time        : 6/10/2020
 *
 * @author Teten Nugraha
 */

public class GenericSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    private final transient List<SearchCriteria> list;

    public GenericSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        if (nonNull(criteria.getPair().getValue())) {
            list.add(criteria);
        }
    }

    @SneakyThrows
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add criteria to predicates
        for (SearchCriteria criteria : list) {

            switch (criteria.getOperation()) {
                case EQUAL:
                    predicates.add(builder.equal(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(
                        root.get(criteria.getPair().getKey()), criteria.getPair().getValue().toString()));
                    break;
                case MATCH:
                    predicates.add(builder.like(
                        builder.lower(root.get(criteria.getPair().getKey())),
                        "%" + criteria.getPair().getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(
                        builder.lower(root.get(criteria.getPair().getKey())),
                        "%" + criteria.getPair().getValue().toString().toLowerCase()));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(
                        builder.lower(root.get(criteria.getPair().getKey())),
                        criteria.getPair().getValue().toString().toLowerCase() + "%"));
                    break;
                case BETWEEN_INT:
                    Pair<Integer, Integer> intPair = getIntPair((String) criteria.getPair().getValue());
                    if (nonNull(intPair)) {
                        predicates.add(builder.between(
                            root.get(criteria.getPair().getKey()), intPair.getLeft(), intPair.getRight()));
                    }
                    break;
                case BETWEEN_DATES:
                    Pair<Date, Date> datePair = getDatePair((String) criteria.getPair().getValue());
                    if (nonNull(datePair)) {
                        predicates.add(builder.between(
                            root.get(criteria.getPair().getKey()), datePair.getLeft(), datePair.getRight()));
                    }
                    break;
                default:
                    //do nothing
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Pair<Integer, Integer> getIntPair(String rangeValues) {
        String[] values = new String[2];
        if (nonNull(rangeValues)) {
            values = StringUtils.split(rangeValues, ",", 2);
        }
        return values.length == 2 ? Pair.of(Integer.parseInt(values[0]), Integer.parseInt(values[1])) : null;
    }

    private Pair<Date, Date> getDatePair(String rangeValues) throws ParseException {
        String[] values = new String[2];
        if (nonNull(rangeValues)) {
            values = StringUtils.split(rangeValues, ",", 2);
        }
        return values.length == 2 ? Pair.of(toDate(values[0], DateUtil.FORMAT), toDate(values[1], DateUtil.FORMAT)) : null;
    }

}
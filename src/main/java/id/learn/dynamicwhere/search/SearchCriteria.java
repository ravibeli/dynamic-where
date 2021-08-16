package id.learn.dynamicwhere.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Project Name     : dynamic-where
 * Date Time        : 6/10/2020
 *
 * @author Teten Nugraha
 */

@ToString
@Setter
@Getter
@AllArgsConstructor
public class SearchCriteria {
    private Pair<String, Object> pair;
    private SearchOperation operation;
}
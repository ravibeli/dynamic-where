package id.learn.dynamicwhere.search;

/**
 * Project Name     : dynamic-where
 * Date Time        : 6/10/2020
 *
 * @author Teten Nugraha
 */

public enum SearchOperation {
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN_EQUAL,
    NOT_EQUAL,
    EQUAL,
    MATCH,
    MATCH_START,
    MATCH_END,
    BETWEEN_INT,
    BETWEEN_DATES,
    IN
}
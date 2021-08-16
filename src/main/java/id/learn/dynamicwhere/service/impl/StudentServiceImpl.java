package id.learn.dynamicwhere.service.impl;

import id.learn.dynamicwhere.entity.Student;
import id.learn.dynamicwhere.repository.StudentRepository;
import id.learn.dynamicwhere.search.GenericSpecification;
import id.learn.dynamicwhere.search.SearchCriteria;
import id.learn.dynamicwhere.search.SearchOperation;
import id.learn.dynamicwhere.service.StudentService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String ADDRESS = "address";
    private static final String ADDRESS_VALUE = "BANDUNG";
    private static final String AGE = "age";
    private static final int AGE_VALUE = 25;
    private static final String CREATED_ON = "createdOn";

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findStudentsWithPredicate() {

        /*
         * find Student which stay in BANDUNG and age greater than 25 years old
         */

        GenericSpecification<Student> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(Pair.of(ADDRESS, ADDRESS_VALUE), SearchOperation.EQUAL));
        genericSpecification.add(new SearchCriteria(Pair.of(AGE, AGE_VALUE), SearchOperation.GREATER_THAN));

        return studentRepository.findAll(genericSpecification);
    }

    @Override
    public List<Student> findStudentsWithPredicate(String address, Integer age, String ageRange) {

        /*
         * find Student which stay in BANDUNG and age greater than 25 years old
         */

        GenericSpecification<Student> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(Pair.of(ADDRESS, address), SearchOperation.EQUAL));
        genericSpecification.add(new SearchCriteria(Pair.of(AGE, age), SearchOperation.EQUAL));
        genericSpecification.add(new SearchCriteria(Pair.of(AGE, ageRange), SearchOperation.BETWEEN_INT));

        return studentRepository.findAll(genericSpecification);
    }

    @Override
    public List<Student> findStudentsWithPredicate(String address, Integer age, String ageRange, String dateRange) {

        /*
         * find Student which stay in BANDUNG and age greater than 25 years old
         */

        GenericSpecification<Student> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(Pair.of(ADDRESS, address), SearchOperation.EQUAL));
        genericSpecification.add(new SearchCriteria(Pair.of(AGE, age), SearchOperation.EQUAL));
        genericSpecification.add(new SearchCriteria(Pair.of(AGE, ageRange), SearchOperation.BETWEEN_INT));
        genericSpecification.add(new SearchCriteria(Pair.of(CREATED_ON, dateRange), SearchOperation.BETWEEN_DATES));

        return studentRepository.findAll(genericSpecification);
    }
}

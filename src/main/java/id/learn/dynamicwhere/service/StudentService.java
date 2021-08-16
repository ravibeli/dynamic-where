package id.learn.dynamicwhere.service;

import id.learn.dynamicwhere.entity.Student;
import java.util.List;

public interface StudentService {

    List<Student> findAll();
    List<Student> findStudentsWithPredicate();
    List<Student> findStudentsWithPredicate(String address, Integer age, String ageRange);
    List<Student> findStudentsWithPredicate(String address, Integer age, String ageRange, String dateRange);
}

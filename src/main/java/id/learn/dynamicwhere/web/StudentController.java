package id.learn.dynamicwhere.web;

import static java.util.Objects.nonNull;

import id.learn.dynamicwhere.entity.Student;
import id.learn.dynamicwhere.service.StudentService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Name     : dynamic-where
 * Date Time        : 6/10/2020
 *
 * @author Teten Nugraha
 */

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/withDynamicSearch")
    public List<Student> withDynamicSearch(@RequestParam(value = "address", required = false) String address,
                                           @RequestParam(value = "age", required = false) Integer age,
                                           @RequestParam(value = "ageRange", required = false) String ageRange,
                                           @RequestParam(value = "dateRange", required = false) String dateRange) {

        return studentService.findStudentsWithPredicate(address, age, ageRange, dateRange);
    }

}

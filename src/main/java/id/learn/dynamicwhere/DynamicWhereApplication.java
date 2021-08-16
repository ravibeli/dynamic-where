package id.learn.dynamicwhere;

import static id.learn.dynamicwhere.utils.DateUtil.toDate;

import id.learn.dynamicwhere.entity.Student;
import id.learn.dynamicwhere.repository.StudentRepository;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DynamicWhereApplication implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(DynamicWhereApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        studentRepository.deleteAll();

        String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

        Student student1 = new Student();
        student1.setName("Budi");
        student1.setAge(21);
        student1.setAddress("BANDUNG");
        student1.setCreatedOn(toDate("2021-08-16T10:20:30", FORMAT));

        Student student2 = new Student();
        student2.setName("Djaka");
        student2.setAge(22);
        student2.setAddress("JAKARTA");
        student2.setCreatedOn(toDate("2021-08-17T02:20:30", FORMAT));


        Student student3 = new Student();
        student3.setName("Handoko");
        student3.setAge(23);
        student3.setAddress("BANDUNG");
        student3.setCreatedOn(toDate("2021-08-18T01:40:30", FORMAT));


        Student student4 = new Student();
        student4.setName("Turi");
        student4.setAge(24);
        student4.setAddress("BANDUNG");
        student4.setCreatedOn(toDate("2021-08-19T04:25:30", FORMAT));

        Student student5 = new Student();
        student5.setName("Ravi");
        student5.setAge(25);
        student5.setAddress("BANDUNG");
        student5.setCreatedOn(toDate("2021-08-20T05:30:01", FORMAT));

        List<Student> mockStudentList = Arrays.asList(student1, student2, student3, student4, student5);

        studentRepository.saveAll(mockStudentList);
    }

}

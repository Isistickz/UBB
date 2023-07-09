package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllstudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getstudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student getStudentByFirstName(String firstName) {
        return studentRepository.findByFirstName(firstName);
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + studentId));
    }

    public Student getStudentWithTelegramID(String telegramID) {
        return studentRepository.findByTelegramID(telegramID);
    }
}


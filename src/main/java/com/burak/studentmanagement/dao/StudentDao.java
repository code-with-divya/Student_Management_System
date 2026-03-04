package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Student;

public interface StudentDao {
	
	public Student findByEmail(String email);
	
	public void save(Student student);
	
	
public Student findByStudentId(int id);
	
	public List<Student> findAllStudents();
	
	public void deleteById(int id);
}

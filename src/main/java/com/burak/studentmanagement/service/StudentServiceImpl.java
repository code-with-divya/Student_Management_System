package com.burak.studentmanagement.service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.RoleDao;
import com.burak.studentmanagement.dao.StudentDao;
import com.burak.studentmanagement.entity.Role;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.user.UserDto;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired 
	private RoleDao roleDao;
	
	// This method is used to find a student by their username. It is called by Spring Security during the authentication process to load the user's details.
	@Override
	@Transactional
	public Student findByEmail(String email) {
		return studentDao.findByEmail(email);
	}
	
	@Override
	@Transactional
	public Student findByStudentId(int id) {
		return studentDao.findByStudentId(id);
	}
	// This method is used to save a new student to the database. It takes a UserDto object as input, which contains the student's information from the registration form. It creates a new Student entity, sets its properties, encodes the password, and saves it to the database using the StudentDao.
	@Override
	@Transactional
	public void save(UserDto userDto) {
		Student student = new Student();
		student.setUserName(userDto.getUserName());
		student.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		student.setFirstName(userDto.getFirstName());
		student.setLastName(userDto.getLastName());
		student.setEmail(userDto.getEmail());	
			
		// student.setRole(userDto.getRole());	

		Role role=roleDao.findByName("STUDENT");
		student.setRole(role);
		
		studentDao.save(student);
	}
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Spring Security will call this method to load the user details during authentication. We need to find the user by username and return a UserDetails object that contains the user's information and authorities.
		Student student = studentDao.findByEmail(username);
		if (student == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		Collection<Role> role = new ArrayList<>();
		role.add(student.getRole());
		return new org.springframework.security.core.userdetails.User
		(
			student.getEmail(),
			student.getPassword(),
			mapRolesToAuthorities(role));
	}
	// Spring Security requires a collection of GrantedAuthority objects, so we need to convert our Role objects to SimpleGrantedAuthority objects.
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
		.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).
		collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<Student> findAllStudents() {
		return studentDao.findAllStudents();
	}

	@Override
	@Transactional
	public void save(Student student) {
		studentDao.save(student);
		
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		studentDao.deleteById(id);
	}

}

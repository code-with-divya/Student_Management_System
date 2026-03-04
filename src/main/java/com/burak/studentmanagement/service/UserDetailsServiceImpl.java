package com.burak.studentmanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.burak.studentmanagement.entity.Role;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.Teacher;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // First try student by email
        Student student = studentService.findByEmail(username);
        if (student != null) {
            Collection<Role> roles = new ArrayList<>();
            roles.add(student.getRole());

            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    roles.stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                            .collect(Collectors.toList())
            );
        }

        // Then try teacher by username
        Teacher teacher = teacherService.findByTeacherName(username);
        if (teacher != null) {
            Collection<Role> roles = new ArrayList<>();
            roles.add(teacher.getRole());

            return new User(
                    teacher.getUserName(),
                    teacher.getPassword(),
                    roles.stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                            .collect(Collectors.toList())
            );
        }

        throw new UsernameNotFoundException("Invalid username or password");
    }
}
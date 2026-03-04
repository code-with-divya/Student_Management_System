package com.burak.studentmanagement.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.burak.studentmanagement.dao.StudentDao;
import com.burak.studentmanagement.dao.TeacherDao;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.Teacher;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private TeacherDao teacherDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        
        //  Check Student table
        Student student = studentDao.findByEmail(username);
        if (student != null) {
            Collection<? extends GrantedAuthority> authorities
                    = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + student.getRole().getName())
                    );

            return new User(student.getUserName(),
                    student.getPassword(),
                    authorities);
        }


        // Check Teacher table
        Teacher teacher = teacherDao.findByTeacherName(username);

        if (teacher != null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new User(teacher.getUserName(),
                teacher.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(teacher.getRole().getName())
                ));
    }

}

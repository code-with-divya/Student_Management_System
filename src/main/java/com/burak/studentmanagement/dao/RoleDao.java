package com.burak.studentmanagement.dao;

import com.burak.studentmanagement.entity.Role;

public interface RoleDao {
	
	public Role findByName(String name);
}

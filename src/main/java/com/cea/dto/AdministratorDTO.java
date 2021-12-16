package com.cea.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cea.models.Administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String username;
	private String password;
	private String user;
	
	public Administrator toEntity() {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String passwordHash = bCrypt.encode(this.password);
		
		Date date = new Date();
		
		String usernameLowerCase = this.username != null ? this.username.toLowerCase() : "";
		
		Administrator administrator = new Administrator();
		
		administrator.setRoles(false);
		administrator.setName(this.name);
		administrator.setCreatedAt(date);
		administrator.setUpdatedAt(date);
		administrator.setCreatedBy(this.user);
		administrator.setUpdatedBy(this.user);
		administrator.setPassword(passwordHash);
		administrator.setUsername(usernameLowerCase);
		
		return administrator;
	}

}

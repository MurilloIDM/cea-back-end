package com.cea.dto.administrator;

import java.io.Serializable;
import java.util.Date;

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
	private String user;
	
	public Administrator toEntity() {
		Date date = new Date();
		
		String usernameLowerCase = this.username != null ? this.username.toLowerCase() : "";
		
		Administrator administrator = new Administrator();
		
		administrator.setRoles(false);
		administrator.setName(this.name);
		administrator.setCreatedAt(date);
		administrator.setUpdatedAt(date);
		administrator.setCreatedBy(this.user);
		administrator.setUpdatedBy(this.user);
		administrator.setIsPrimaryAccess(true);
		administrator.setUsername(usernameLowerCase);
		
		return administrator;
	}

}

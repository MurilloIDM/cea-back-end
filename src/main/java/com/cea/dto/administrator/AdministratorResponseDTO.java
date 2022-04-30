package com.cea.dto.administrator;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorResponseDTO {
	
	private UUID id;
	private String username;
	private String password;
	
}

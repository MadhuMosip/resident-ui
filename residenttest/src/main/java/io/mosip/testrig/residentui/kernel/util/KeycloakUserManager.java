package io.mosip.testrig.residentui.kernel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.utility.TestRunner;

public class KeycloakUserManager {
	
	private static final org.slf4j.Logger logger= org.slf4j.LoggerFactory.getLogger(KeycloakUserManager.class);

	public static Properties propsKernel = getproperty(TestRunner.getResourcePath() + "/config/"+TestRunner.GetKernalFilename());

	private static Keycloak getKeycloakInstance() {
		 Keycloak key=null;
		try {
			
	key=KeycloakBuilder.builder().serverUrl(ConfigManager.getIAMUrl()).realm(ConfigManager.getIAMRealmId())
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS).clientId(ConfigManager.getAutomationClientId()).clientSecret(ConfigManager.getAutomationClientSecret())
				.build();
	System.out.println(ConfigManager.getIAMUrl());
	System.out.println(key.toString() + key.realms());
		}catch(Exception e)
		{
			throw e;
			
		}
		return key;
	}

	public static Properties getproperty(String path) {
		Properties prop = new Properties();		
		try {
			File file = new File(path);
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			logger.error("Exception " + e.getMessage());
		}
		return prop;
	}
	
	
public static void createuser(){
	createUsers(BaseTestCase.currentModule +"-"+ConfigManager.getIAMUsersToCreate(), ConfigManager.getIAMUsersPassword(),"roles", new HashMap<String, List<String>>());
}



public static void createUsers(String userid, String pwd, String rolenum, Map<String, List<String>> map) {
	Keycloak keycloakInstance = getKeycloakInstance();
	UserRepresentation user = new UserRepresentation();
	user.setEnabled(true);
	user.setUsername(userid);
	user.setFirstName(userid);
	user.setLastName(userid);
	user.setEmail(userid+"mosip.io");
	if (map != null)
		user.setAttributes(map);
	RealmResource realmResource = null;

	realmResource = keycloakInstance.realm(ConfigManager.getIAMRealmId());

	UsersResource usersRessource = realmResource.users();

	try (Response response = usersRessource.create(user)) {

		if (response.getStatus() == 409) {

		} else {


			String userId = CreatedResponseUtil.getCreatedId(response);
			

			CredentialRepresentation passwordCred = new CredentialRepresentation();

			passwordCred.setTemporary(false);
			passwordCred.setType(CredentialRepresentation.PASSWORD);

			passwordCred.setValue(pwd);

			UserResource userResource = usersRessource.get(userId);
			userResource.resetPassword(passwordCred);

			List<RoleRepresentation> allRoles = realmResource.roles().list();
			List<RoleRepresentation> availableRoles = new ArrayList<>();
			List<String> toBeAssignedRoles = List.of(propsKernel.getProperty(rolenum).split(","));
			for (String role : toBeAssignedRoles) {
				if (allRoles.stream().anyMatch((r -> r.getName().equalsIgnoreCase(role)))) {
					if (allRoles.stream().filter(r->r.getName().equals(role)).findFirst().isPresent())
						availableRoles.add(allRoles.stream().filter(r->r.getName().equals(role)).findFirst().get());
				 
			}
			userResource.roles().realmLevel() //
					.add((availableRoles.isEmpty() ? allRoles : availableRoles));

			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
	}

}
//	public static void createUsers() {
//		
//		List<String> needsToBeCreatedUsers = List.of(ConfigManager.getIAMUsersToCreate().split(","));
//		Keycloak keycloakInstance = getKeycloakInstance();
//		for (String needsToBeCreatedUser : needsToBeCreatedUsers) {
//			UserRepresentation user = new UserRepresentation();
//			String moduleSpecificUser = null;
//			if (needsToBeCreatedUser.equals("globaladmin")) {
//				moduleSpecificUser = needsToBeCreatedUser;
//			}
//			else if(needsToBeCreatedUser.equals("masterdata-220005")){
//				moduleSpecificUser = needsToBeCreatedUser;
//				
//			}
//			
//			else {
//				moduleSpecificUser = BaseTestCase.currentModule +"-"+ needsToBeCreatedUser;
//			}
//			
//			System.out.println(moduleSpecificUser);
//			user.setEnabled(true);
//			user.setUsername(moduleSpecificUser);
//			user.setFirstName(moduleSpecificUser);
//			user.setLastName(moduleSpecificUser);
//			user.setEmail("automation" + moduleSpecificUser + "@automationlabs.com");
//			// Get realm
//			RealmResource realmResource = keycloakInstance.realm(ConfigManager.getIAMRealmId());
//			UsersResource usersRessource = realmResource.users();
//			// Create user (requires manage-users role)
//			Response response = null;
//				response = usersRessource.create(user);
// 				System.out.println(response);
//			System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
//			if (response.getStatus()==409) {
//				continue;
//			}
//			System.out.println(response.getLocation());
//			String userId = CreatedResponseUtil.getCreatedId(response);
//			System.out.printf("User created with userId: %s%n", userId);
//
//			// Define password credential
//			CredentialRepresentation passwordCred = new CredentialRepresentation();
//			
//			passwordCred.setTemporary(false);
//			passwordCred.setType(CredentialRepresentation.PASSWORD);
//			
//			//passwordCred.setValue(userPassword.get(passwordIndex));
//			passwordCred.setValue("mosip123");
//
//			UserResource userResource = usersRessource.get(userId);
//
//			// Set password credential
//			userResource.resetPassword(passwordCred);
//
//			// Getting all the roles
//			List<RoleRepresentation> allRoles = realmResource.roles().list();
//			List<RoleRepresentation> availableRoles = new ArrayList<>();
//			List<String> toBeAssignedRoles = List.of(ConfigManager.getRolesForUser(needsToBeCreatedUser));
//			for(String role : toBeAssignedRoles) {
//				if(allRoles.stream().anyMatch((r->r.getName().equalsIgnoreCase(role)))){
//					availableRoles.add(allRoles.stream().filter(r->r.getName().equals(role)).findFirst().get());
//				}else {
//					System.out.printf("Role not found in keycloak: %s%n", role);
//				}
//			}
//			// Assign realm role tester to user
//			userResource.roles().realmLevel() //
//					.add((availableRoles.isEmpty() ? allRoles : availableRoles));
//			//passwordIndex ++;
//		}
//	}
	
	public static void removeUser() {
		List<String> needsToBeRemovedUsers = List.of(ConfigManager.getIAMUsersToCreate().split(","));
		Keycloak keycloakInstance = getKeycloakInstance();
		for (String needsToBeRemovedUser : needsToBeRemovedUsers) {
			String moduleSpecificUserToBeRemoved = BaseTestCase.currentModule +"-"+ needsToBeRemovedUser;
			RealmResource realmResource = keycloakInstance.realm(ConfigManager.getIAMRealmId());
			UsersResource usersRessource = realmResource.users();

			List<UserRepresentation> usersFromDB = usersRessource.search(moduleSpecificUserToBeRemoved);
			if (!usersFromDB.isEmpty()) {
				UserResource userResource = usersRessource.get(usersFromDB.get(0).getId());
				userResource.remove();
				System.out.printf("User removed with name: %s%n", moduleSpecificUserToBeRemoved);
			} else {
				System.out.printf("User not found with name: %s%n", moduleSpecificUserToBeRemoved);
			}

		}
	}

}

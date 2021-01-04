package com.learning.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.DeletedSecret;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;



@RestController
public class SecretsController {
	//https://docs.microsoft.com/en-us/azure/key-vault/secrets/quick-create-java
	
	private String keyVaultName = "keyvalutdemokv1";
	
	@RequestMapping("get/{secretName}")
	public String getSecret(@PathVariable("secretName") String secretName) {
		System.out.println(String.format("Get Request %s", secretName));
		String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

		SecretClient secretClient = new SecretClientBuilder()
		    .vaultUrl(keyVaultUri)
		    .credential(new DefaultAzureCredentialBuilder().build())
		    .buildClient();
		
		KeyVaultSecret retrievedSecret = secretClient.getSecret(secretName);
		
		
		return "Secret Value: " + retrievedSecret.getValue();
	}
	
	@RequestMapping("create/{secretName}/{secretValue}")
	public String setSecret(@PathVariable("secretName") String secretName, @PathVariable("secretValue") String secretValue) {
		System.out.println(String.format("Get Request %s:%s", secretName,secretValue));
		String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

		SecretClient secretClient = new SecretClientBuilder()
		    .vaultUrl(keyVaultUri)
		    .credential(new DefaultAzureCredentialBuilder().build())
		    .buildClient();
		
		secretClient.setSecret(new KeyVaultSecret(secretName, secretValue));
		
		
		return String.format("The value of secret %s is updated to %s", secretName, secretValue);
	}
	
	@RequestMapping("delete/{secretName}")
	public String deleteSecret(@PathVariable("secretName") String secretName) {
		System.out.println(String.format("Delete Request %s", secretName));
		String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

		SecretClient secretClient = new SecretClientBuilder()
		    .vaultUrl(keyVaultUri)
		    .credential(new DefaultAzureCredentialBuilder().build())
		    .buildClient();
		
		SyncPoller<DeletedSecret, Void> deletionPoller = secretClient.beginDeleteSecret(secretName);
		deletionPoller.waitForCompletion();
		
		
		return String.format("The value of secret %s is deleted", secretName);
	}
	
	@RequestMapping("/{name}")
	public String greet(@PathVariable("name") String name) {
		return "Hello " + name;
	}

}

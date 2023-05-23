/*
 * Cloud Keys 2022 Java Edition- Demo Application
 *
 * Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
 *
 */

import java.io.*;

import cloudkeys.*;

public class googlesecrets extends ConsoleDemo{

	static String command; // user's command
	static String buffer; // text buffer
	static String[] argument; // arguments following command

	static String secretName;
	static String secretType;
	static String data;

	public static void main(String[] args) {

		Googlesecrets googlesecrets = new Googlesecrets();
		try {
			googlesecrets.addGooglesecretsEventListener(new DefaultGooglesecretsEventListener(){

				@Override
				public void error(GooglesecretsErrorEvent e) {
					System.out.println("Error [" + e.errorCode + "]: " + e.description);
				}

				@Override
				public void secretList(GooglesecretsSecretListEvent e) {
					System.out.println("   " + e.name);
				}
			});

			System.out.println("Press Enter to Authenticate.");
			input();

			googlesecrets.getOAuth().setClientId("157623334268.apps.googleusercontent.com");
			googlesecrets.getOAuth().setClientSecret("v8R8R90hn_LchsVc0Ta6Sy0D");
			googlesecrets.getOAuth().setAuthorizationScope("https://www.googleapis.com/auth/cloud-platform");
			googlesecrets.getOAuth().setServerAuthURL("https://accounts.google.com/o/oauth2/auth");
			googlesecrets.getOAuth().setServerTokenURL("https://accounts.google.com/o/oauth2/token");

			googlesecrets.authorize();
			System.out.println("Authentication Successful.");

			googlesecrets.setGoogleProjectId(prompt("Google Cloud Project ID"));

			while (true) {
				DisplayMenu();
				System.out.print("> ");
				command = input();
				argument = command.split("\\s");
				if (argument.length == 0)
					continue;
				switch (argument[0].charAt(0)) {
					case 'l':
						System.out.println("Secrets: ");
						googlesecrets.listSecrets();
						System.out.println();
						break;
					case 'c':
						secretName = prompt("Secret Name", ":", "TestSecret");
						data = prompt("Secret Data", ":", "Test");

						googlesecrets.setSecretData(data);
						googlesecrets.createSecret(secretName);

						System.out.println("Secret Created Successfully");
						break;
					case 'd':
						secretName = prompt("Secret Name", ":", "TestSecret");
						googlesecrets.deleteSecret(secretName);
						System.out.println("Secret Deleted Successfully");
						break;
					case 'v':
						secretName = prompt("Secret Name", ":", "TestSecret");
						googlesecrets.getSecret(secretName, "");

						System.out.println("Secret Data: " + new String(googlesecrets.getSecretData()));
						break;
					case '?':
						DisplayMenu();
						break;
					case 'q':
						return;
					default:
						System.out.println("Command Not Recognized.");
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static void DisplayMenu() {
			System.out.println("\nGoogleSecrets Commands:");
			System.out.println("l            List Secrets");
			System.out.println("c            Create a Secret");
			System.out.println("d            Delete a Secret");
			System.out.println("v            View a Secret's Data");
			System.out.println("?            Display Options");
			System.out.println("q            Quit");
	}
}

class ConsoleDemo {
  private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

  static String input() {
    try {
      return bf.readLine();
    } catch (IOException ioe) {
      return "";
    }
  }
  static char read() {
    return input().charAt(0);
  }

  static String prompt(String label) {
    return prompt(label, ":");
  }
  static String prompt(String label, String punctuation) {
    System.out.print(label + punctuation + " ");
    return input();
  }

  static String prompt(String label, String punctuation, String defaultVal)
  {
	System.out.print(label + " [" + defaultVal + "] " + punctuation + " ");
	String response = input();
	if(response.equals(""))
		return defaultVal;
	else
		return response;
  }

  static char ask(String label) {
    return ask(label, "?");
  }
  static char ask(String label, String punctuation) {
    return ask(label, punctuation, "(y/n)");
  }
  static char ask(String label, String punctuation, String answers) {
    System.out.print(label + punctuation + " " + answers + " ");
    return Character.toLowerCase(read());
  }

  static void displayError(Exception e) {
    System.out.print("Error");
    if (e instanceof CloudKeysException) {
      System.out.print(" (" + ((CloudKeysException) e).getCode() + ")");
    }
    System.out.println(": " + e.getMessage());
    e.printStackTrace();
  }
}





/*
 * Cloud Keys 2022 Java Edition- Demo Application
 *
 * Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
 *
 */

import java.io.*;

import cloudkeys.*;


public class googlekms extends ConsoleDemo{

	static String command; // user's command
	static String buffer; // text buffer
	static String[] argument; // arguments following command

	static String keyName;
	static String keyPurpose;
	static String algorithm;

	public static void main(String[] args) {

		Googlekms googlekms = new Googlekms();
		try {
			googlekms.addGooglekmsEventListener(new DefaultGooglekmsEventListener(){

				@Override
				public void error(GooglekmsErrorEvent e) {
					System.out.println("Error [" + e.errorCode + "]: " + e.description);
				}

				@Override
				public void keyList(GooglekmsKeyListEvent e) {
					System.out.println("   " + e.name);
				}
			});

			System.out.print("Press Enter to Authenticate.");
			input();

			googlekms.getOAuth().setClientId("157623334268.apps.googleusercontent.com");
			googlekms.getOAuth().setClientSecret("v8R8R90hn_LchsVc0Ta6Sy0D");
			googlekms.getOAuth().setAuthorizationScope("https://www.googleapis.com/auth/cloud-platform");
			googlekms.getOAuth().setServerAuthURL("https://accounts.google.com/o/oauth2/auth");
			googlekms.getOAuth().setServerTokenURL("https://accounts.google.com/o/oauth2/token");

			googlekms.authorize();
			System.out.println("Authentication Successful.");

			googlekms.setGoogleProjectId(prompt("Google Cloud Project ID"));

			googlekms.setKeyRing(prompt("Key Ring Name (If the key ring does not exist, it will be created)", ":", "CloudKeysDemo"));
			googlekms.listKeyRings();

			boolean exists = false;
			for (int i = 0; i < googlekms.getKeyRings().size(); i++) {
				if (googlekms.getKeyRing().equals(googlekms.getKeyRings().item(i).getName())) {
					System.out.println("Key Ring Found.");
					exists = true;
				}
			}

			if (!exists){
				System.out.println("Creating a New Key Ring...");
				googlekms.createKeyRing();
			}

			while (true) {
				DisplayMenu();
				System.out.print("> ");
				command = input();
				argument = command.split("\\s");
				if (argument.length == 0)
					continue;
				switch (argument[0].charAt(0)) {
					case 'l':
						System.out.println("Keys: ");
						googlekms.listKeys();
						System.out.println();
						break;
					case 'c':
						keyName = prompt("Key Name", ":", "TestKey");
						System.out.println("Valid Purpose Values:");
						System.out.println("  1: A symmetric key used for encryption and decryption.");
						System.out.println("  2: An asymmetric key used for signing and verification.");
						System.out.println("  3: An asymmetric key used for encryption and decryption.");
						keyPurpose = prompt("Key Purpose", ":", "2");
						algorithm = prompt("Key Algorithm", ":", "RSA_SIGN_PSS_2048_SHA256");

						googlekms.createKey(keyName, Integer.parseInt(keyPurpose), algorithm, false);
						System.out.println("Key Created Successfully");
						break;
					case 'e':
						keyName = prompt("Key Name", ":", "TestKey");
						googlekms.setInputData(prompt("String to Encrypt", ":", "This is an Example!"));
						googlekms.encrypt(keyName, "");
						System.out.println("Encrypted Data: ");
						System.out.println(new String(googlekms.getOutputData()));
						break;
					case 's':
						keyName = prompt("Key Name", ":", "TestKey");
						algorithm = prompt("Signature Algorithm", ":", "SHA256");
						googlekms.setInputData(prompt("String to Sign", ":", "This is an Example!"));

						googlekms.listVersions(keyName);

						googlekms.sign(keyName, googlekms.getVersions().item(0).getVersionId(), algorithm, false);
						System.out.println("Signature Data: ");
						System.out.println(new String(googlekms.getOutputData()));
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
		System.out.println("\nGoogleKeys Commands:");
		System.out.println("l            List Keys");
		System.out.println("c            Create a Key");
		System.out.println("e            Encrypts a String");
		System.out.println("s            Signs a String");
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





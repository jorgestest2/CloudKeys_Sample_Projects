#
# Cloud Keys 2022 Python Edition - Demo Application
#
# Copyright (c) 2023 /n software inc. - All rights reserved. - www.nsoftware.com
#

import sys
import string
from cloudkeys import *

input = sys.hexversion<0x03000000 and raw_input or input

def fireSecretListEvent(e):
  print("  " + e.name)

def display_menu():
  print("\nAzureSecrets Commands:")
  print("l            List Secrets")
  print("c            Create a Secret")
  print("d            Delete a Secret")
  print("v            View a Secret's Data")
  print("?            Display Options")
  print("q            Quit")

def prompt(message, default):
  ret = input(message + " [" + default + "]: ")
  if ret == "":
    return default
  return ret

azuresecrets = AzureSecrets()
azuresecrets.on_secret_list = fireSecretListEvent

try:
  input("Press Enter to Authenticate.")

  azuresecrets.set_o_auth_client_id("ce5c0f06-1f2b-4f98-8abf-73f8aaa2592c")
  azuresecrets.set_o_auth_client_secret("3KqXE.3tm~0-1A~~V6AjSA1Y8a1FI.Fgec")
  azuresecrets.set_o_auth_authorization_scope("offline_access https://vault.azure.net/user_impersonation")
  azuresecrets.set_o_auth_server_auth_url("https://login.microsoftonline.com/common/oauth2/v2.0/authorize")
  azuresecrets.set_o_auth_server_token_url("https://login.microsoftonline.com/common/oauth2/v2.0/token")
  azuresecrets.config("OAuthWebServerPort=7777")
  
  azuresecrets.authorize()

  print("Authentication Successful.")

  azuresecrets.set_vault(input("AzureSecrets Vault Name: "))

  while True:
    display_menu()
    command = input("> ")

    if command == "l":
      print("Secrets: ")
      azuresecrets.list_secrets()
      print()

    elif command == "c":
      name = prompt("Secret Name", "TestSecret")
      type = prompt("Secret Type", "Test")
      data = prompt("Secret Data", "123")

      azuresecrets.set_secret_data(data)
      azuresecrets.create_secret(name, type)

      print("Secret Created Successfully.")

    elif command == "d":
      name = prompt("Secret Name", "TestSecret")

      azuresecrets.delete_secret(name)

      print("Secret Deleted Successfully.")

    elif command == "v":
      name = prompt("Secret Name", "TestSecret")
      azuresecrets.get_secret(name)

      print("Secret Data: " + str(azuresecrets.get_secret_data()))

    elif command == "?":
      continue
    elif command == "q":
      break
    else:
      print("Command not recognized.")

  
except CloudKeysError as e:
    print("ERROR %s"%e.message)
    sys.exit()  



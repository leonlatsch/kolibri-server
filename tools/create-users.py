#!/usr/bin/python3

import requests
import sys
import os
import json

url = "https://bfdc99b120cd49e0e1a18dc8267afa3e:6eb77586c6fbfd1280412db3bf0e103f@olivia.leonlatsch.de/users/register"

def post(username, email, password):
    print("Creating user: " + username)
    r = requests.post(url, json={"username": username, "email": email, "password": password}, verify=False)
    if r.status_code != 200:
        print("ERROR: " + str(r.status_code) + " " + str(r.content))

def main():

    requests.packages.urllib3.disable_warnings()

    if len(sys.argv) < 1:
        print("Provide json file as parameter")
        exit()
    
    print("")
    print("Posting users from " + sys.argv[1] + " to: "  + url)
    print("")

    with open(sys.argv[1]) as file:
        lines = file.readlines()

        for line in lines:
            o = json.loads(line)
            post(o["username"], o["email"], o["password"])

if __name__ == "__main__":
    main()

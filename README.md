# Olivia Backend REST API Documentation 1.0 SNAPSHOT
----

### Request Pattern

`https://{token}:{key}@{host}:{port}/{module}/{function}`

- **Token:** The API token is the first part of the authentication and acts as the username.
- **Key:** The API key is the second part of authentication and acts as the password.
- **Host:** The host is the IP address or domain name of the server running the REST API.
- **Port:** The port the api is running on. Default is **7443**
- **Module:** The module is the first endpoint and contains functions.
- **Function:** A function is a specified function of the API such as user authentication

> Note: SSL Cert is self signed, so disable verification.

----



### Possible Responses

| Code               | Meaning                                              |
| ------------------ | ---------------------------------------------------- |
| 200 OK             | Everything went great.                               |
| 201 No Content     | Everything went great, but there is nothing to show. |
| 401 Not Authorized | You are not authorized.                              |
| 500 Internal Error | A heavy bug or error happend at the backend.         |

----



### Modules



#### User Module

**Endpoint:** `/users/{function}`



**Description:**

Used to access, create, update or delete all user related information such as email address, username, profile picture, etc.



**Json keys for a user entity:**

| Key          | Usage                                |
| ------------ | ------------------------------------ |
| uid          | Unique id for the user.              |
| username     | The users username.                  |
| email        | The users email address.             |
| passwordHash | The hashcode for the users password. |
| profilePic   | The Base64 encoded profile picture,  |



#### User Module Functions:

----

#### Get all users

**Endpoint:** `/users`

**Method:** GET



**Description:**

Returns all registered users as a json list.



**Example:**:

Request: `GET https://....../users`

Response:

200 OK

```json
[
    {
        "uid": 22489676,
        "username": "sobeir",
        "email": "sobeir@webmail.de",
        "profilePic": "{BASE64 IMAGE}"
    },
    {
        "uid": 91576168,
        "username": "leon",
        "email": "leonlatsch@gmx.de",
        "profilePic": "{BASE64 IMAGE}"
    }
]
```



----

#### Get User By Uid

**Endpoint:** `/users/getByUid/{uid}`

**Method:** GET



**Description:**

Returns one user by it's uid, if there is one.



**Example:**

Request: `GET https://....../users/getByUid/22489676`

Response: 200 OK

```json
{
    "uid": 22489676,
    "username": "sobeir",
    "email": "sobeir@webmail.de",
    "profilePic": "{BASE64 IMAGE}"
}
```



----

#### Get User By Email

**Endpoint:** `/users/getByEmail/{email}`

**Method:** GET



**Description:**

Returns one user by it's email address, if there is one.

Works the same way as "Get User By Uid".



**Example:**

Request: `GET https://....../users/getByEmail/leonlatsch@gmx.de`

Response: 200 OK

```json
{
    "uid": 91576168,
    "username": "leon",
    "email": "leonlatsch@gmx.de",
    "profilePic": "{BASE64 IMAGE}"
}
```



----

#### Search Users By Username

**Endpoint:** `/users/search/{username}`

**Method:** GET



**Description:**

Returns a list of users that contain the parsed username in their username.

Used to search for users .



**Example:**

Request: `https://....../users/search/leo`

Response: 200 OK

```json
{
    "uid": 91576168,
    "username": "leon",
    "email": "leonlatsch@gmx.de",
    "profilePic": "{BASE64 IMAGE}"
}
```



----

#### Create User

**Endpoint:** `/users/register`

**Method:** POST



**Description:**

Creates a new user with a default profile picture and a random uid.

Information required: **username, email** and the **sha256 hashcode** of the password.



**Example:**

Request: `POST https://....../users/register`

```json
{
    "username": "peter",
    "email": "peter@gmail.com",
    "passwordHash": "a2e51fe52cf2740e99e67139d3ef2af7587537848076251f3822fbcde387b6bc"
}
```

Response: 200 OK

```json
{"message": "OK"}
```



----

#### Update User

**Endpoint:** `/users/update`

**Method:** PUT



**Description:**

Updates an existing user. **Doesn't** create a new one if there is none.

Identifies a user by the provided **uid** and overrides all provided properties.

If there is **no uid** provided, you will get an **error**.



**Example:**

Request: `PUT https://....../users/update`

```jso
{
	"uid": 91576168,
	"email": "leon.latsch@roqqio.com"
}
```

Response: 200 OK

```json
{"message": "OK"}
```



----

#### Delete User

**Endpoint:** `/users/delete/{uid}`

**Method:** DELETE



**Description:**

Delete a user by it's uid.



**Example:**

Request: `DELETE https://....../users/delete/91576168`

Response: 200 OK

```json
{"message": "OK"}
```



----

#### Check Username

**Endpoint:** `/users/checkUsername/{username}`

**Method:** GET



**Description:** 

Check if a username is already taken.



**Example:**

Request: `GET https://....../users/checkUsername/therock`

Response: 200 OK

```json
{"message": "FREE"}
```



----

#### Check Email

**Endpoint:** `/users/checkEmail/{email}`

**Method:** GET



**Description:**

Check if an email address is already taken. Same as Check User.



**Example:**

Request: `GET https://....../users/checkEmail/barney@stinson.com`

Response: 200 OK

```json
{"message": "TAKEN"}
```



----

#### Authenticate User By Email

**Endpoint:** `/users/auth`

**Method:** GET



**Description:**

Authenticate a user by it's email address and it's password hashcode.



**Example:**

Request: `GET https://....../users/auth`

```json
{
    "email": "leonlatsch@gmx.de",
    "passwordHash": "79c48187356a074b3028ab5ec3b38f8b16f74d967dde4224287376158c0ec58d"
}
```

Response: 200 OK

```json
{"message": "AUTH_OK"}
```




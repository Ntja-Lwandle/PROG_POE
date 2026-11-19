# QuickChat – Programming 1A Portfolio of Evidence

## Project Overview

QuickChat is a Java console-based messaging application developed for the PROG5121 Programming 1A Portfolio of Evidence.

The application allows users to register, log in, and manage messages through a numeric menu. It demonstrates Java programming concepts including object-oriented programming, validation, conditional statements, loops, string manipulation, file handling, and unit testing.

## Technologies Used

- Java
- Apache NetBeans
- Apache Maven
- JUnit 5
- Git and GitHub
- GitHub Actions
- JSON file storage

## Part 1: Registration and Login

The registration system requires a first name, last name, username, password, and South African cellphone number.

The program validates the following:

- Username contains an underscore and is no more than five characters long.
- Password contains at least eight characters, an uppercase letter, a number, and a special character.
- Cellphone number follows the required international format.

Only users who log in successfully can access the messaging menu.

## Part 2: Sending Messages

After logging in, the user is welcomed to QuickChat and enters the number of messages they wish to process.

The menu offers three options:

1. Send Messages
2. Show recently sent messages – Coming Soon
3. Quit

## Part 3: Stored Messages and Reports

The final part of QuickChat introduces message management using arrays
and JSON storage.

The application maintains arrays for:

- Sent messages
- Disregarded messages
- Stored messages
- Message hashes
- Message IDs

The Stored Messages menu allows users to:

1. Display sender and recipient details.
2. Find the longest stored message.
3. Search for a message using its ID.
4. Search for messages by recipient.
5. Delete a stored message using its hash.
6. Display a message report.

Stored messages are saved and retrieved using a JSON file.

### Message Features

- A while loop keeps the menu running until the user quits.
- A for loop limits the number of messages entered.
- Each message receives a randomly generated ten-digit message ID.
- Messages are assigned sequential numbers.
- Recipient cellphone numbers are validated.
- Messages must contain no more than 250 characters.
- A message hash is generated using the first two digits of the message ID, the message number, and the first and last words of the message.
- Users can send, disregard, or store messages for later.
- Stored messages are saved to a JSON file.
- The application displays the total number of messages successfully sent.

### Message Hash Example

Message ID: `0012345678`

Message Number: `0`

Message: `Hi Mike, can you join us for dinner tonight?`

Generated Hash: `00:0:HITONIGHT`

## Project Structure
QuickChat/
├── .github/
│   └── workflows/
│       └── TestJava.yml
├── src/
│   ├── main/
│   │   └── java/quickchat/
│   │       ├── QuickChat.java
│   │       ├── Login.java
│   │       ├── Message.java
│   │       ├── MessageManager.java
│   │       └── MessageStorage.java
│   └── test/
│       └── java/quickchat/
│           ├── LoginTest.java
│           ├── MessageTest.java
│           └── MessageManagerTest.java
├── stored-messages.json
├── pom.xml
└── README.md

## How to Run the Application

1. Open the project in Apache NetBeans.
2. Right-click the project and select Clean and Build.
3. Run the QuickChat main class.
4. Register a user.
5. Log in with the registered username and password.
6. Enter the number of messages to process.
7. Select the appropriate menu options.

## Unit Testing

JUnit 5 is used to test the registration, login, message validation,
message ID generation, message hash generation, message actions,
array population, message searching, deletion and JSON storage.

The tests can be executed in NetBeans by selecting Test.

Alternatively, run:

mvn clean verify

GitHub Actions is configured to automatically build the project
and execute the unit tests when changes are pushed to GitHub.

## Version Control

Git is used to track changes to the project. Part 2 development is managed using a dedicated feature branch and meaningful commits.

## AI Usage Disclosure

I used AI tools while working on this PoE:

- ChatGPT: helped me understand the assessment requirements, explain Java concepts, find coding errors and plan my testing.
- Claude (Anthropic): used to help me simplify and tidy my code (removing duplicate methods, shortening the main class, rewriting comments and fixing the registration message wording)

## References

Apache Software Foundation (n.d.) *Maven Getting Started Guide*. Available at: https://maven.apache.org/guides/getting-started/index.html (Accessed: 10 October 2026).

Bray, T. (2017) *The JavaScript Object Notation (JSON) Data Interchange Format*. RFC 8259. Internet Engineering Task Force. Available at: https://www.rfc-editor.org/rfc/rfc8259 (Accessed: 18 September 2026).

Google (n.d.) *Gson User Guide*. Available at: https://github.com/google/gson/blob/main/UserGuide.md (Accessed: 10 October 2026).

GitHub (n.d.) *Building and testing Java with Maven*. GitHub Docs. Available at: https://docs.github.com/en/actions/tutorials/build-and-test-code/java-with-maven (Accessed: 5 September 2026).

JUnit Team (n.d.) *JUnit 5 User Guide*. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 25 September 2026).

Oracle (n.d.) *Class Pattern: Java Platform, Standard Edition API Specification*. Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html (Accessed: 10 October 2026).

Oracle (n.d.) *Class SecureRandom: Java Platform, Standard Edition API Specification*. Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/SecureRandom.html (Accessed: 10 October 2026).

Oracle (n.d.) *Class Scanner: Java Platform, Standard Edition API Specification*. Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html (Accessed: 10 October 2026).

Oracle (n.d.) *Interface Path: Java Platform, Standard Edition API Specification*. Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Path.html (Accessed: 10 October 2026).

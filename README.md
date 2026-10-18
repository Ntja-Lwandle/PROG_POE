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

```text
QuickChat/
├── src/
│   ├── main/java/quickchat/
│   │   ├── QuickChat.java
│   │   ├── Login.java
│   │   └── Message.java
│   └── test/java/quickchat/
│       ├── LoginTest.java
│       └── MessageTest.java
├── .github/workflows/
│   └── TestJava.yml
├── pom.xml
└── README.md
```

## How to Run the Application

1. Open the project in Apache NetBeans.
2. Right-click the project and select Clean and Build.
3. Run the QuickChat main class.
4. Register a user.
5. Log in with the registered username and password.
6. Enter the number of messages to process.
7. Select the appropriate menu options.

## Unit Testing

JUnit 5 tests are used to validate registration, login, message length, recipient numbers, message IDs, hashes, message actions, and JSON storage.

The most recent local test run completed with:

- Tests run: 26
- Failures: 0
- Errors: 0
- Skipped: 0

To run the tests in NetBeans, right-click the project and select Test.

Alternatively, run:

`mvn clean test`

GitHub Actions is configured to execute the Maven tests when changes are pushed to GitHub.

## Version Control

Git is used to track changes to the project. Part 2 development is managed using a dedicated feature branch and meaningful commits.

## AI Usage Disclosure

ChatGPT was used to assist with understanding the assessment requirements, explaining Java concepts, identifying coding errors, suggesting improvements, and preparing testing and documentation guidance. The student remains responsible for reviewing the implementation, verifying functionality, and accurately disclosing the assistance received.

## References

Internet Engineering Task Force (2017) *The JavaScript Object Notation (JSON) Data Interchange Format*. RFC 8259. Available at: https://www.rfc-editor.org/rfc/rfc8259 (Accessed: 18 October 2026).
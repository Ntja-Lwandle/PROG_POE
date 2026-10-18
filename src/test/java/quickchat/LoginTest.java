package quickchat;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class.
 *
 * These tests verify username validation, password complexity,
 * cellphone number validation, registration, login authentication,
 * and login status messages using the required project test data.
 *
 * @author lwandle
 */
public class LoginTest {

     private Login createUser(String username,
                             String password,
                             String phone) {

        return new Login(
                username,
                password,
                phone,
                "John",
                "Smith"
        );
    }

    @Test
    public void testValidUsername() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkUserName());
    }

    @Test
    public void testInvalidUsername() {

        Login user = createUser(
                "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"
        );

        assertFalse(user.checkUserName());
    }

    @Test
    public void testValidPassword() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testInvalidPassword() {

        Login user = createUser(
                "kyl_1", "password", "+27838968976"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testValidPhone() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testInvalidPhone() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "08966553"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testRegistrationSuccess() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.",
                user.registerUser()
        );
    }

    @Test
    public void testLoginSuccess() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(
                user.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    @Test
    public void testLoginFailure() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertFalse(
                user.loginUser("wrong", "wrongPassword")
        );
    }

    @Test
    public void testSuccessfulLoginStatus() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Welcome John, Smith it is great to see you again.",
                user.returnLoginStatus(true)
        );
    }

    @Test
    public void testFailedLoginStatus() {

        Login user = createUser(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                user.returnLoginStatus(false)
        );
    }

}
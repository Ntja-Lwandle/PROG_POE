package quickchat;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class.
 *
 * @author lwand
 */
public class LoginTest {

 /**
 * Unit tests for the Login class.
 *
 * These tests verify username validation, password complexity,
 * cellphone number validation, registration, login authentication,
 * and login status messages using the required project test data.
 *
 * @author lwand
 */
    @Test
    public void testCheckUserNameMaximumLenght() {

        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertTrue(login.checkUserName());
    }

    /**
     * Tests an incorrectly formatted username.
     */
    @Test
    public void testCheckUserNameInvalid() {

        Login instance = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertFalse(instance.checkUserName());
    }

    /**
     * Tests a password that meets the complexity requirements.
     */
    @Test
    public void testCheckPasswordComplexityValid() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertTrue(instance.checkPasswordComplexity());
    }

    /**
     * Tests a password that does not meet the complexity requirements.
     */
    @Test
    public void testCheckPasswordComplexityInvalid() {

        Login instance = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "John",
                "Smith"
        );

        assertFalse(instance.checkPasswordComplexity());
    }

    /**
     * Tests a correctly formatted South African cellphone number.
     */
    @Test
    public void testCheckCellPhoneNumberValid() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertTrue(instance.checkCellPhoneNumber());
    }

    /**
     * Tests an incorrectly formatted cellphone number.
     */
    @Test
    public void testCheckCellPhoneNumberInvalid() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "John",
                "Smith"
        );

        assertFalse(instance.checkCellPhoneNumber());
    }

    /**
     * Tests successful user registration.
     */
    @Test
    public void testRegisterUserValid() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        String expected =
                "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";

        assertEquals(expected, instance.registerUser());
    }

    /**
     * Tests successful login.
     */
    @Test
    public void testLoginUserSuccessful() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertTrue(
                instance.loginUser(
                        "kyl_1",
                        "Ch&&sec@ke99!"
                )
        );
    }

    /**
     * Tests unsuccessful login.
     */
    @Test
    public void testLoginUserFailed() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        assertFalse(
                instance.loginUser(
                        "wrong",
                        "wrongPassword"
                )
        );
    }

    /**
     * Tests the successful login status message.
     */
    @Test
    public void testReturnLoginStatusSuccessful() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        String expected =
                "Welcome John, Smith it is great to see you again.";

        assertEquals(
                expected,
                instance.returnLoginStatus(true)
        );
    }

    /**
     * Tests the failed login status message.
     */
    @Test
    public void testReturnLoginStatusFailed() {

        Login instance = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "John",
                "Smith"
        );

        String expected =
                "Username or password incorrect, please try again.";

        assertEquals(
                expected,
                instance.returnLoginStatus(false)
        );
    }
}

package test.java.com.ege.reservation.util;

import com.ege.reservation.util.InputValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputValidator utility class
 * Testing basic validation methods
 */
class InputValidatorTest {

    @Test
    void testIsValidPassword_ValidPassword() {
        // Arrange & Act & Assert
        assertTrue(InputValidator.isValidPassword("123456"), "Password with 6 characters should be valid");
        assertTrue(InputValidator.isValidPassword("password123"), "Longer password should be valid");
        assertTrue(InputValidator.isValidPassword("abcdef"), "Password with exactly 6 characters should be valid");
    }

    @Test
    void testIsValidPassword_InvalidPassword() {
        // Arrange & Act & Assert
        assertFalse(InputValidator.isValidPassword("12345"), "Password with less than 6 characters should be invalid");
        assertFalse(InputValidator.isValidPassword(""), "Empty password should be invalid");
    }

    @Test
    void testIsValidUsername_ValidUsername() {
        // Arrange & Act & Assert
        assertTrue(InputValidator.isValidUsername("abc"), "Username with 3 characters should be valid");
        assertTrue(InputValidator.isValidUsername("user123"), "Longer username should be valid");
        assertTrue(InputValidator.isValidUsername("   test   "), "Username with spaces should be valid (trimmed)");
    }

    @Test
    void testIsValidUsername_InvalidUsername() {
        // Arrange & Act & Assert
        assertFalse(InputValidator.isValidUsername("ab"), "Username with less than 3 characters should be invalid");
        assertFalse(InputValidator.isValidUsername(""), "Empty username should be invalid");
        assertFalse(InputValidator.isValidUsername("   "), "Whitespace-only username should be invalid");
        assertFalse(InputValidator.isValidUsername(null), "Null username should be invalid");
    }

    @Test
    void testIsValidEmail_ValidEmail() {
        // Arrange & Act & Assert
        assertTrue(InputValidator.isValidEmail("test@example.com"), "Standard email should be valid");
        assertTrue(InputValidator.isValidEmail("user@domain.org"), "Email with .org should be valid");
        assertTrue(InputValidator.isValidEmail("a@b.c"), "Minimal email should be valid");
    }

    @Test
    void testIsValidEmail_InvalidEmail() {
        // Arrange & Act & Assert
        assertFalse(InputValidator.isValidEmail("test@example"), "Email without dot should be invalid");
        assertFalse(InputValidator.isValidEmail("testexample.com"), "Email without @ should be invalid");
        assertFalse(InputValidator.isValidEmail(""), "Empty email should be invalid");
        assertFalse(InputValidator.isValidEmail(null), "Null email should be invalid");
    }

    @Test
    void testIsNotEmpty_ValidInput() {
        // Arrange & Act & Assert
        assertTrue(InputValidator.isNotEmpty("hello"), "Non-empty string should be valid");
        assertTrue(InputValidator.isNotEmpty("   test   "), "String with content should be valid");
        assertTrue(InputValidator.isNotEmpty("123"), "Numeric string should be valid");
    }

    @Test
    void testIsNotEmpty_InvalidInput() {
        // Arrange & Act & Assert
        assertFalse(InputValidator.isNotEmpty(""), "Empty string should be invalid");
        assertFalse(InputValidator.isNotEmpty("   "), "Whitespace-only string should be invalid");
        assertFalse(InputValidator.isNotEmpty(null), "Null string should be invalid");
    }
} 
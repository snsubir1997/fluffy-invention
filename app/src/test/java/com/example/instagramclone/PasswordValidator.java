package com.example.instagramclone;

import com.example.instagramclone.LoginActivityFragments.RegisterFragment;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class PasswordValidator {

    @Test
    public void validatePassword_EmptyString() {
        // setup
        String password = "";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(password);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validatePassword_Missing_OneNumber() {
        // setup
        String password = "Abcdefg#";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(password);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validatePassword_Missing_OneSymbol() {
        // setup
        String password = "Abcdefg5";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(password);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validatePassword_AllRulesMet() {
        // setup
        String password = "Abcdefg5#";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(password);

        // assert
        assertTrue(actual);
    }

    @Test
    public void validatePassword_LengthShort() {
        // setup
        String password = "Abc#1";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(password);

        // assert
        assertFalse(actual);
    }
}

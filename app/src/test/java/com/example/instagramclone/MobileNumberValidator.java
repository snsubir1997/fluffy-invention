package com.example.instagramclone;

import com.example.instagramclone.LoginActivityFragments.RegisterFragment;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class MobileNumberValidator {

    @Test
    public void validateMobileNumber_EmptyString() {
        // setup
        String mob_no = "";

        // execute
        boolean actual = RegisterFragment.isGoodMobileNumber(mob_no);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validateMobileNumber_StarHash() {
        // setup
        String mob_no = "*#";

        // execute
        boolean actual = RegisterFragment.isGoodMobileNumber(mob_no);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validatePassword_AllGoodl() {
        // setup
        String mob_no = "7992305913";

        // execute
        boolean actual = RegisterFragment.isGoodMobileNumber(mob_no);

        // assert
        assertTrue(actual);
    }

    @Test
    public void validateMobileNumber_LengthShort() {
        // setup
        String mob_no = "799230";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(mob_no);

        // assert
        assertFalse(actual);
    }

    @Test
    public void validateMobileNumber_WrongNumberst() {
        // setup
        String mob_no = "1234567890";

        // execute
        boolean actual = RegisterFragment.isGoodPassword(mob_no);

        // assert
        assertFalse(actual);
    }
}

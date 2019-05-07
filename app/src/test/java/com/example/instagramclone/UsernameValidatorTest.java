package com.example.instagramclone;

import com.example.instagramclone.LoginActivityFragments.RegisterFragment;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class UsernameValidatorTest {

    @Test
    public void usernameValidator_CorrectNameSimple_ReturnsTrue() {
        assertTrue(RegisterFragment.isUsername("snsubir"));
    }
    @Test
    public void usernameValidator_CorrectNameSimple_SmallCase_ReturnsTrue() {
        assertTrue(RegisterFragment.isUsername("snsubir1997"));
    }
    @Test
    public void usernameValidator_CorrectNameSimple_SmallCase_Underscore_ReturnsTrue() {
        assertTrue(RegisterFragment.isUsername("subir_97"));
    }
    @Test
    public void usernameValidator_CorrectNameSimple_Dot_ReturnsTrue() {
        assertTrue(RegisterFragment.isUsername("sn.subir1997"));
    }
    @Test
    public void usernameValidator_InCorrectNameSpecialCharacter_ReturnsFalse() {
        assertFalse(RegisterFragment.isUsername("sub@Ndnd"));
    }
    @Test
    public void usernameValidator_InCorrectSpace_ReturnsFalse() {
        assertFalse(RegisterFragment.isUsername("subir nand1"));
    }
    @Test
    public void usernameValidator_InCorrectEmpty_ReturnsFalse() {
        assertFalse(RegisterFragment.isUsername(""));
    }
}

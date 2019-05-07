package com.example.instagramclone;

import com.example.instagramclone.LoginActivityFragments.RegisterFragment;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class NameValidatorTest {

    @Test
    public void nameValidator_CorrectNameSimple_ReturnsTrue() {
        assertTrue(RegisterFragment.isFullname("Subir Nandi"));
    }
    @Test
    public void nameValidator_CorrectNameSimple_SmallCase_ReturnsTrue() {
        assertTrue(RegisterFragment.isFullname("subir nandi"));
    }
    @Test
    public void nameValidator_InCorrectNameSpecialCharacter_ReturnsFalse() {
        assertFalse(RegisterFragment.isFullname("sub@Ndnd"));
    }
    @Test
    public void nameValidator_InCorrectNameNumber_ReturnsFalse() {
        assertFalse(RegisterFragment.isFullname("subir nand1"));
    }
    @Test
    public void nameValidator_InCorrectNameNumber_SpecialCharacter_ReturnsFalse() {
        assertFalse(RegisterFragment.isFullname("35#$%"));
    }
    @Test
    public void nameValidator_InCorrectNameEmpty_ReturnsFalse() {
        assertFalse(RegisterFragment.isFullname(""));
    }
}


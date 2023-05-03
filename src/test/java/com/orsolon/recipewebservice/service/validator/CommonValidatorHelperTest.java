package com.orsolon.recipewebservice.service.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Common Validator Helper Test")
public class CommonValidatorHelperTest {

    @Test
    @DisplayName("Cast list should cast list to expected type and return result")
    public void castList_ShouldCastListToExpectedTypeAndReturnResult() {
        List<?> input = List.of(1, 2, 3);
        List<Integer> result = CommonValidatorHelper.castList(input, Integer.class);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("Cast list should throw exception when list contains invalid values")
    public void castList_ShouldThrowExceptionWhenListContainsInvalidValues() {
        List<?> input = List.of("1", 2, "3");
        assertThrows(ClassCastException.class, () -> CommonValidatorHelper.castList(input, Integer.class));
    }

    @Test
    @DisplayName("Sanitize string value should return empty string when null")
    public void sanitizeStringValue_ShouldReturnEmptyStringWhenNull() {
        String input = "<script>alert('XSS');</script>";
        String expected = "";
        String result = CommonValidatorHelper.sanitizeStringValue(input);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Sanitize string value should sanitize valid string and return result")
    public void sanitizeStringValue_ShouldSanitizeValidStringAndReturnResult() {
        String input = " <b>Test String</b> ";
        String expected = "Test String";
        String result = CommonValidatorHelper.sanitizeStringValue(input);
        assertEquals(expected, result);
    }
}


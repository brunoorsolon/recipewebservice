package com.orsolon.recipewebservice.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommonValidatorHelperTest {

    @Test
    public void sanitizeStringValue_good() {
        String input = " <b>Test String</b> ";
        String expected = "Test String";
        String result = CommonValidatorHelper.sanitizeStringValue(input);
        assertEquals(expected, result);
    }

    @Test
    public void sanitizeStringValue_bad() {
        String input = "<script>alert('XSS');</script>";
        String expected = "";
        String result = CommonValidatorHelper.sanitizeStringValue(input);
        assertEquals(expected, result);
    }

    @Test
    public void castList_good() {
        List<?> input = List.of(1, 2, 3);
        List<Integer> result = CommonValidatorHelper.castList(input, Integer.class);
        assertEquals(input, result);
    }

    @Test
    public void castList_bad() {
        List<?> input = List.of("1", 2, "3");
        assertThrows(ClassCastException.class, () -> CommonValidatorHelper.castList(input, Integer.class));
    }
}


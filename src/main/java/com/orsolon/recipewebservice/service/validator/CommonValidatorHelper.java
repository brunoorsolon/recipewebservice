package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonValidatorHelper {
    public static String sanitizeStringValue(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none()).trim();
    }

    public static <T> List<T> castList(List<?> list, Class<T> elementType) {
        if (list.stream().allMatch(elementType::isInstance)) {
            return (List<T>) list;
        }
        throw new ClassCastException("List elements are not of the expected type.");
    }

    public static void validateIdParameter(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidFieldValueException("Invalid value: " + id);
        }
    }

    public static void validateStringParameter(String string) {
        if (string == null || string.isEmpty()) {
            throw new InvalidFieldValueException("Invalid value: " + string);
        }
    }
}

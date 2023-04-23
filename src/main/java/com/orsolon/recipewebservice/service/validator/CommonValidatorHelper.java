package com.orsolon.recipewebservice.service.validator;

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
}

package com.github.tavi.lilbird.util;

import java.util.Locale;

import org.springframework.core.convert.converter.Converter;

import jakarta.persistence.AttributeConverter;


/**
 * This converter defines the standard for string/locale conversion
 * during database and HTTP operations.
 */
public class LocaleConverter implements 
    AttributeConverter<Locale,String>,      // Hibernate
    Converter<String,Locale>                // MVC (JSON)
{

    @Override
    public String convertToDatabaseColumn(Locale attribute) {
        return localeToString(attribute);
    }

    @Override
    public Locale convertToEntityAttribute(String dbData) {
        return stringToLocale(dbData);
    }

    @Override
    public Locale convert(String source) {
        return stringToLocale(source);
    }


    private String localeToString(Locale locale) {
        return locale.toLanguageTag();
    }

    private Locale stringToLocale(String locale) {
        return Locale.forLanguageTag(locale);
    }

}

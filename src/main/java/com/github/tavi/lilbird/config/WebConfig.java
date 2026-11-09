package com.github.tavi.lilbird.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.tavi.lilbird.util.LocaleConverter;
import com.github.tavi.lilbird.util.enums.TaxonType.TaxonTypeConverter;


/** Web configuration bean. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // App-specific converters
        registry.addConverter(new TaxonTypeConverter());
        registry.addConverter(new LocaleConverter());
    }
    
}

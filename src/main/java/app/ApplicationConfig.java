package app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// This is a meta-annotation composed of @ConfigurationProperties, @ConstructorBinding
// and @Validated. Those annotations could be directly applied to the configuration class.
@ConfigurationProperties(prefix = JsonPropertySourceFactory.DEFAULT_PREFIX)
@ConstructorBinding
@Validated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationConfig {
}

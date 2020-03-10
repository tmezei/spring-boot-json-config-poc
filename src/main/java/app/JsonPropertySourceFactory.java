package app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonPropertySourceFactory implements PropertySourceFactory {

    public static final String DEFAULT_PREFIX = "default";

    private static final TypeReference<Map<String, Object>> MAP = new TypeReference<>() {};

    @SuppressWarnings("unchecked")
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String prefix = name == null ? DEFAULT_PREFIX : name;

        var props = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            .readValue(resource.getInputStream(), MAP);

        var expandableKeys = new ArrayList<String>();
        for (var e : props.entrySet()) {
            if (e.getValue() instanceof Map) {
               expandableKeys.add(e.getKey());
            }
        }

        for (var k : expandableKeys) {
            for (var e : ((Map<String, Object>) props.remove(k)).entrySet()) {
                props.put(String.format("%s.%s", k, e.getKey()), e.getValue());
            }
        }

        var prefixedProps = props
            .entrySet()
            .stream()
            .collect(Collectors.toMap(e -> prefix + "." + e.getKey(), Map.Entry::getValue));
        prefixedProps.putAll(props); // so that the values can be injected without the prefix

        return new MapPropertySource(prefix, prefixedProps);
    }
}

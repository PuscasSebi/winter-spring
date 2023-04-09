package com.puscas.authentication;

import com.puscas.authentication.model.Place;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
@RunWith(SpringRunner.class)
@JsonTest
public class MyJsonTests {
    @Autowired
    private JacksonTester<Place> json;
    final public static String placeCreator = "Puscasa";
    final String description = "description";
    final String placeImage = "image/url";
    final String name = "Aname";
    final String metadata = "[\"isPrimi\"]";
    public Place details = new Place(1, placeCreator, name, description, placeImage, metadata);
    @Test
    public void testSerialize() throws Exception {
        ClassPathResource resource = new ClassPathResource("/expected.json");

        assertThat(this.json.write(details)).isEqualToJson(resource);
        // Or use JSON path based assertions
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.description");
        assertThat(this.json.write(details)).extractingJsonPathStringValue("@.name")
                .isEqualTo("Aname");
    }

    @Test
    public void testDeserialization() throws Exception{
        ClassPathResource resource = new ClassPathResource("/expected.json");
        final ObjectContent<Place> read = this.json.read(resource);
        assertThat(read.getObject()).isEqualTo(details);
    }
}

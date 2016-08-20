package pe.joedayz.campus.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import pe.joedayz.campus.util.EnumBase;

/**
 * Created by awusr on 03/05/2016.
 */
public class JsonEnumSerializer extends JsonSerializer<EnumBase> {

    @Override
    public void serialize (EnumBase value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString((value.getCode()));
    }
}
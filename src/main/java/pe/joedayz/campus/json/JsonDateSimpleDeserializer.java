package pe.joedayz.campus.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by awusr on 03/05/2016.
 */
public class JsonDateSimpleDeserializer extends JsonDeserializer<Date> {
    private SimpleDateFormat formatter =
            new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String dateAsString = p.getText();
        try {
            if (!StringUtils.hasText(dateAsString)) {
                return null;
            }

            return formatter.parse(dateAsString);

        }
        catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

}
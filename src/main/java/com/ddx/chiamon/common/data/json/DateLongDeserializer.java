package com.ddx.chiamon.common.data.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import java.text.ParseException;
import java.io.IOException;

/**
 *
 * @author ddx
 */
public class DateLongDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String dateAsString = jsonparser.getText();
        try {
            if (dateAsString == null || dateAsString.length() == 0) return null;
            Date date = DateLong.getFormatter().parse(dateAsString);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
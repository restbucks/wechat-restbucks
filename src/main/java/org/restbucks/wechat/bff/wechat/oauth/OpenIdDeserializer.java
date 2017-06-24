package org.restbucks.wechat.bff.wechat.oauth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.restbucks.wechat.oauth.OpenId;

public class OpenIdDeserializer extends JsonDeserializer<OpenId> {

    @Override
    public OpenId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        return OpenId.valueOf(jsonParser.getValueAsString());
    }
}

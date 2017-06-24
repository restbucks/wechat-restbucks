package org.restbucks.wechat.bff.wechat.oauth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.restbucks.wechat.oauth.OpenId;

public class OpenIdSerializer extends JsonSerializer<OpenId> {

    @Override
    public void serialize(OpenId value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeString(value.getValue());
    }
}

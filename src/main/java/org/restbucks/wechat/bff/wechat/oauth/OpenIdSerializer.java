package org.restbucks.wechat.bff.wechat.oauth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.hippoom.wechat.oauth.OpenId;
import java.io.IOException;

public class OpenIdSerializer extends JsonSerializer<OpenId> {

    @Override
    public void serialize(OpenId value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeString(value.getValue());
    }
}

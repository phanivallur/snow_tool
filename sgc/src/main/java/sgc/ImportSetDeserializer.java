package sgc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class ImportSetDeserializer extends StdDeserializer<ImportSet> {

	public ImportSetDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ImportSet deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		
		JsonNode node = parser.getCodec().readTree(parser);
		
		String link=node.get("link").asText();
		String value=node.get("value").asText();
		
		ImportSet is=new ImportSet();
		is.setLink(link);
		is.setValue(value);
		
		return is;
	}
}

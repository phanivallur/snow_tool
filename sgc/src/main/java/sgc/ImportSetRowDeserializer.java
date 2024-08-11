package sgc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class ImportSetRowDeserializer extends StdDeserializer<ImportSetRow> {
	
	public ImportSetRowDeserializer(Class<?> vc) {
		super(vc);		
	}

	@Override
	public ImportSetRow deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		
		JsonNode node = parser.getCodec().readTree(parser);
	    //Integer id = (Integer) ((IntNode) node.get("id")).numberValue();
	    String sysRowError = node.get("sys_row_error").asText();
	    String sysTargetTable = node.get("sys_target_table").asText();
	    String sysImportStateComment = node.get("sys_import_state_comment").asText();
	    String sysModCount=node.get("sys_mod_count").asText();
	    String templateImportLog=node.get("template_import_log").asText();
	    String importSetRun=node.get("import_set_run").asText();
	    String sysUpdatedOn=node.get("sys_updated_on").asText();
	    String sysTags=node.get("sys_tags").asText();
	    String sysClassName=node.get("sys_class_name").asText();
	    String sysImportState=node.get("sys_import_state").asText();
	    String sysTargetSysId=node.get("sys_target_sys_id").asText();
	    String sysId=node.get("sys_id").asText();
	    String sysUpdatedBy=node.get("sys_updated_by").asText();
	    String sysCreatedOn=node.get("sys_created_on").asText();
	    
	    String sysImportSet=node.get("sys_import_set").asText();
	    ObjectMapper objectMapper=new ObjectMapper();
		//rootNode = objectMapper.readTree(importSetRowsResponse);
	    ImportSet importSet=objectMapper.readValue(sysImportSet, ImportSet.class);
	    
	    String sysTransformMap=node.get("sys_transform_map").asText();
	    String sysCreatedBy=node.get("sys_created_by").asText();
	    String sysImportRow=node.get("sys_import_row").asText();
	    
		ImportSetRow isr=new ImportSetRow();
		isr.setSys_row_error(sysRowError);
		isr.setSys_target_table(sysTargetTable);
		isr.setSys_import_state_comment(sysImportStateComment);
		isr.setSys_mod_count(sysModCount);
		isr.setTemplate_import_log(templateImportLog);
		isr.setImport_set_run(importSetRun);
		isr.setSys_updated_on(sysUpdatedOn);
		isr.setSys_tags(sysTags);
		isr.setSys_class_name(sysClassName);
		isr.setSys_import_state(sysImportState);
		isr.setSys_target_sys_id(sysTargetSysId);
		isr.setSys_id(sysId);
		isr.setSys_updated_by(sysUpdatedBy);
		isr.setSys_created_on(sysCreatedOn);
		isr.setSys_import_set(importSet);
		isr.setSys_transform_map(sysTransformMap);
		isr.setSys_created_by(sysCreatedBy);
		isr.setSys_import_row(sysImportRow);
		
		return isr;
	}
}

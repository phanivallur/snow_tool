package sgc;

import java.util.Date;

public class OutputTargetItem {
	private String expected_target_table;
	private RunId run_id;
	private SourceRecordId source_record_id;
	private String error_detail;
	private String run_table;
	private int sys_mod_count;
	private Date sys_updated_on;
	private String sys_tags;
	private String warning_detail;
	private String sys_id;
	private String sys_updated_by;
	private Date sys_created_on;
	private String actual_target_table;
	private TargetRecordId target_record_id;
	private String operation;
	private String source_table;
	private String sys_created_by;
	
	
	public String getExpected_target_table() {
		return expected_target_table;
	}
	public void setExpected_target_table(String expected_target_table) {
		this.expected_target_table = expected_target_table;
	}
	public RunId getRun_id() {
		return run_id;
	}
	public void setRun_id(RunId run_id) {
		this.run_id = run_id;
	}
	public SourceRecordId getSource_record_id() {
		return source_record_id;
	}
	public void setSource_record_id(SourceRecordId source_record_id) {
		this.source_record_id = source_record_id;
	}
	public String getError_detail() {
		return error_detail;
	}
	public void setError_detail(String error_detail) {
		this.error_detail = error_detail;
	}
	public String getRun_table() {
		return run_table;
	}
	public void setRun_table(String run_table) {
		this.run_table = run_table;
	}
	public int getSys_mod_count() {
		return sys_mod_count;
	}
	public void setSys_mod_count(int sys_mod_count) {
		this.sys_mod_count = sys_mod_count;
	}
	public Date getSys_updated_on() {
		return sys_updated_on;
	}
	public void setSys_updated_on(Date sys_updated_on) {
		this.sys_updated_on = sys_updated_on;
	}
	public String getSys_tags() {
		return sys_tags;
	}
	public void setSys_tags(String sys_tags) {
		this.sys_tags = sys_tags;
	}
	public String getWarning_detail() {
		return warning_detail;
	}
	public void setWarning_detail(String warning_detail) {
		this.warning_detail = warning_detail;
	}
	public String getSys_id() {
		return sys_id;
	}
	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}
	public String getSys_updated_by() {
		return sys_updated_by;
	}
	public void setSys_updated_by(String sys_updated_by) {
		this.sys_updated_by = sys_updated_by;
	}
	public Date getSys_created_on() {
		return sys_created_on;
	}
	public void setSys_created_on(Date sys_created_on) {
		this.sys_created_on = sys_created_on;
	}
	public String getActual_target_table() {
		return actual_target_table;
	}
	public void setActual_target_table(String actual_target_table) {
		this.actual_target_table = actual_target_table;
	}
	public TargetRecordId getTarget_record_id() {
		return target_record_id;
	}
	public void setTarget_record_id(TargetRecordId target_record_id) {
		this.target_record_id = target_record_id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSource_table() {
		return source_table;
	}
	public void setSource_table(String source_table) {
		this.source_table = source_table;
	}
	public String getSys_created_by() {
		return sys_created_by;
	}
	public void setSys_created_by(String sys_created_by) {
		this.sys_created_by = sys_created_by;
	}
}

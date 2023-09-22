package sgc;

import java.util.Date;

public class ImportSet {
	private String transaction_id;
	private String short_description;
	private int sys_mod_count;
	private Date sys_updated_on;
	private ConcurrentImportSet concurrent_import_set;
	private String sys_tags;
	private String table_name;
	private DataSource data_source;
	private boolean multi_import_set;
	private String mode;
	private String number;
	private String sys_id;
	private String sys_updated_by;
	private Date load_run_time;
	private String creation_source;
	private Date sys_created_on;
	private int max_row_id;
	private ScheduleImport schedule_import;
	private Date load_completed;
	private String state;
	private String sys_created_by;
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
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
	public ConcurrentImportSet getConcurrent_import_set() {
		return concurrent_import_set;
	}
	public void setConcurrent_import_set(ConcurrentImportSet concurrent_import_set) {
		this.concurrent_import_set = concurrent_import_set;
	}
	public String getSys_tags() {
		return sys_tags;
	}
	public void setSys_tags(String sys_tags) {
		this.sys_tags = sys_tags;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public DataSource getData_source() {
		return data_source;
	}
	public void setData_source(DataSource data_source) {
		this.data_source = data_source;
	}
	public boolean isMulti_import_set() {
		return multi_import_set;
	}
	public void setMulti_import_set(boolean multi_import_set) {
		this.multi_import_set = multi_import_set;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
	public Date getLoad_run_time() {
		return load_run_time;
	}
	public void setLoad_run_time(Date load_run_time) {
		this.load_run_time = load_run_time;
	}
	public String getCreation_source() {
		return creation_source;
	}
	public void setCreation_source(String creation_source) {
		this.creation_source = creation_source;
	}
	public Date getSys_created_on() {
		return sys_created_on;
	}
	public void setSys_created_on(Date sys_created_on) {
		this.sys_created_on = sys_created_on;
	}
	public int getMax_row_id() {
		return max_row_id;
	}
	public void setMax_row_id(int max_row_id) {
		this.max_row_id = max_row_id;
	}
	public ScheduleImport getSchedule_import() {
		return schedule_import;
	}
	public void setSchedule_import(ScheduleImport schedule_import) {
		this.schedule_import = schedule_import;
	}
	public Date getLoad_completed() {
		return load_completed;
	}
	public void setLoad_completed(Date load_completed) {
		this.load_completed = load_completed;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSys_created_by() {
		return sys_created_by;
	}
	public void setSys_created_by(String sys_created_by) {
		this.sys_created_by = sys_created_by;
	}	
}

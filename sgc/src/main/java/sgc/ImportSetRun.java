package sgc;

import java.util.Date;

public class ImportSetRun {
	private int ignored;
	private Set set;
	private Date run_time;
	private int sys_mod_count;
	private Date completed;
	private Date sys_updated_on;
	private ConcurrentImportSet concurrent_import_set;
	private String sys_tags;
	private int updates;
	private boolean multi_import_set;
	private int skipped;
	private String number;
	private int processed;
	private String sys_id;
	private int total;
	private String sys_updated_by;
	private Date sys_created_on;
	private String sys_transform_map;
	private SysRobustTransformer sys_robust_transformer;
	private String state;
	private int inserts;
	private int errors;
	private String sys_created_by;
	
	public int getIgnored() {
		return ignored;
	}
	public void setIgnored(int ignored) {
		this.ignored = ignored;
	}
	public Set getSet() {
		return set;
	}
	public void setSet(Set set) {
		this.set = set;
	}
	public Date getRun_time() {
		return run_time;
	}
	public void setRun_time(Date run_time) {
		this.run_time = run_time;
	}
	public int getSys_mod_count() {
		return sys_mod_count;
	}
	public void setSys_mod_count(int sys_mod_count) {
		this.sys_mod_count = sys_mod_count;
	}
	public Date getCompleted() {
		return completed;
	}
	public void setCompleted(Date completed) {
		this.completed = completed;
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
	public int getUpdates() {
		return updates;
	}
	public void setUpdates(int updates) {
		this.updates = updates;
	}
	public boolean isMulti_import_set() {
		return multi_import_set;
	}
	public void setMulti_import_set(boolean multi_import_set) {
		this.multi_import_set = multi_import_set;
	}
	public int getSkipped() {
		return skipped;
	}
	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getProcessed() {
		return processed;
	}
	public void setProcessed(int processed) {
		this.processed = processed;
	}
	public String getSys_id() {
		return sys_id;
	}
	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
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
	public SysRobustTransformer getSys_robust_transformer() {
		return sys_robust_transformer;
	}
	public void setSys_robust_transformer(SysRobustTransformer sys_robust_transformer) {
		this.sys_robust_transformer = sys_robust_transformer;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getInserts() {
		return inserts;
	}
	public void setInserts(int inserts) {
		this.inserts = inserts;
	}
	public int getErrors() {
		return errors;
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public String getSys_created_by() {
		return sys_created_by;
	}
	public void setSys_created_by(String sys_created_by) {
		this.sys_created_by = sys_created_by;
	}
	public String getSys_transform_map() {
		return sys_transform_map;
	}
	public void setSys_transform_map(String sys_transform_map) {
		this.sys_transform_map = sys_transform_map;
	}
}

package test3;

import java.util.List;

public class TaskName {
	private List<ItemName> itemnames;
	private String TaskName;
	private String TaskCode;
	public List<ItemName> getItemnames() {
		return itemnames;
	}
	public String getTaskName() {
		return TaskName;
	}
	public String getTaskCode() {
		return TaskCode;
	}
	public void setItemnames(List<ItemName> itemnames) {
		this.itemnames = itemnames;
	}
	public void setTaskName(String taskName) {
		TaskName = taskName;
	}
	public void setTaskCode(String taskCode) {
		TaskCode = taskCode;
	}
	
}

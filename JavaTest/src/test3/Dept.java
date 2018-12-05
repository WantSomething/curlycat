package test3;

import java.util.List;

public class Dept {
	private List<TaskName> TaskName;
	private String deptID;
	private String deptName;
	public List<TaskName> getTaskName() {
		return TaskName;
	}
	public String getDeptID() {
		return deptID;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setTaskName(List<TaskName> taskName) {
		TaskName = taskName;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}

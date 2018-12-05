package test3;

import java.util.List;

public class DeptSection {
	private List<Dept> depts;
	private int deptSectionID;
	private String deptSectionName;
	public List<Dept> getDepts() {
		return depts;
	}
	public int getDeptSectionID() {
		return deptSectionID;
	}
	public String getDeptSectionName() {
		return deptSectionName;
	}
	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}
	public void setDeptSectionID(int deptSectionID) {
		this.deptSectionID = deptSectionID;
	}
	public void setDeptSectionName(String deptSectionName) {
		this.deptSectionName = deptSectionName;
	}
	
}

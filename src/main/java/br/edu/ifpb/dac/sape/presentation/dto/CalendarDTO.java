package br.edu.ifpb.dac.sape.presentation.dto;

public class CalendarDTO {
	private Integer schedulingId;
	private String title;
	private String location;
	private String start;
	private String end;
	
	public CalendarDTO(Integer schedulingId, String title, String location, String start, String end) {
		super();
		this.schedulingId = schedulingId;
		this.title = title;
		this.location = location;
		this.start = start;
		this.end = end;
	}
	
	

	public Integer getSchedulingId() {
		return schedulingId;
	}



	public void setSchedulingId(Integer schedulingId) {
		this.schedulingId = schedulingId;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	

}

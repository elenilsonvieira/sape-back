package br.edu.ifpb.dac.sape.presentation.dto;

public class CalendarDTO {
	private String title;
	private String location;
	private String start;
	private String end;
	
	public CalendarDTO(String title, String location, String start, String end) {
		super();
		this.title = title;
		this.location = location;
		this.start = start;
		this.end = end;
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

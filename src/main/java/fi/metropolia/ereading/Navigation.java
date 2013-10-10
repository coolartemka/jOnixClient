package fi.metropolia.ereading;

import javax.faces.bean.*;

@ManagedBean
@SessionScoped
public class Navigation {

	private String page = "login";

	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}	
	
}

package se.sml.jaxrs.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class UserWeb {

//	@XmlElement
//	private final Long id;
	@XmlElement
	private final String username;
	@XmlElement
	private final String firstName;
	@XmlElement
	private final String lastName;
	@XmlElement
	private final String userNumber;
	@XmlElement
	private final String status;

	@SuppressWarnings("unused")
	private UserWeb() {
		this("","","","","");
	}

	public UserWeb(String username, String firstName, String lastName, String userNumber, String status) {
//		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userNumber = userNumber;
		this.status = status;
//		this.workItemsWeb = new ArrayList<>();
	}

//	public Long getId() {
//		return id;
//	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserNumber() {
		return userNumber;
	}

//	public Collection<WorkItemWeb> getWorkItemWeb() {
//		return workItemsWeb;
//	}

	public String getStatus() {
		return status;
	}
}

package se.sml.jaxrs.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
public class TeamWeb {
	
//	@XmlElement
//	private final Long id;
	@XmlElement
	private final String name;
	@XmlElement
	private final String status;
	@XmlElement(name = "userWeb")
	@XmlElementWrapper(name = "usersWeb")
	private final Collection<UserWeb> usersWeb;

	@SuppressWarnings("unused")
	private TeamWeb() {
		this("","");
	}

	public TeamWeb(String name, String status) {
//		this.id = id;
		this.name = name;
		this.status = status;
		this.usersWeb = new ArrayList<>();
	}

//	public Long getId() {
//		return id;
//	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public Collection<UserWeb> getUsersWeb() {
		return usersWeb;
	}

//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public void addUserWeb(UserWeb userWeb) {
		usersWeb.add(userWeb);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	@Override
	public boolean equals(Object otherObj) {
		if (this == otherObj) {
			return true;
		}

		if (otherObj instanceof TeamWeb) {
			TeamWeb otherTeamWeb = (TeamWeb) otherObj;
			return this.name.equals(otherTeamWeb.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result += 37 * name.hashCode();
		return result;
	}
}

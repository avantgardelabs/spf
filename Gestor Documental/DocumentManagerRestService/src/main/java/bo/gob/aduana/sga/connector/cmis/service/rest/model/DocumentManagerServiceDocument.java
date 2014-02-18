package bo.gob.aduana.sga.connector.cmis.service.rest.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.springframework.util.FileCopyUtils;

public class DocumentManagerServiceDocument {
	private String name;
	private String cmisId;
	private String version;
	private List<String> versionsHistory;
	private String md5Hash;
	private byte[] content;
	private List<Property<?>> properties;

	private List<String> format(List<Document> versions) {
		List<String> formatedVersions = new ArrayList<String>();
		for (Document document : versions) {
			formatedVersions.add(document.getVersionLabel());
		}
		return formatedVersions;
	}

	public DocumentManagerServiceDocument(Document document, String hash) throws IOException {
		super();
		this.properties = document.getProperties();
		this.md5Hash = hash;
		this.cmisId = document.getId();
		this.content = FileCopyUtils.copyToByteArray(document.getContentStream().getStream());
		this.name = document.getName();
		this.version = document.getVersionLabel();
		this.versionsHistory = format(document.getAllVersions());
	}

	public DocumentManagerServiceDocument(String cmisId, String md5Hash, byte[] content, List<Property<?>> properties, String name, String version, List<String> versionsHistory) {
		super();
		this.cmisId = cmisId;
		this.md5Hash = md5Hash;
		this.content = content;
		this.properties = properties;
		this.name = name;
		this.version = version;
		this.versionsHistory = versionsHistory;
	}

	public String getCmisId() {
		return cmisId;
	}

	public void setCmisId(String cmisId) {
		this.cmisId = cmisId;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public List<Property<?>> getProperties() {
		return properties;
	}

	public void setProperties(List<Property<?>> properties) {
		this.properties = properties;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getVersionsHistory() {
		return versionsHistory;
	}

	public void setVersionsHistory(List<String> versionsHistory) {
		this.versionsHistory = versionsHistory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

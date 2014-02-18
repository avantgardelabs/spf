package bo.gob.aduana.sga.connector.cmis.model;

import org.apache.chemistry.opencmis.client.api.Document;

public class CmisConnectorDocument {
	private String hashCode;
	private Document document;

	public CmisConnectorDocument(String docHashCode, Document document) {
		super();
		this.hashCode = docHashCode;
		this.document = document;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}

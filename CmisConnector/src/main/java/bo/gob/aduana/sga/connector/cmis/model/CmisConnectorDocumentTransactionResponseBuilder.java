package bo.gob.aduana.sga.connector.cmis.model;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class CmisConnectorDocumentTransactionResponseBuilder {
	private static final Logger LOGGER = Logger.getLogger(CmisConnectorDocumentTransactionResponseBuilder.class);
	private Document document;
	private String documentContentMD5HashCode;
	private Exception exception;
	private CmisConnectorOperationResult result;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
		this.result = CmisConnectorOperationResult.SUCCESS;
		try {
			documentContentMD5HashCode = DigestUtils.md5Hex(IOUtils.toByteArray(document.getContentStream().getStream()));
		} catch (IOException e) {
			LOGGER.warn("Could not hash document {" + document + "} returning an empty hash string instead");
		}
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
		this.result = CmisConnectorOperationResult.ERROR;
	}

	public CmisConnectorDocumentTransactionResponse build() {
		return new CmisConnectorDocumentTransactionResponse(new CmisConnectorObject(documentContentMD5HashCode, document), exception, result);
	}
}

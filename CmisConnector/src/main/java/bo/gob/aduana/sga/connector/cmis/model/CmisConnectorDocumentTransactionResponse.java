package bo.gob.aduana.sga.connector.cmis.model;

public class CmisConnectorDocumentTransactionResponse {
	private CmisConnectorDocument cmisConnectorDocument;
	private Exception exception;
	private CmisConnectorOperationResult result;

	public CmisConnectorDocumentTransactionResponse(CmisConnectorDocument document, Exception exception, CmisConnectorOperationResult result) {
		super();
		this.cmisConnectorDocument = document;
		this.exception = exception;
		this.result = result;
	}

	public CmisConnectorDocument getCmisConnectorDocument() {
		return cmisConnectorDocument;
	}

	public void setCmisConnectorDocument(CmisConnectorDocument cmisConnectorDocument) {
		this.cmisConnectorDocument = cmisConnectorDocument;
	}

	public CmisConnectorOperationResult getResult() {
		return result;
	}

	public void setResult(CmisConnectorOperationResult result) {
		this.result = result;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}

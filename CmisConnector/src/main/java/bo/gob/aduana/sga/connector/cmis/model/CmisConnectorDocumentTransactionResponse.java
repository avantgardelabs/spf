package bo.gob.aduana.sga.connector.cmis.model;

public class CmisConnectorDocumentTransactionResponse {
	private CmisConnectorObject cmisConnectorObject;
	private Exception exception;
	private CmisConnectorOperationResult result;

	public CmisConnectorDocumentTransactionResponse(CmisConnectorObject document, Exception exception, CmisConnectorOperationResult result) {
		super();
		this.cmisConnectorObject = document;
		this.exception = exception;
		this.result = result;
	}

	public CmisConnectorObject getCmisConnectorObject() {
		return cmisConnectorObject;
	}

	public void setCmisConnectorObject(CmisConnectorObject cmisConnectorObject) {
		this.cmisConnectorObject = cmisConnectorObject;
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

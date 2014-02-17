package bo.gob.aduana.sga.connector.cmis.service.model;

import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponse;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorOperationResult;

public class DocumentManagerServiceResponse {
	private boolean success;
	private Object data;

	public DocumentManagerServiceResponse(boolean result, Object data) {
		super();
		this.success = result;
		this.data = data;
	}

	public DocumentManagerServiceResponse(CmisConnectorDocumentTransactionResponse cmisConnectorResponse) {
		this.data = cmisConnectorResponse.getCmisConnectorDocument();
		if (cmisConnectorResponse.getResult() == CmisConnectorOperationResult.ERROR) {
			data = cmisConnectorResponse.getException().getMessage();
		}
		this.success = CmisConnectorOperationResult.SUCCESS == cmisConnectorResponse.getResult();
	}

	public DocumentManagerServiceResponse(CmisConnectorOperationResult cmisConnectorOperationResult) {
		this.success = CmisConnectorOperationResult.SUCCESS == cmisConnectorOperationResult;
		this.data = "";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

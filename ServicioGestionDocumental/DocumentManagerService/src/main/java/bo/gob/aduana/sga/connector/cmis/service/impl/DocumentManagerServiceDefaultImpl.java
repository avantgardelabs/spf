package bo.gob.aduana.sga.connector.cmis.service.impl;

import java.util.Map;

import bo.gob.aduana.sga.connector.cmis.CmisConnector;
import bo.gob.aduana.sga.connector.cmis.CmisConnectorImpl;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponse;
import bo.gob.aduana.sga.connector.cmis.service.DocumentManagerService;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceParamsHelper;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceResponse;

/**
 * Document Manager Service default implementation
 * 
 * @author eric
 * 
 */
public class DocumentManagerServiceDefaultImpl implements DocumentManagerService {
	private CmisConnector cmisConnector;
	private String cmisIdPrefix;

	public DocumentManagerServiceDefaultImpl(String user, String password, String repoId, String atomPubUrl) {
		cmisConnector = new CmisConnectorImpl(user, password, repoId, atomPubUrl);
	}

	public DocumentManagerServiceResponse createDocument(Map<String, Object> params) {
		if (DocumentManagerServiceParamsHelper.hasProperties(DocumentManagerServiceParamsHelper.getProperties(params))) {
			return new DocumentManagerServiceResponse(cmisConnector.createDocument(DocumentManagerServiceParamsHelper.getName(params), DocumentManagerServiceParamsHelper.getContent(params), DocumentManagerServiceParamsHelper.getType(params), cmisIdPrefix + DocumentManagerServiceParamsHelper.getFolderId(params), DocumentManagerServiceParamsHelper.getProperties(params)));
		}
		return new DocumentManagerServiceResponse(cmisConnector.createDocument(DocumentManagerServiceParamsHelper.getName(params), DocumentManagerServiceParamsHelper.getContent(params), DocumentManagerServiceParamsHelper.getType(params), cmisIdPrefix + DocumentManagerServiceParamsHelper.getFolderId(params)));
	}

	public DocumentManagerServiceResponse updateDocument(Map<String, Object> params) {
		CmisConnectorDocumentTransactionResponse response;
		response = cmisConnector.checkOutDocument(cmisIdPrefix + DocumentManagerServiceParamsHelper.getDocId(params));
		if (!DocumentManagerServiceParamsHelper.isError(response.getResult())) {
			if (DocumentManagerServiceParamsHelper.hasProperties(DocumentManagerServiceParamsHelper.getProperties(params))) {
				response = cmisConnector.checkIn(response.getCmisConnectorDocument().getDocument().getId(), true, DocumentManagerServiceParamsHelper.getName(params), DocumentManagerServiceParamsHelper.getContent(params), DocumentManagerServiceParamsHelper.getType(params), DocumentManagerServiceParamsHelper.getCommitComment(params), DocumentManagerServiceParamsHelper.getProperties(params));
			} else {
				response = cmisConnector.checkIn(response.getCmisConnectorDocument().getDocument().getId(), true, DocumentManagerServiceParamsHelper.getName(params), DocumentManagerServiceParamsHelper.getContent(params), DocumentManagerServiceParamsHelper.getType(params), DocumentManagerServiceParamsHelper.getCommitComment(params));
			}
		}
		return new DocumentManagerServiceResponse(response);
	}

	public DocumentManagerServiceResponse deleteDocument(Map<String, Object> params) {
		return new DocumentManagerServiceResponse(cmisConnector.deleteCmisObject(cmisIdPrefix + DocumentManagerServiceParamsHelper.getDocId(params), DocumentManagerServiceParamsHelper.getAllVersions(params)));
	}

	public DocumentManagerServiceResponse getDocument(Map<String, Object> params) {
		return new DocumentManagerServiceResponse(cmisConnector.getDocumentById(cmisIdPrefix + DocumentManagerServiceParamsHelper.getDocId(params)));
	}

	public DocumentManagerServiceResponse getDocumentVersion(Map<String, Object> params) {
		return new DocumentManagerServiceResponse(cmisConnector.getDocumentVersion(cmisIdPrefix + DocumentManagerServiceParamsHelper.getDocId(params), DocumentManagerServiceParamsHelper.getDocVersion(params)));
	}

	// GETTERS & SETTERS

	public CmisConnector getCmisConnector() {
		return cmisConnector;
	}

	public String getCmisIdPrefix() {
		return cmisIdPrefix;
	}

	public void setCmisIdPrefix(String cmisIdPrefix) {
		this.cmisIdPrefix = cmisIdPrefix;
	}

	public void setCmisConnector(CmisConnector cmisConnector) {
		this.cmisConnector = cmisConnector;
	}

}

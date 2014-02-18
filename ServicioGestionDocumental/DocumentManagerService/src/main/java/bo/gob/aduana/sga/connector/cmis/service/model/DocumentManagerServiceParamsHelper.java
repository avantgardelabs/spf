package bo.gob.aduana.sga.connector.cmis.service.model;

import java.io.InputStream;
import java.util.Map;

import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorOperationResult;

public class DocumentManagerServiceParamsHelper {
	public static String DOCUMENT_ID = "documentId";
	public static String DOCUMENT_NAME = "documentName";
	public static String DOCUMENT_CONTENT = "documentContent";
	public static String DOCUMENT_TYPE = "documentType";
	public static String FOLDER_ID = "folderId";
	public static String DOCUMENT_VERSION = "documentVersion";
	public static String COMMIT_COMMENT = "commitComment";
	public static String ALL_VERSIONS = "allVersions";
	public static String DOCUMENT_PROPERTIES = "documentProperties";
	public static String MAJOR_VERSION = "major";

	public static boolean isError(CmisConnectorOperationResult cmisConnectorOperationResult) {
		return cmisConnectorOperationResult == CmisConnectorOperationResult.ERROR;
	}

	public static boolean hasProperties(Map<String, ?> properties) {
		return !((properties == null) || (properties.isEmpty()));
	}

	public static Map<String, ?> getProperties(Map<String, Object> params) {
		return (Map<String, ?>) params.get(DocumentManagerServiceParamsHelper.DOCUMENT_PROPERTIES);
	}

	public static String getFolderId(Map<String, Object> params) {
		return (String) params.get(DocumentManagerServiceParamsHelper.FOLDER_ID);
	}

	public static String getType(Map<String, Object> params) {
		return (String) params.get(DocumentManagerServiceParamsHelper.DOCUMENT_TYPE);
	}

	public static InputStream getContent(Map<String, Object> params) {
		return (InputStream) params.get(DocumentManagerServiceParamsHelper.DOCUMENT_CONTENT);
	}

	public static String getName(Map<String, Object> params) {
		return (String) params.get(DocumentManagerServiceParamsHelper.DOCUMENT_NAME);
	}

	public static boolean getMajor(Map<String, Object> params) {
		return (Boolean) params.get(DocumentManagerServiceParamsHelper.MAJOR_VERSION);
	}

	public static boolean getAllVersions(Map<String, Object> params) {
		return (Boolean) params.get(ALL_VERSIONS);
	}

	public static String getDocId(Map<String, Object> params) {
		return (String) params.get(DOCUMENT_ID);
	}

	public static String getDocVersion(Map<String, Object> params) {
		return (String) params.get(DOCUMENT_VERSION);
	}

	public static String getCommitComment(Map<String, Object> params) {
		return (String) params.get(COMMIT_COMMENT);
	}
}

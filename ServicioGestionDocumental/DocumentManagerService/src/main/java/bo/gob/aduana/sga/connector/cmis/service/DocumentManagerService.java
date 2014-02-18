package bo.gob.aduana.sga.connector.cmis.service;

import java.util.Map;

import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceResponse;

/**
 * Document Manager Service interface
 * 
 * @author eric
 * 
 */
public interface DocumentManagerService {

	/**
	 * creates a document on the Cmis system
	 * 
	 * Parameters: DOCUMENT_NAME (String), DOCUMENT_CONTENT (InputStream),
	 * DOCUMENT_TYPE (String/mimeType), FOLDER_ID (String), DOCUMENT_PROPERTIES
	 * (optional -> if not passed it will used default properties)
	 * 
	 * Properties type is Map<String, ?> refer to PropertyIds class to see Map
	 * keys
	 * 
	 * @param params
	 * @return
	 */
	public DocumentManagerServiceResponse createDocument(Map<String, Object> params);

	/**
	 * updates an existing document on the Cmis system and makes a new version
	 * 
	 * Parameters: DOCUMENT_ID (String), DOCUMENT_NAME (String),
	 * DOCUMENT_CONTENT (InputStream), DOCUMENT_TYPE (String/mimeType),
	 * COMMIT_COMMENT (String), DOCUMENT_PROPERTIES (optional -> if not passed
	 * it will used default properties)
	 * 
	 * Properties type is Map<String, ?> refer to PropertyIds class to see Map
	 * keys
	 * 
	 * @param params
	 * @return
	 */
	public DocumentManagerServiceResponse updateDocument(Map<String, Object> params);

	/**
	 * deletes a document on the Cmis system, if ALL_VERSIONS is set to true, it
	 * will delete all versions
	 * 
	 * Parameters: DOCUMENT_ID, ALL_VERSIONS
	 * 
	 * @param params
	 * @return
	 */
	public DocumentManagerServiceResponse deleteDocument(Map<String, Object> params);

	/**
	 * get a document of the Cmis system
	 * 
	 * Parameters: DOCUMENT_ID (String)
	 * 
	 * @param params
	 * @return
	 */
	public DocumentManagerServiceResponse getDocument(Map<String, Object> params);

	/**
	 * get a document version of the Cmis system
	 * 
	 * Parameters: DOCUMENT_ID (String), DOCUMENT_VERSION (String)
	 * 
	 * @param params
	 * @return
	 */
	public DocumentManagerServiceResponse getDocumentVersion(Map<String, Object> params);

	/**
	 * Used to inject via spring the ID prefix of the Cmis system, like
	 * "workspace://SpacesStore/"
	 * 
	 */
	public void setCmisIdPrefix(String cmisIdPrefix);
}

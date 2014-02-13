package bo.gob.aduana.sga.connector.cmis;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponse;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorOperationResult;

/**
 * Cmis connector interface
 * 
 * @author eric
 * 
 */
public interface CmisConnector {
	/**
	 * Get the current cmis session
	 * 
	 * @return
	 */
	public Session getSession();

	/**
	 * Gets the repository information
	 * 
	 * @return RepositoryInfo
	 */
	public RepositoryInfo getRepoInfo();

	/**
	 * Gets the repository capabilities to see which operations supports this
	 * CMIS version
	 * 
	 * @return RepositoryCapabilities
	 */
	public RepositoryCapabilities getRepoCapabilities();

	/**
	 * Get a Document by ID
	 * 
	 * @return Document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse getDocumentById(String id);

	/**
	 * Get a CmisObject by ID
	 * 
	 * @return CmisObject
	 */
	public CmisObject getCmisObjectById(String id);

	/**
	 * CheckOut the given Document ID to update it and create a new version.
	 * Don't forget to call checkIn method inside Document class after the
	 * document modification.
	 * 
	 * @return document private working copy (pwc) inside
	 *         CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse checkOutDocument(String id);

	/**
	 * Gets the content of the given Document as String
	 * 
	 * @Throws IOException if the convertion fails
	 * @return content as String
	 */
	public String getDocumentContentAsString(Document doc) throws IOException;

	/**
	 * Creates a new document using (fileName, fileContent and mimeType) inside
	 * the given folderId. mimeType example: "text/plain; charset=UTF-8" This
	 * method will use default properties> type and name
	 * 
	 * @return the created document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, String fileContent, String mimeType, String folderId);

	/**
	 * Creates a new document using (fileName, fileContent and mimeType) inside
	 * the given folderId. mimeType example: "text/plain; charset=UTF-8" This
	 * method will use default properties> type and name
	 * 
	 * @return the created document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, InputStream fileContent, String mimeType, String folderId);

	/**
	 * Get the properties list of the given cmisObject Id. To retrieve the
	 * property you wish, you may use constant inside PropertyIds class
	 * 
	 * @return list of properties
	 */
	public List<Property<?>> getCmisObjectProperties(String id);

	/**
	 * Delete the cmisObject associated with the given Id, if allVersions is
	 * true and the given cmisObject is a document then it will delete all
	 * document versions. If the given Id is associated to a folder, it has to
	 * be empty or a runtime exception will be thrown
	 * 
	 * TODO make a difference -> cmis doesn't delete folders with content
	 * 
	 */
	public CmisConnectorOperationResult deleteCmisObject(String id, boolean allVersions);

	/**
	 * Get the given document Id versions history TODO remove this method?
	 * 
	 * @return list of Document
	 */
	public List<Document> getDocumentVersionsHistory(String id);

	/**
	 * Get the documentId associated with versionLabel version
	 * 
	 * @return Document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse getDocumentVersion(String id, String versionLabel);

	/**
	 * Make a checkIn of a Private Working Copy (the one obtained after
	 * checkOut) using given properties
	 * 
	 * @return Document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse checkIn(String pwcId, boolean major, String fileName, InputStream fileContent, String mimeType, String checkinComment, Map<String, ?> properties);

	/**
	 * Make a checkIn of a Private Working Copy (the one obtained after
	 * checkOut) using default properties
	 * 
	 * @return Document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse checkIn(String pwcId, boolean major, String fileName, InputStream fileContent, String mimeType, String checkinComment);

	/**
	 * Creates a new document using (fileName, fileContent, mimeType and
	 * properties) inside the given folderId. mimeType example:
	 * "text/plain; charset=UTF-8"
	 * 
	 * @return the created document inside CmisConnectorFileTransactionResponse
	 */
	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, InputStream fileContent, String mimeType, String folderId, Map<String, ?> properties);

}

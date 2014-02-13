package bo.gob.aduana.sga.connector.cmis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.RepositoryCapabilities;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisVersioningException;
import org.apache.log4j.Logger;

import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponse;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponseBuilder;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorOperationResult;
import bo.gob.aduana.sga.connector.cmis.utils.Utils;

/**
 * Cmis connector implementation
 * 
 * TODO 1 - refactor createDocuments methods 2- refactor checkIn methods 3- put
 * session in another class
 * 
 * @author eric
 * 
 */
public class CmisConnectorImpl implements CmisConnector {
	private static final Logger LOGGER = Logger.getLogger(CmisConnectorImpl.class);

	private Session session;

	public CmisConnectorImpl(String user, String password, String repoId, String atomPubUrl) {
		LOGGER.debug("Creating session with the next info: [ user=" + user + ", password=" + password + ", repoId=" + repoId + ", atomPubUrl=" + atomPubUrl + " ]");
		session = SessionFactoryImpl.newInstance().createSession(Utils.getCreateSessionParameters(user, password, atomPubUrl, repoId));
	}

	public RepositoryInfo getRepoInfo() {
		RepositoryInfo repositoryInfo = session.getRepositoryInfo();
		LOGGER.debug("Get repository info called, returning repo info: " + repositoryInfo);
		return repositoryInfo;
	}

	public RepositoryCapabilities getRepoCapabilities() {
		RepositoryCapabilities capabilities = session.getRepositoryInfo().getCapabilities();
		LOGGER.debug("Get repository capabilities called, returning repo capabilities: " + capabilities);
		return capabilities;
	}

	public CmisConnectorDocumentTransactionResponse getDocumentById(String id) {
		LOGGER.debug("Getting object with id=" + id);
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			Document document = getDocumentInternally(id);
			LOGGER.debug("Object found! returning document: " + document);
			response.setDocument(document);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public String getDocumentContentAsString(Document doc) throws IOException {
		LOGGER.debug("getDocumentContentAsString called, params received: [doc=" + doc + "]");
		String contentAsString = Utils.getContentAsString(doc.getContentStream());
		LOGGER.debug("returning content:\n" + contentAsString);
		return contentAsString;
	}

	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, String fileContent, String mimeType, String folderId) {
		LOGGER.debug("Creating document with the next info: [ fileName=" + fileName + ", mimeType=" + mimeType + ", folderId=" + folderId + ",\n fileContent=" + fileContent + "\n ]");
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			byte[] buf = fileContent.getBytes("UTF-8");
			ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, buf.length, mimeType, new ByteArrayInputStream(buf));
			Folder folder = (Folder) session.getObject(folderId);
			Document newDoc = folder.createDocument(Utils.getDefaultDocumentProperties(fileName), contentStream, VersioningState.MAJOR);
			LOGGER.debug("Document created in " + folder.getPath() + " returning doc " + newDoc);
			response.setDocument(newDoc);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, InputStream fileContent, String mimeType, String folderId) {
		LOGGER.debug("Creating document with the next info: [ fileName=" + fileName + ", mimeType=" + mimeType + ", folderId=" + folderId + ", fileContent=" + fileContent + " ]");
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, -1, mimeType, fileContent);
			Map<String, Object> properties = Utils.getDefaultDocumentProperties(fileName);
			Folder folder = (Folder) session.getObject(folderId);
			Document newDoc = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
			LOGGER.debug("Document created in " + folder.getPath() + " returning doc id " + newDoc.getId());
			response.setDocument(newDoc);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public List<Property<?>> getCmisObjectProperties(String id) {
		LOGGER.debug("Getting cmis object with id=" + id);
		CmisObject cmisObject = session.getObject(id);
		LOGGER.debug("Cmis object found! name=" + cmisObject.getName() + ", returning its properties=" + cmisObject.getProperties());
		return cmisObject.getProperties();
	}

	public CmisConnectorOperationResult deleteCmisObject(String id, boolean allVersions) {
		LOGGER.debug("Deleting object with id=" + id + " - allVersions?	" + allVersions);
		try {
			session.getObject(id).delete(allVersions);
		} catch (Exception e) {
			LOGGER.error("An exception was caught! message=" + e.getMessage());
			return CmisConnectorOperationResult.ERROR;
		}
		LOGGER.debug("Object deleted with success!");
		return CmisConnectorOperationResult.SUCCESS;
	}

	public List<Document> getDocumentVersionsHistory(String id) {
		LOGGER.debug("Getting document versions history for " + id);
		Document doc = getDocumentInternally(id);
		if (!Utils.isVersionable(doc)) {
			LOGGER.error("Document is not versionable, throwing exception...");
			throw new CmisVersioningException("Document " + doc.getName() + " is not versionable");
		}
		LOGGER.debug("Returning versions history=" + doc.getAllVersions());
		return doc.getAllVersions();
	}

	public CmisConnectorDocumentTransactionResponse getDocumentVersion(String id, String versionLabel) {
		LOGGER.debug("Getting document " + id + " with version " + versionLabel);
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			Document document = Utils.findVersion(versionLabel, getDocumentVersionsHistory(id));
			LOGGER.debug("Version found! returning document " + document);
			response.setDocument(document);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public CmisConnectorDocumentTransactionResponse checkOutDocument(String id) {
		LOGGER.debug("checking out document for a private working copy, doc id=" + id);
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			Document doc = getDocumentInternally(id);
			ObjectId pwc = doc.checkOut();
			LOGGER.debug("private working copy is " + pwc);
			response.setDocument(getDocumentInternally(pwc.getId()));
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	/**
	 * The objective of this method is to retrieve internally a document and
	 * reuse the code without repetitions
	 * 
	 * @param id
	 * @return
	 */
	private Document getDocumentInternally(String id) {
		CmisObject obj = session.getObject(id);
		if (!Utils.isDocument(obj)) {
			LOGGER.error("CmisObject is not a document, throwing exception...");
			throw new CmisInvalidArgumentException("CmisObject " + obj + " is not a Document, object type is " + obj.getType().getId());
		}
		return (Document) obj;
	}

	public CmisConnectorDocumentTransactionResponse checkIn(String pwcId, boolean major, String fileName, InputStream fileContent, String mimeType, String checkinComment, Map<String, ?> properties) {
		LOGGER.debug("CheckIn document with the next info: [ fileName=" + fileName + ", mimeType=" + mimeType + ", major=" + major + ", fileContent=" + fileContent + ", checkinComment=" + checkinComment + ", properties=" + properties + " ]");
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			Document doc = getDocumentInternally(pwcId);
			ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, -1, mimeType, fileContent);
			ObjectId objId = doc.checkIn(major, properties, contentStream, checkinComment);
			Document newDoc = getDocumentInternally(objId.getId());
			LOGGER.debug("checkin done with success! returning the new document version " + newDoc);
			response.setDocument(newDoc);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public CmisConnectorDocumentTransactionResponse checkIn(String pwcId, boolean major, String fileName, InputStream fileContent, String mimeType, String checkinComment) {
		LOGGER.debug("CheckIn document with the next info: [ fileName=" + fileName + ", mimeType=" + mimeType + ", major=" + major + ", fileContent=" + fileContent + ", checkinComment=" + checkinComment + " ]");
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			Document doc = getDocumentInternally(pwcId);
			ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, -1, mimeType, fileContent);
			ObjectId objId = doc.checkIn(major, Utils.getDefaultDocumentProperties(fileName), contentStream, checkinComment);
			Document newDoc = getDocumentInternally(objId.getId());
			LOGGER.debug("checkin done with success! returning the new document version " + newDoc);
			response.setDocument(newDoc);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public CmisConnectorDocumentTransactionResponse createDocument(String fileName, InputStream fileContent, String mimeType, String folderId, Map<String, ?> properties) {
		LOGGER.debug("Creating document with the next info: [ fileName=" + fileName + ", mimeType=" + mimeType + ", folderId=" + folderId + ", fileContent=" + fileContent + ", properties=" + properties + " ]");
		CmisConnectorDocumentTransactionResponseBuilder response = new CmisConnectorDocumentTransactionResponseBuilder();
		try {
			ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, -1, mimeType, fileContent);
			Folder folder = (Folder) session.getObject(folderId);
			Document newDoc = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
			LOGGER.debug("Document created in " + folder.getPath() + " returning doc id " + newDoc.getId());
			response.setDocument(newDoc);
		} catch (Exception e) {
			handleException(response, e);
		}
		return response.build();
	}

	public CmisObject getCmisObjectById(String id) {
		LOGGER.debug("Getting cmis object with id = " + id);
		return session.getObject(id);
	}

	private void handleException(CmisConnectorDocumentTransactionResponseBuilder response, Exception e) {
		LOGGER.error("An exception was caught! message=" + e.getMessage());
		response.setException(e);
	}

	public Session getSession() {
		return session;
	}
}

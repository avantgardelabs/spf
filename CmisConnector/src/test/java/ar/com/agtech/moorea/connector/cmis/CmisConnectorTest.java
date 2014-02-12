package ar.com.agtech.moorea.connector.cmis;

import java.util.List;

import junit.framework.TestCase;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bo.gob.aduana.sga.connector.cmis.CmisConnector;
import bo.gob.aduana.sga.connector.cmis.CmisConnectorImpl;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocumentTransactionResponse;
import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorOperationResult;

public class CmisConnectorTest extends TestCase {
	private static final String FOLDER_SITIOS_ID = "workspace://SpacesStore/4f66b275-8e6e-4040-b4bd-fd206bad2e62";
	private static final String MAIN_PAGE_FILE_ID = "workspace://SpacesStore/d6f3a279-ce86-4a12-8985-93b71afbb71d";
	private static final Logger LOGGER = Logger.getLogger(CmisConnectorTest.class);
	private static final String REPO_ID = "25068bb8-dd84-41c3-aa2f-64c9f3a0e53f";
	private static final String ATOMPUB_URL = "http://localhost:8081/alfresco/cmisatom";
	private static final String USER = "admin";
	private static final String PASSWORD = "admin";

	CmisConnector connector = new CmisConnectorImpl(USER, PASSWORD, REPO_ID, ATOMPUB_URL);

	@Before
	public void prepare() {

	}

	@Test
	public void testCreateDocument() {
		String textFileName = "test.txt";
		// folder Sitios
		String mimetype = "text/plain; charset=UTF-8";
		String content = "This is some test content.";
		CmisConnectorDocumentTransactionResponse response = connector.createDocument(textFileName, content, mimetype, FOLDER_SITIOS_ID);
		validateSuccessResponse(response);
		assertTrue(DigestUtils.md5Hex(content).equalsIgnoreCase(response.getCmisConnectorDocument().getHashCode()));
		testDelete(response.getCmisConnectorDocument().getDocument().getId(), true);
		validateDocumentDeletion(response.getCmisConnectorDocument().getDocument().getId());
	}

	@Test
	public void testGetDocumentVersion() {
		// Documento Main_Page
		CmisConnectorDocumentTransactionResponse response = connector.getDocumentVersion(MAIN_PAGE_FILE_ID, "1.15");
		validateSuccessResponse(response);
	}

	@Test
	public void testUpdateDocument() {
		Document oldDoc = getDocumentTest(MAIN_PAGE_FILE_ID);
		CmisConnectorDocumentTransactionResponse response = connector.checkOutDocument(MAIN_PAGE_FILE_ID);
		Document pwc = response.getCmisConnectorDocument().getDocument();
		CmisConnectorDocumentTransactionResponse response2 = connector.checkIn(pwc.getId(), true, pwc.getName(), pwc.getContentStream().getStream(), pwc.getContentStreamMimeType(), "test comment");
		Document newDoc = response2.getCmisConnectorDocument().getDocument();
		LOGGER.debug("oldDoc version=" + oldDoc.getVersionLabel());
		LOGGER.debug("newDoc version (after checkin)=" + newDoc.getVersionLabel());
		assertTrue(newDoc.getVersionLabel() != pwc.getVersionLabel());
		testDelete(newDoc.getId(), false);
		validateDocumentDeletion(newDoc.getId());
	}

	@Test
	public void testGetDocumentProperties() {
		List<Property<?>> list = connector.getCmisObjectProperties(MAIN_PAGE_FILE_ID);
		assertNotNull(list);
		for (Property<?> property : list) {
			LOGGER.debug("Property Id = " + property.getId());
			LOGGER.debug("Property values = \n" + property.getValuesAsString());
		}
	}

	@After
	public void destroy() {

	}

	// reuse methods TODO refactor this
	private Document getDocumentTest(String id) {
		CmisConnectorDocumentTransactionResponse response = connector.getDocumentById(id);
		validateSuccessResponse(response);
		return response.getCmisConnectorDocument().getDocument();
	}

	private void validateSuccessResponse(CmisConnectorDocumentTransactionResponse response) {
		assertTrue(response.getResult() == CmisConnectorOperationResult.SUCCESS);
		assertNotNull(response.getCmisConnectorDocument());
	}

	private void validateDocumentDeletion(String id) {
		CmisConnectorDocumentTransactionResponse response = connector.getDocumentById(id);
		assertTrue(response.getResult() == CmisConnectorOperationResult.ERROR);
		assertNull(response.getCmisConnectorDocument().getDocument());
	}

	private void testDelete(String id, boolean allVersions) {
		assertTrue(CmisConnectorOperationResult.SUCCESS == connector.deleteCmisObject(id, allVersions));
	}

}

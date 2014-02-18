package bo.gob.aduana.sga.connector.cmis.service.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import bo.gob.aduana.sga.connector.cmis.service.DocumentManagerService;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceParamsHelper;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceResponse;

import com.sun.xml.messaging.saaj.util.ByteInputStream;

public class DocumentManagerServiceImplTest extends TestCase {

	private static final String FOLDER_SITIOS_ID = "4f66b275-8e6e-4040-b4bd-fd206bad2e62";
	private static final String MAIN_PAGE_FILE_ID = "workspace://SpacesStore/d6f3a279-ce86-4a12-8985-93b71afbb71d";
	private static final String REPO_ID = "25068bb8-dd84-41c3-aa2f-64c9f3a0e53f";
	private static final String ATOMPUB_URL = "http://localhost:8081/alfresco/cmisatom";
	private static final String USER = "admin";
	private static final String PASSWORD = "admin";

	private DocumentManagerService documentManager = new DocumentManagerServiceImpl(USER, PASSWORD, REPO_ID, ATOMPUB_URL);

	@Test
	public void testCreateDocument() {
//		String textFileName = "test.txt";
//		String mimetype = "text/plain";
//		String content = "This is some test content.";
//		documentManager.setCmisIdPrefix("");
//		Map params = new HashMap();
//		params.put(DocumentManagerServiceParamsHelper.DOCUMENT_CONTENT, new ByteInputStream(content.getBytes(), content.getBytes().length));
//		params.put(DocumentManagerServiceParamsHelper.DOCUMENT_NAME, textFileName);
//		params.put(DocumentManagerServiceParamsHelper.DOCUMENT_TYPE, mimetype);
//		params.put(DocumentManagerServiceParamsHelper.FOLDER_ID, FOLDER_SITIOS_ID);
//		DocumentManagerServiceResponse response = documentManager.createDocument(params);
//		assertTrue(response.isSuccess());
	}
}

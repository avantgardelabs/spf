package bo.gob.aduana.sga.connector.cmis.service.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import bo.gob.aduana.sga.connector.cmis.model.CmisConnectorDocument;
import bo.gob.aduana.sga.connector.cmis.service.DocumentManagerService;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceParamsHelper;
import bo.gob.aduana.sga.connector.cmis.service.model.DocumentManagerServiceResponse;
import bo.gob.aduana.sga.connector.cmis.service.rest.model.DocumentManagerServiceDocument;

/**
 * Document Manager Rest Service controller. Exposes the Document Manager
 * Service TODO dejar content solo en get, validar null file
 * 
 * @author eric
 * 
 */
@Controller
@RequestMapping(value = "/rest")
public class DocumentManagerRestServiceController {
	@Autowired
	DocumentManagerService documentManagerService;

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public DocumentManagerServiceResponse delete(@QueryParam("cmisId") String cmisId, @QueryParam("allVersions") String allVersions) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_ID, cmisId);
			params.put(DocumentManagerServiceParamsHelper.ALL_VERSIONS, Boolean.valueOf(allVersions));
			return documentManagerService.deleteDocument(params);
		} catch (Exception e) {
			return new DocumentManagerServiceResponse(false, e.getMessage());
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DocumentManagerServiceResponse update(MultipartHttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			MultipartFile document = request.getFile("document");
			String cmisId = request.getParameter("cmisId");
			String commitComment = request.getParameter("commitComment");
			boolean major = Boolean.valueOf(request.getParameter("major"));

			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_CONTENT, document.getInputStream());
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_TYPE, document.getContentType());
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_ID, cmisId);
			params.put(DocumentManagerServiceParamsHelper.COMMIT_COMMENT, commitComment);
			params.put(DocumentManagerServiceParamsHelper.MAJOR_VERSION, major);

			DocumentManagerServiceResponse documentManagerServiceResponse = documentManagerService.updateDocument(params);

			if (documentManagerServiceResponse.isSuccess()) {
				return new DocumentManagerServiceResponse(documentManagerServiceResponse.isSuccess(), new DocumentManagerServiceDocument(((CmisConnectorDocument) documentManagerServiceResponse.getData()).getDocument(), ((CmisConnectorDocument) documentManagerServiceResponse.getData()).getHashCode()));
			}
			return documentManagerServiceResponse;
		} catch (Exception e) {
			return new DocumentManagerServiceResponse(false, e.getMessage());
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public DocumentManagerServiceResponse upload(MultipartHttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			MultipartFile file = request.getFile("document");
			String folderId = request.getParameter("folderId");
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_CONTENT, file.getInputStream());
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_NAME, file.getOriginalFilename());
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_TYPE, file.getContentType());
			params.put(DocumentManagerServiceParamsHelper.FOLDER_ID, folderId);
			DocumentManagerServiceResponse documentManagerServiceResponse = documentManagerService.createDocument(params);
			if (documentManagerServiceResponse.isSuccess()) {
				return new DocumentManagerServiceResponse(documentManagerServiceResponse.isSuccess(), new DocumentManagerServiceDocument(((CmisConnectorDocument) documentManagerServiceResponse.getData()).getDocument(), ((CmisConnectorDocument) documentManagerServiceResponse.getData()).getHashCode()));
			}
			return documentManagerServiceResponse;
		} catch (Exception e) {
			return new DocumentManagerServiceResponse(false, e.getMessage());
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public DocumentManagerServiceResponse get(@QueryParam("cmisId") String cmisId, @DefaultValue("") @QueryParam("version") String version) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			DocumentManagerServiceResponse documentManagerServiceResponse;
			params.put(DocumentManagerServiceParamsHelper.DOCUMENT_ID, cmisId);
			if ((version == null) || ("".equalsIgnoreCase(version))) {
				documentManagerServiceResponse = documentManagerService.getDocument(params);
			} else {
				params.put(DocumentManagerServiceParamsHelper.DOCUMENT_VERSION, version);
				documentManagerServiceResponse = documentManagerService.getDocumentVersion(params);
			}
			if (documentManagerServiceResponse.isSuccess()) {
				return new DocumentManagerServiceResponse(documentManagerServiceResponse.isSuccess(), new DocumentManagerServiceDocument(((CmisConnectorDocument) documentManagerServiceResponse.getData()).getDocument(), ((CmisConnectorDocument) documentManagerServiceResponse.getData()).getHashCode()));
			}
			return documentManagerServiceResponse;
		} catch (Exception e) {
			return new DocumentManagerServiceResponse(false, e.getMessage());
		}
	}
}

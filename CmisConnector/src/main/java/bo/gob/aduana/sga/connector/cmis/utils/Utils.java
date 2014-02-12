package bo.gob.aduana.sga.connector.cmis.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisVersioningException;

public class Utils {
	public static CmisObject getCmisObjectFromChildrens(ItemIterable<CmisObject> childrens, String name) {
		for (CmisObject cmisObject : childrens) {
			if (cmisObject.getName().equalsIgnoreCase(name)) {
				return cmisObject;
			} else if (ObjectType.FOLDER_BASETYPE_ID.equalsIgnoreCase(cmisObject.getType().getId())) {
				getCmisObjectFromChildrens(((Folder) cmisObject).getChildren(), name);
			}
		}
		throw new CmisObjectNotFoundException("Object name " + name + " not found");
	}

	public static String getContentAsString(ContentStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();
		Reader reader = new InputStreamReader(stream.getStream(), "UTF-8");

		try {
			final char[] buffer = new char[4 * 1024];
			int b;
			while (true) {
				b = reader.read(buffer, 0, buffer.length);
				if (b > 0) {
					sb.append(buffer, 0, b);
				} else if (b == -1) {
					break;
				}
			}
		} finally {
			reader.close();
		}

		return sb.toString();
	}

	public static Boolean isVersionable(Document doc) {
		return ((DocumentType) doc.getType()).isVersionable();
	}

	public static boolean isDocument(CmisObject obj) {
		return (BaseTypeId.CMIS_DOCUMENT.value()).equalsIgnoreCase(obj.getType().getId());
	}

	public static Map<String, Object> getDefaultDocumentProperties(String fileName) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, fileName);
		return properties;
	}

	public static Document findVersion(String versionLabel, List<Document> documents) {
		for (Document document : documents) {
			if (document.getVersionLabel().equalsIgnoreCase(versionLabel)) {
				return document;
			}
		}
		throw new CmisVersioningException("Version " + versionLabel + " not found for documents " + documents);
	}

	public static Map<String, String> getCreateSessionParameters(String user, String password, String atomPubUrl, String repoId) {
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.USER, user);
		parameter.put(SessionParameter.PASSWORD, password);
		parameter.put(SessionParameter.ATOMPUB_URL, atomPubUrl);
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.REPOSITORY_ID, repoId);
		return parameter;
	}
}

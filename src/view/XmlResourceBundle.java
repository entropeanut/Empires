package view;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlResourceBundle extends ResourceBundle {
	
	private Properties props;

	XmlResourceBundle(InputStream stream) 
			throws IOException, ParserConfigurationException, SAXException, TransformerException {

		props = new Properties();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new InputSource(stream));

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		NodeList nodeList = document.getFirstChild().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (Node.ELEMENT_NODE == node.getNodeType()) {
				DOMSource source = new DOMSource(node);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				StreamResult result = new StreamResult(outputStream);

				transformer.transform(source, result);
				props.put(node.getAttributes().getNamedItem("key").getNodeValue(), outputStream.toString());
			}
		}
	}

	protected Object handleGetObject(String key) {
		return props.getProperty(key);
	}

	public Enumeration<String> getKeys() {
		return Collections.enumeration(props.stringPropertyNames());
	}

	public static class Control extends ResourceBundle.Control {

		public List<String> getFormats(String baseName) {
			if (baseName == null)
				throw new NullPointerException();
			return Arrays.asList("xml");
		}

		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
				throws IOException {

			if (baseName == null || locale == null || format == null || loader == null) {
				throw new NullPointerException();
			}

			ResourceBundle bundle = null;
			if (format.equals("xml")) {
				String bundleName = toBundleName(baseName, locale);
				String resourceName = toResourceName(bundleName, format);
				InputStream stream = null;
				if (reload) {
					URL url = loader.getResource(resourceName);
					if (url != null) {
						URLConnection connection = url.openConnection();
						if (connection != null) {
							// Disable caches to get fresh data for
							// reloading.
							connection.setUseCaches(false);
							stream = connection.getInputStream();
						}
					}
				} else {
					stream = loader.getResourceAsStream(resourceName);
				}
				if (stream != null) {
					BufferedInputStream bis = new BufferedInputStream(stream);
					try {
						bundle = new XmlResourceBundle(bis);
					} catch (ParserConfigurationException | SAXException | TransformerException e) {
						throw new IOException(e);
					}
					bis.close();
				}
			}
			return bundle;
		}
	}
}


package com.longfe.parse.sql;import com.longfe.test.Person;import ognl.Ognl;import org.w3c.dom.*;import org.xml.sax.SAXException;import javax.xml.parsers.DocumentBuilder;import javax.xml.parsers.DocumentBuilderFactory;import javax.xml.parsers.ParserConfigurationException;import java.io.IOException;import java.util.ArrayList;import java.util.List;import java.util.Map;import java.util.concurrent.ConcurrentHashMap;public class XMLScriptSqlBuilder {	private Map<String, SqlNode> sqlMap = new ConcurrentHashMap<String, SqlNode>();	public static void main(String[] args) throws Exception {		(new XMLScriptSqlBuilder()).readXML();	}	public void readXML() throws Exception {		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		try {			DocumentBuilder builder = factory.newDocumentBuilder();			Document doc = builder.parse(XMLScriptSqlBuilder.class.getResourceAsStream("sql.xml"));			Element element = doc.getDocumentElement();			parseScriptNode(element);			Person person = new Person();			person.setName("lonfe");			getSql("select", person);		} catch (ParserConfigurationException e) {			e.printStackTrace();		} catch (SAXException e) {			e.printStackTrace();		} catch (IOException e) {			e.printStackTrace();		}	}	public String getSql(String sqlId, Object parameter) throws Exception {		SqlNode sqlNode = sqlMap.get(sqlId);		StringBuffer sql = new StringBuffer();		if (sqlNode instanceof SelectSqlNode) {			SelectSqlNode selectSqlNode = (SelectSqlNode) sqlNode;			List<SqlNode> sqlNodes = selectSqlNode.getSqlNodes();			for (SqlNode node : sqlNodes) {				if (node instanceof TextSqlNode) {					sql.append(((TextSqlNode) node).getContent());				} else if (node instanceof IfSqlNode) {					String test = ((IfSqlNode) node).getTest();					Object expression = Ognl.parseExpression(test);					if (Ognl.getValue(expression, parameter).equals(true)) {						String str = ((TextSqlNode) ((IfSqlNode) node).getSqlNodes().get(0)).getContent();						sql.append(parseExpression(str, parameter));					}				}			}		}		return sql.toString();	}	public String parseExpression(String expression, Object parameter) throws Exception {		String openTag = "{";		String closeTag = "}";		int start = expression.trim().indexOf(openTag);		int end = expression.trim().indexOf(closeTag);		String strPre = expression.trim().substring(0, start - 1);		String strMid = expression.trim().substring(start + 1, end);		String strSuf = expression.trim().substring(end + 1);		StringBuffer stringBuffer = new StringBuffer();		stringBuffer.append(strPre).append(Ognl.getValue(Ognl.parseExpression(strMid), parameter)).append(strSuf);		return stringBuffer.toString();	}	public void parseScriptNode(Node node) throws Exception {		String nodeName = node.getNodeName();		NamedNodeMap nnmap = node.getAttributes();		//如果是select标签,解析成SelectSqlNode		if ("select".equals(nodeName)) {			SelectSqlNode sqlNode = new SelectSqlNode();			if (nnmap.getLength() == 0 || nnmap.getNamedItem("id") == null || nnmap.getNamedItem("parameterType") == null) {				throw new Exception("id or parameterType is empty!");			} else {				String id = nnmap.getNamedItem("id").getNodeValue();				String parameterType = nnmap.getNamedItem("parameterType").getNodeValue();				sqlNode.setId(id);				sqlNode.setParameterType(parameterType);				sqlNode.setSqlNodes(parseDynamicTags(node));				sqlMap.put(id, sqlNode);			}		}	}	public List<SqlNode> parseDynamicTags(Node node) throws Exception {		List<SqlNode> contents = new ArrayList<SqlNode>();		NodeList children = node.getChildNodes();		for (int i = 0; i < children.getLength(); i++) {			Node child = children.item(i);			if (child.getNodeType() == Node.CDATA_SECTION_NODE || child.getNodeType() == Node.TEXT_NODE) {				TextSqlNode textSqlNode = new TextSqlNode();				textSqlNode.setContent(child.getTextContent());				contents.add(textSqlNode);			} else if (child.getNodeType() == Node.ELEMENT_NODE) {				String nodeName = child.getNodeName();				NamedNodeMap nnmap = child.getAttributes();				//如果是if标签,解析成IfSqlNode				if ("if".equals(nodeName)) {					IfSqlNode ifSqlNode = new IfSqlNode();					if (nnmap.getLength() == 0 || nnmap.getNamedItem("test") == null) {						throw new Exception("test is empty!");					} else {						String test = nnmap.getNamedItem("test").getNodeValue();						ifSqlNode.setTest(test);						List<SqlNode> ifSqlNodeContents = parseDynamicTags(child);						ifSqlNode.setSqlNodes(ifSqlNodeContents);						contents.add(ifSqlNode);					}				}			}		}		return contents;	}}
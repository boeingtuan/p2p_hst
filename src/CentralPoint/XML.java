package CentralPoint;

import Cryptography.PeerKey;
import Server.UserDatabase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML {

    private final String XML_Str;
    public Document doc = null;

    public XML(String XML_Str) {
        this.XML_Str = XML_Str;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(new ByteArrayInputStream(this.XML_Str.getBytes()));
            this.doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("Document element exception: DEXMLlize()");
        }
    }

    public String firstTag() {
        return doc.getDocumentElement().getNodeName();
    }

    public PeerInfo getRegister() throws Exception {
        String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement());
        String password = UserDatabase.getTargetValue(ConstantTags.PASSWORD_TAG, doc.getDocumentElement());
        int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement()));
        String keyString = UserDatabase.getTargetValue(ConstantTags.KEY_TAG, doc.getDocumentElement());
        
        return new PeerInfo(username, password, portNum, PeerInfo.REGISTER, new PeerKey.Public(keyString));
    }

    public PeerInfo getLogin() throws Exception {
        String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement());
        String password = UserDatabase.getTargetValue(ConstantTags.PASSWORD_TAG, doc.getDocumentElement());
        int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement()));
        String keyString = UserDatabase.getTargetValue(ConstantTags.KEY_TAG, doc.getDocumentElement());

        return new PeerInfo(username, password, portNum, PeerInfo.LOGIN, new PeerKey.Public(keyString));
    }

    public OnlinePeerInfo getOnlinePeer() throws Exception {
        ArrayList<PeerInfo> lstOnlinePeer = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName(ConstantTags.PEER_TAG);
        for (int i = 0; i < nList.getLength(); i++) {
            Element elemPeer = (Element) nList.item(i);
            String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, elemPeer);
            String IP = UserDatabase.getTargetValue(ConstantTags.IP_TAG, elemPeer);
            int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, elemPeer));
            String keyString = UserDatabase.getTargetValue(ConstantTags.KEY_TAG, elemPeer);
            
            PeerInfo info = new PeerInfo(username, IP, portNum);
            info.setPKey(new PeerKey.Public(keyString));
            lstOnlinePeer.add(info);
        }
        return new OnlinePeerInfo(lstOnlinePeer);
    }

    public RegisResponeInfo getRegisterRespone() throws Exception {
        System.out.println(doc.getDocumentElement().getNodeName());
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.SESSION_DENY_TAG)) {
            return new RegisResponeInfo(false);
        } else {
            return new RegisResponeInfo(getOnlinePeer(), true);
        }
    }

    public StatusInfo getClientStatus() throws Exception {
        if (doc.getDocumentElement().getChildNodes().item(0).getNodeValue().equals(ConstantTags.ALIVE)) {
            return new StatusInfo(StatusInfo.ALIVE);
        } else {
            return new StatusInfo(StatusInfo.DYING);
        }
    }

    public ChatRequestInfo getChatRequest() throws Exception {
        return new ChatRequestInfo(UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement()));
    }

    public ChatResponeInfo getChatRespone() throws Exception {
        return new ChatResponeInfo(doc.getDocumentElement().getNodeName().equals(ConstantTags.CHAT_ACCEPT_TAG));
    }

    public MessageInfo getMessage() throws Exception {
        return new MessageInfo(doc.getDocumentElement().getChildNodes().item(0).getNodeValue());
    }

    public ChatCloseInfo getChatClose() throws Exception {
        return new ChatCloseInfo(true);
    }

    public FileNameInfo getFileName() throws Exception {
        String fileName = UserDatabase.getTargetValue(ConstantTags.FILE_NAME_TAG, doc.getDocumentElement());
        String fileSize = UserDatabase.getTargetValue(ConstantTags.FILE_SIZE_TAG, doc.getDocumentElement());
        return new FileNameInfo(fileName, fileSize);
    }

    public FileAckInfo getFileAck() throws Exception {
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.FILE_REQ_NOACK_TAG)) {
            return new FileAckInfo(false);
        } else {
            return new FileAckInfo(true, Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement())));
        }
    }

    public PairUser getPairUser() throws Exception {
        Element elem = doc.getDocumentElement();
        String user1 = ((Node) elem.getElementsByTagName(ConstantTags.USERNAME_TAG).item(0).getFirstChild()).getNodeValue();
        String user2 = ((Node) elem.getElementsByTagName(ConstantTags.USERNAME_TAG).item(1).getFirstChild()).getNodeValue();
        return new PairUser(user1, user2);
    }

    public Conversation getConversation() throws Exception {
        return new Conversation(getPairUser(), UserDatabase.getTargetValue(ConstantTags.TEXT_TAG, doc.getDocumentElement()));
    }

    public String getText() throws Exception {
        return doc.getDocumentElement().getFirstChild().getNodeValue();
    }

    public static String createUserXML(String username, String password, String tag, int port, String pKey) throws Exception {
        // Initialize
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement(tag);
        doc.appendChild(rootElement);

        Element newElem = doc.createElement(ConstantTags.USER_TAG);
        newElem.appendChild(createNode(ConstantTags.USERNAME_TAG, username, doc));
        newElem.appendChild(createNode(ConstantTags.PASSWORD_TAG, password, doc));
        newElem.appendChild(createNode(ConstantTags.PORT_TAG, String.valueOf(port), doc));
        newElem.appendChild(createNode(ConstantTags.KEY_TAG, pKey, doc));

        rootElement.appendChild(newElem);

        //ToString
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createStatusXML(String content) throws Exception {
        // Initialize
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = createNode(ConstantTags.STATUS_TAG, content, doc);
        doc.appendChild(rootElement);

        //ToString
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createMessage(String content) throws Exception {
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        doc.appendChild(createNode(ConstantTags.CHAT_MSG_TAG, content, doc));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createFileRequest(String fileName, String fileSize) throws Exception {
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement(ConstantTags.FILE_REQ_TAG);
        doc.appendChild(rootElement);
        rootElement.appendChild(createNode(ConstantTags.FILE_NAME_TAG, fileName, doc));
        rootElement.appendChild(createNode(ConstantTags.FILE_SIZE_TAG, fileSize, doc));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createChatRequestXML(String content) throws Exception {
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement(ConstantTags.CHAT_REQ_TAG);
        doc.appendChild(rootElement);
        rootElement.appendChild(createNode(ConstantTags.USERNAME_TAG, content, doc));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createSaveConversation(Conversation con) throws Exception {
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement(ConstantTags.SAVE_CONVERSATION_TAG);
        doc.appendChild(rootElement);

        rootElement.appendChild(createNode(ConstantTags.USERNAME_TAG, con.getPairUser().getUser1(), doc));
        rootElement.appendChild(createNode(ConstantTags.USERNAME_TAG, con.getPairUser().getUser2(), doc));
        rootElement.appendChild(createNode(ConstantTags.TEXT_TAG, con.getText(), doc));

        //ToString
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    public static String createConversation(PairUser pairUser) throws Exception {
        String res = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement(ConstantTags.CONVERSATION_TAG);
        doc.appendChild(rootElement);

        rootElement.appendChild(createNode(ConstantTags.USERNAME_TAG, pairUser.getUser1(), doc));
        rootElement.appendChild(createNode(ConstantTags.USERNAME_TAG, pairUser.getUser2(), doc));

        //ToString
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();

        return sb.toString();
    }

    private static Element createNode(String tag, String content, Document doc) throws ParserConfigurationException {
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(content));

        return elem;
    }

    /*public static void main(String[] args) throws Exception {
     String x = createRegisterXML("boeingtuan", "password", 4508);
     XML a = new XML(x);
     System.out.println(a.getRegister().getPortNum());
     }*/
}

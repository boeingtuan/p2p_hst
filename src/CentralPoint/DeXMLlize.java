package CentralPoint;

import Server.UserDatabase;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DeXMLlize {
    private final String XML_Str;
    private Document doc = null;
      
    public DeXMLlize(String XML_Str) {
        this.XML_Str = XML_Str;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();        
            this.doc = dBuilder.parse(new ByteArrayInputStream(this.XML_Str.getBytes()));             
            this.doc.getDocumentElement().normalize();            
        }
        catch (Exception e) {
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
        return new PeerInfo(username, password, portNum, PeerInfo.REGISTER);
    }
    
    public PeerInfo getLogin() throws Exception {
        String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement());
        String password = UserDatabase.getTargetValue(ConstantTags.PASSWORD_TAG, doc.getDocumentElement());
        int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement()));
        return new PeerInfo(username, password, portNum, PeerInfo.LOGIN);        
    }
    
    public OnlinePeerInfo getOnlinePeer() throws Exception {
        ArrayList<PeerInfo> lstOnlinePeer = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName(ConstantTags.PEER_TAG);
        for (int i = 0; i < nList.getLength(); i++) {
            Element elemPeer = (Element) nList.item(i);
            String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, elemPeer);
            String IP = UserDatabase.getTargetValue(ConstantTags.IP_TAG, elemPeer);
            int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, elemPeer));
            lstOnlinePeer.add(new PeerInfo(username, IP, portNum));            
        }
        return new OnlinePeerInfo(lstOnlinePeer);
    }
    
    public RegisResponeInfo getRegisterRespone() throws Exception {
        System.out.println(doc.getDocumentElement().getNodeName());
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.SESSION_DENY_TAG)) {
            return new RegisResponeInfo(false);
        }
        else {
            return new RegisResponeInfo(getOnlinePeer(), true);
        }
    }
    
    public StatusInfo getClientStatus() throws Exception {
        if (doc.getDocumentElement().getChildNodes().item(0).getNodeValue().equals(ConstantTags.ALIVE)) {
            return new StatusInfo(StatusInfo.ALIVE);
        }
        else {
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
        return new FileNameInfo(doc.getDocumentElement().getChildNodes().item(0).getNodeValue());
    }
    
    public FileAckInfo getFileAck() throws Exception {
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.FILE_REQ_NOACK_TAG))
            return new FileAckInfo(false);
        else 
            return new FileAckInfo(true, Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement())));
    }
    
    public FileContentInfo getContent() throws Exception {
        switch (doc.getDocumentElement().getNodeName()) {
            case ConstantTags.FILE_DATA_BEGIN_TAG: 
                return new FileContentInfo(true, null, false);
            case ConstantTags.FILE_DATA_TAG:
                return new FileContentInfo(true, doc.getDocumentElement().getChildNodes().item(0).getNodeValue().getBytes(), false);
            default:
                return new FileContentInfo(true, null, true);
        }
    }
    
    public static String createUserXML(String username, String password, String tag,int port) throws Exception {
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
    
    private static Element createNode(String tag, String content, Document doc) throws ParserConfigurationException {       
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(content));
        
        return elem;
    }    
    
    /*public static void main(String[] args) throws Exception {
        String x = createRegisterXML("boeingtuan", "password", 4508);
        DeXMLlize a = new DeXMLlize(x);
        System.out.println(a.getRegister().getPortNum());
    }*/
}

package CentralPoint;

import Server.UserDatabase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    
    public Object deXMLlize() throws Exception {
        System.out.println(getMessage().getMessage());
        return 1;
    }
    
    private PeerInfo getRegister() throws Exception {        
        String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement());
        String password = UserDatabase.getTargetValue(ConstantTags.PASSWORD_TAG, doc.getDocumentElement());
        int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement()));
        return new PeerInfo(username, password, portNum, PeerInfo.REGISTER);
    }
    
    private PeerInfo getLogin() throws Exception {
        String username = UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement());
        String password = UserDatabase.getTargetValue(ConstantTags.PASSWORD_TAG, doc.getDocumentElement());
        int portNum = Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement()));
        return new PeerInfo(username, password, portNum, PeerInfo.LOGIN);        
    }
    
    private OnlinePeerInfo getOnlinePeer() throws Exception {
        ArrayList<PeerInfo> lstOnlinePeer = new ArrayList<PeerInfo>();
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
    
    private RegisResponeInfo getRegisterRespone() throws Exception {
        System.out.println(doc.getDocumentElement().getNodeName());
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.SESSION_DENY_TAG)) {
            return new RegisResponeInfo(false);
        }
        else {
            return new RegisResponeInfo(getOnlinePeer(), true);
        }
    }
    
    private StatusInfo getClientStatus() throws Exception {
        if (doc.getDocumentElement().getChildNodes().item(0).getNodeValue().equals(ConstantTags.ALIVE)) {
            return new StatusInfo(StatusInfo.ALIVE);
        }
        else {
            return new StatusInfo(StatusInfo.DYING);
        }
    }
    
    private ChatRequestInfo getChatRequest() throws Exception {
        return new ChatRequestInfo(UserDatabase.getTargetValue(ConstantTags.USERNAME_TAG, doc.getDocumentElement()));
    }
    
    private ChatResponeInfo getChatRespone() throws Exception {
        return new ChatResponeInfo(doc.getDocumentElement().getNodeName().equals(ConstantTags.CHAT_ACCEPT_TAG));
    }
    
    private MessageInfo getMessage() throws Exception {
        return new MessageInfo(doc.getDocumentElement().getChildNodes().item(0).getNodeValue());
    }
    
    private ChatCloseInfo getChatClose() throws Exception {
        return new ChatCloseInfo(true);
    }
    
    private FileNameInfo getFileName() throws Exception {
        return new FileNameInfo(doc.getDocumentElement().getChildNodes().item(0).getNodeValue());
    }
    
    private FileAckInfo getFileAck() throws Exception {
        if (doc.getDocumentElement().getNodeName().equals(ConstantTags.FILE_REQ_NOACK_TAG))
            return new FileAckInfo(false);
        else 
            return new FileAckInfo(true, Integer.parseInt(UserDatabase.getTargetValue(ConstantTags.PORT_TAG, doc.getDocumentElement())));
    }
    
    private FileContentInfo getContent() throws Exception {
        switch (doc.getDocumentElement().getNodeName()) {
            case ConstantTags.FILE_DATA_BEGIN_TAG: 
                return new FileContentInfo(true, null, false);
            case ConstantTags.FILE_DATA_TAG:
                return new FileContentInfo(true, doc.getDocumentElement().getChildNodes().item(0).getNodeValue().getBytes(), false);
            default:
                return new FileContentInfo(true, null, true);
        }
    }
    
    public static void main(String[] args) throws Exception {
        String xmlRecords = "<CHAT_MSG>Chat 1321321313132 31321321321 3132132132 3132132131 313131 message</CHAT_MSG>";
        DeXMLlize x = new DeXMLlize(xmlRecords);
        x.deXMLlize();
    }
    
}

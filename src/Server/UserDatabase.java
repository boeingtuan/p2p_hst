package Server;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import CentralPoint.ConstantTags;
import CentralPoint.PairUser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;

public class UserDatabase {
    private String filepath;
    private Document doc = null;    
    
    UserDatabase(String filepath) {
        this.filepath = filepath;
        try {
            File xmlFile = new File(filepath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(xmlFile);
            this.doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("Document element exception");
        }             
    }
    
    public boolean isDatabaseOK() {
        return doc != null;
    }
    
    public boolean isUserExist(String username) {
        try {
            NodeList nList = doc.getElementsByTagName(ConstantTags.USER_TAG);
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (getTargetValue(ConstantTags.USERNAME_TAG, element).equals(username)) {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            System.out.println("Database exception: isUserExist()");
            return false;
        }
    }
    
    public boolean checkLogin(String username, String password) {
        if (!isUserExist(username)) return false;
        
        try {
            NodeList nList = doc.getElementsByTagName(ConstantTags.USER_TAG);
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (getTargetValue(ConstantTags.USERNAME_TAG, element).equals(username) && 
                        getTargetValue(ConstantTags.PASSWORD_TAG, element).equals(password)) {
                        return true;
                    }
                }
            }
            return false;            
        }
        catch (Exception e) {
            System.out.println("Database exception: checkLogin()");
            return false;
        }
    }
    
    public boolean addUser(String username, String password) {
        if (isUserExist(username)) 
            return false;
        try {
            Node data = doc.getLastChild();
            
            Element newuser = doc.createElement(ConstantTags.USER_TAG);
            newuser.appendChild(createNode(ConstantTags.USERNAME_TAG, username, doc));
            newuser.appendChild(createNode(ConstantTags.PASSWORD_TAG, password, doc));            
            data.appendChild(newuser);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
            return true;
        }
        catch (Exception e) {
            System.out.println("Database exception: addUser()");
            return false;
        }
    }
    
    public HashMap<PairUser, String> getConversation() {
        HashMap<PairUser, String> res = null;
        try {            
            res = new HashMap<>();
            NodeList nList = doc.getElementsByTagName(ConstantTags.SAVE_CONVERSATION_TAG);
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                Element elem = (Element) node;
                String user1 = ((Node) elem.getElementsByTagName(ConstantTags.USERNAME_TAG).item(0).getFirstChild()).getNodeValue();
                String user2 = ((Node) elem.getElementsByTagName(ConstantTags.USERNAME_TAG).item(1).getFirstChild()).getNodeValue();
                String text = ((Node) elem.getElementsByTagName(ConstantTags.TEXT_TAG).item(0).getFirstChild()).getNodeValue();
                
                res.put(new PairUser(user1, user2), text);
                res.put(new PairUser(user2, user1), text);
            }           
        }
        catch (Exception e) {
            System.out.println("Exception getConversation");
        }
        return res;
    }
    
    public void writeConversation(String str) {
        try {
            Files.write(Paths.get(filepath), str.getBytes(), StandardOpenOption.APPEND);
        }
        catch (Exception e) {
            System.out.println("Exception writeConversation");
        }        
    }
    
    public static String getTargetValue(String tag, Element xmlTree) {
        Node node = (Node) xmlTree.getElementsByTagName(tag).item(0).getChildNodes().item(0);
        return node.getNodeValue();
    }
    
    private Element createNode(String tag, String content, Document doc) throws ParserConfigurationException {       
        Element elem = doc.createElement(tag);
        elem.appendChild(doc.createTextNode(content));
        
        return elem;
    }    
    
    /*public static void main(String[] a) {
        String x = "C:\\Users\\Tuân\\Documents\\NetBeansProjects\\ChatAssignment\\Data.xml";
        UserDatabase at = new UserDatabase(x);
        at.addUser("boeing", "123456");
    }*/
}

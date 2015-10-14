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
            Element newusername = doc.createElement(ConstantTags.USERNAME_TAG);
            Element newpassword = doc.createElement(ConstantTags.PASSWORD_TAG);
            newuser.appendChild(newusername); newuser.appendChild(newpassword);
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
    
    public static String getTargetValue(String tag, Element xmlTree) {
        Node node = (Node) xmlTree.getElementsByTagName(tag).item(0).getChildNodes().item(0);
        return node.getNodeValue();
    }
}

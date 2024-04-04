import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParserDemo {

    public static void main(String[] args) {

        try {
            File inputFile = new File("C:\\Users\\assist-lab\\IdeaProjects\\Functional-Reasoning\\data\\CoffeeMaker.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("XMI.documentation");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
//                    System.out.println("Student roll no : "
//                            + eElement.getAttribute("rollno"));
                    System.out.println(eElement
                            .getElementsByTagName("XMI.exporter")
                            .item(0)
                            .getTextContent());
                }
            }

            NodeList classList = doc.getElementsByTagName("UML:Class");
            Node classNode = classList.item(2);
            System.out.println("\nCurrentElement: " + classNode.getNodeName());
            Element classElement = (Element) classNode;
            System.out.println("Name: " + classElement.getAttribute("name"));
            System.out.println("xmi.id: " + classElement.getAttribute("xmi.id"));

            NodeList taggedValueList = classElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode = taggedValueList.item(2);
            Element taggedValueElement = (Element) taggedValueNode;
            System.out.println("xmi.id of owner: " + taggedValueElement.getAttribute("value"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
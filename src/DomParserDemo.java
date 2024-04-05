import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.List;
import java.util.ArrayList;

public class DomParserDemo {

    public static record UMLClass(String name, String xmi/*, String type, String xmiOfOwner*/) {
    }

    public static record Association(String name, String xmi, String sourceName, String destinationName, String xmiOfSource, String xmiOfDestination) {
    }

    public static record ClassifierRole(String name, String xmi, String type, String xmiOfOwner, String propertyType, String reusesProperty) {
    }

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

            List<UMLClass> allClasses = new ArrayList<UMLClass>();
            NodeList classList = doc.getElementsByTagName("UML:Class");
            for (int i = 0; i < classList.getLength(); i++) {
                Node classNode = classList.item(i);
                Element classElement = (Element) classNode;
                allClasses.add(new UMLClass (classElement.getAttribute("name"), classElement.getAttribute("xmi.id")));
            }

            //System.out.println("\nCurrentElement: " + classNode.getNodeName());
            //Element classElement = (Element) classNode;
            //System.out.println("Name: " + classElement.getAttribute("name"));
            //System.out.println("xmi.id: " + classElement.getAttribute("xmi.id"));

            //NodeList taggedValueList = classElement.getElementsByTagName("UML:TaggedValue");
            //Node taggedValueNode = taggedValueList.item(2);
            //Element taggedValueElement = (Element) taggedValueNode;
            //System.out.println("xmi.id of owner: " + taggedValueElement.getAttribute("value"));

            System.out.println(allClasses.get(2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
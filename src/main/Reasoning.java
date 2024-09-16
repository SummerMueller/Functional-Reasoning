package main;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.HashMap;


public class Reasoning {
    
   public static record Block(String name, String xmi) {
   }
    
   public static record Port(String name, String xmi, String xmiOfOwner, 
         String reusesProperty) {
   }

   public static record Association(String name, String xmi, String sourceName, 
         String destinationName, String xmiOfSource, String xmiOfDestination) {
   }

   public static record ClassifierRole(String name, String xmi, String type, 
         String xmiOfOwner, String propertyType, String reusesProperty) {
   }
   
   public static record Function(String name, String xmi, String xmiOfOwner) {
   }
   
   public static record IBD(String name, String xmi, ArrayList<String> subjects) {
   }
   
   
   public static record DanglingNode(String IBD, String portName, String xmiOfPort, 
         String roleName, String xmiOfRole) {
   }
   
   public static record BalanceLaws(String IBD, String roleName, String xmiOfRole,
         String dPorts, String sPorts) {
   }
   
   public static record NoFlows(String IBD, String roleName, String xmiOfRole) {
   }
   
   public static record DanglingEdge(String name, String xmi, String sourceName,
         String xmiOfSource, String destinationName, String xmiOfDestination) {
   }
   
   public static record FlowIntegrityTypeI(String IBD, String associationName,
         String xmiOfAssociation, String sPortOwner, String sPort,
         String xmiOfsPort, String dPortOwner, String dPort, String xmiOfdPort) {
   }
   
   public static record FlowIntegrityTypeII(String blockName,
         String xmiOfBlock, String port, String xmiOfPort) {
   }

   public static void main(String[] args) {

      try {
         
         // Sets up the file
         //String file = "C:\\Users\\summe\\Functional main.Reasoning\\ActiveStandby_NLP.xml";
         String file = "C:\\Users\\summe\\Functional Reasoning\\data\\CoffeeMaker_NLP.xml";
         //String file = "C:\\Users\\summe\\Functional main.Reasoning\\NoFlows_Condensed.xml";
         //String file = "C:\\Users\\summe\\Functional main.Reasoning\\CoffeeMaker_Manual.xml";
         //String file = "C:\\Users\\summe\\Functional main.Reasoning\\FGS_NLP.xml";
         //String file = "C:\\Users\\summe\\Functional main.Reasoning\\HairDryer_NLP.xml";
         File inputFile = new File(file);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("XMI.documentation");
         System.out.printf ("----------------------------%n%n");


         // Extracts the system name from the file
         int start, end;
         String currentSystem = "";
         start = file.lastIndexOf("\\");
         end = file.indexOf(".xml");
         currentSystem = file.substring(start+1,end);
         
         // Prints out the file name
         System.out.printf ("%s%n%n%n", currentSystem);
         
         
         // Begins the extract knowledge algorithm
         
         // Creates an ArrayList of records to hold all the blocks and another one for the ports
         ArrayList<Block> allBlocks = new ArrayList<Block>();
         ArrayList<Port> allPorts = new ArrayList<Port>();
            
         // Selects only the UML:Class blocks nested within the UML:Namespace.ownedElement block
         NodeList namespaceList = doc.getElementsByTagName("UML:Namespace.ownedElement");
         Node namespaceNode = namespaceList.item(0);
         Element namespaceElement = (Element) namespaceNode;
         NodeList classList = namespaceElement.getElementsByTagName("UML:Class");
            
         // Iterates over each UML:Class tag
         for (int i = 0; i < classList.getLength(); i++) {
               
            Node classNode = classList.item(i);
            Element classElement = (Element) classNode;
                
            NodeList taggedValueList = classElement.getElementsByTagName("UML:TaggedValue");
                
            // Ignores the first UML:Class tag
            if (!(taggedValueList.item(0) == null)) {
                  
               // Checks if the current UML:Class tag is a block, port, or if theres an error
               Node taggedValueNode0 = taggedValueList.item(0);
               Element taggedValueElement0 = (Element) taggedValueNode0;
               String type = taggedValueElement0.getAttribute("value");
               
               // Adds a new block or port to the appropriate list
               if (type.equals("Class")) {
                  allBlocks.add(new Block (classElement.getAttribute("name"), 
                        classElement.getAttribute("xmi.id")));
               } else if (type.equals("Port")) {
                  Node taggedValueNode2 = taggedValueList.item(2);
                  Element taggedValueElement2 = (Element) taggedValueNode2;
                     
                  // Determines if the port has a reuses property
                  String reusesProperty;
                  if (taggedValueList.getLength() >= 5) {
                     Node taggedValueNode4 = taggedValueList.item(4);
                     Element taggedValueElement4 = (Element) taggedValueNode4;
                     reusesProperty = taggedValueElement4.getAttribute("value");
                  } else {
                     reusesProperty = "";
                  }
                  allPorts.add(new Port (classElement.getAttribute("name"), 
                        classElement.getAttribute("xmi.id"),
                        taggedValueElement2.getAttribute("value"), 
                        reusesProperty));
               } else {
                  System.out.printf ("Invalid UML:Class tag at %s%n", 
                        classElement.getAttribute("name"));
               }
            }
         }
         
         // Creates an ArrayList of records to hold all the associations
         ArrayList<Association> allAssociations = new ArrayList<Association>();
         
         NodeList associationList = namespaceElement.getElementsByTagName("UML:Association");
         
         // Iterates over each UML:Association tag
         for (int i = 0; i < associationList.getLength(); i++) {
            
            Node associationNode = associationList.item(i);
            Element associationElement = (Element) associationNode;
            
            NodeList taggedValueList = associationElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode2 = taggedValueList.item(2);
            Element taggedValueElement2 = (Element) taggedValueNode2;
            Node taggedValueNode3 = taggedValueList.item(3);
            Element taggedValueElement3 = (Element) taggedValueNode3;
            
            NodeList aEndList = associationElement.getElementsByTagName("UML:AssociationEnd");
            Node aEndNode0 = aEndList.item(0);
            Element aEndElement0 = (Element) aEndNode0;
            Node aEndNode1 = aEndList.item(1);
            Element aEndElement1 = (Element) aEndNode1;
            
            // Adds a new association to the list of all associations
            allAssociations.add(new Association (associationElement.getAttribute("name"), 
                  associationElement.getAttribute("xmi.id"), 
                  taggedValueElement2.getAttribute("value"),
                  taggedValueElement3.getAttribute("value"),
                  aEndElement0.getAttribute("type"),
                  aEndElement1.getAttribute("type")));
         }
         
         // Creates an ArrayList of records to hold all the classifier roles
         ArrayList<ClassifierRole> allRoles = new ArrayList<ClassifierRole>();
         
         NodeList roleList = namespaceElement.getElementsByTagName("UML:ClassifierRole");
         
         // Iterates over each UML:ClassifierRole
         for (int i = 0; i < roleList.getLength(); i++) {
            
            Node roleNode = roleList.item(i);
            Element roleElement = (Element) roleNode;
            
            NodeList taggedValueList = roleElement.getElementsByTagName("UML:TaggedValue");
            
            Node taggedValueNode0 = taggedValueList.item(0);
            Element taggedValueElement0 = (Element) taggedValueNode0;
            Node taggedValueNode2 = taggedValueList.item(2);
            Element taggedValueElement2 = (Element) taggedValueNode2;
            Node taggedValueNode4 = taggedValueList.item(4);
            Element taggedValueElement4 = (Element) taggedValueNode4;
            
            // Determines if the classifier role has a reuses property
            String reusesProperty;
            if (taggedValueList.getLength() >= 6) {
               Node taggedValueNode5 = taggedValueList.item(5);
               Element taggedValueElement5 = (Element) taggedValueNode5;
               reusesProperty = taggedValueElement5.getAttribute("value");
            } else {
               reusesProperty = "";
            }
            
            // Adds a new classifier role to the list of all classifier roles
            allRoles.add(new ClassifierRole (roleElement.getAttribute("name"), 
                  roleElement.getAttribute("xmi.id"),
                  taggedValueElement0.getAttribute("value"),
                  taggedValueElement2.getAttribute("value"),
                  taggedValueElement4.getAttribute("value"),
                  reusesProperty));
            
         }
         
         // Creates an ArrayList of records to hold all the functions
         ArrayList<Function> allFunctions = new ArrayList<Function>();
         
         NodeList functionList = doc.getElementsByTagName("UML:TaggedValue");
         
         // Iterates over each UML:TaggedValue
         for (int i = 0; i < functionList.getLength(); i++) {
            Node functionNode = functionList.item(i);
            Element functionElement = (Element) functionNode;
            
            // Verifies the tag is a function
            String tag = functionElement.getAttribute("tag");
            if (tag.length() > 4) {
               if (tag.substring(0,4).equals("Func")) {
                  
                  // Adds a new function to the list of all functions
                  allFunctions.add(new Function (functionElement.getAttribute("value").trim(),
                        functionElement.getAttribute("xmi.id"),
                        functionElement.getAttribute("modelElement")));
               }
            }
         }
         
         // Creates an ArrayList of records to hold all the IBDs
         ArrayList<IBD> allIBDs = new ArrayList<IBD>();
         
         NodeList ibdList = doc.getElementsByTagName("UML:Diagram");
         
         // Iterates over each UML:Diagram
         for (int i = 0; i < ibdList.getLength(); i++) {
            Node ibdNode = ibdList.item(i);
            Element ibdElement = (Element) ibdNode;
            
            // Checks to make sure its not including the main BDD
            if (!(ibdElement.getAttribute("name").equals("One Level Block Hierarchy"))) {
               
               // Creates an ArrayList to store all the components of each IBD
               ArrayList<String> allSubjects = new ArrayList<String>();
               
               NodeList subjectList = ibdElement.getElementsByTagName("UML:DiagramElement");

               // Iterates over each UML:DiagramElement per diagram and adds the components to an ArrayList
               for (int j = 0; j < subjectList.getLength(); j++) {
                  Node subjectNode = subjectList.item(j);
                  Element subjectElement = (Element) subjectNode;
                  allSubjects.add(subjectElement.getAttribute("subject"));
               }
               
               // Adds a new ibd to the list of all ibds
               allIBDs.add(new IBD (ibdElement.getAttribute("name"), 
                     ibdElement.getAttribute("xmi.id"), allSubjects));
            }
         }

         // Tests by printing the first element of each list
         /*System.out.println(allBlocks.get(0));
         System.out.println();
         System.out.println(allPorts.get(0));
         System.out.println();
         System.out.println(allAssociations.get(0));
         System.out.println();
         System.out.println(allRoles.get(0));
         System.out.println();
         System.out.println(allFunctions.get(0));
         for (IBD ibd : allIBDs) {
            System.out.printf("%s%n%n", ibd);
         }*/

         // Begins validating the dangling node inspection
         
         // Creates ArrayLists to hold the complete list of errors and their types
         ArrayList<DanglingNode> allNodes = new ArrayList<DanglingNode>();
         ArrayList<BalanceLaws> allLaws = new ArrayList<BalanceLaws>();
         ArrayList<NoFlows> allFlows = new ArrayList<NoFlows>();
         ArrayList<DanglingEdge> allEdges = new ArrayList<DanglingEdge>();
         ArrayList<FlowIntegrityTypeI> allIntegritiesI = new ArrayList<FlowIntegrityTypeI>();
         ArrayList<FlowIntegrityTypeII> allIntegritiesII = new ArrayList<FlowIntegrityTypeII>();
         
         // Creates two HashMaps to separately define the sources and destinations of each association
         HashMap<String, String> sources = new HashMap<String, String>();
         HashMap<String, String> destinations = new HashMap<String, String>();
         
         // Fills the maps with unique associations but not necessarily unique ports 
         for (Association association : allAssociations) {
           sources.put(association.xmi(), association.xmiOfSource());
           destinations.put(association.xmi(), association.xmiOfDestination());
         }
   
         // Validates that each port within an IBD is either the source or destination of an association
         for (Port port : allPorts) {
            if (!(port.reusesProperty().equals(""))) {
               if (!(sources.containsValue(port.xmi()) || destinations.containsValue(port.xmi()))) {
                  String ownerName = "";
                  // Finds the name of the owner's block
                  for (ClassifierRole role : allRoles) {
                     if (role.xmi().equals(port.xmiOfOwner())) {
                        ownerName = role.name();
                     }
                  }
                  allNodes.add(new DanglingNode (getIBD(allIBDs, port.xmi()), port.name(), port.xmi(), 
                        ownerName, port.xmiOfOwner()));
               }     
            }
         }
         
         
         // Begins validating the balance laws inspection
         
         // Creates ArrayLists to separately store source and destination ports
         ArrayList<Port> allDPorts = new ArrayList<Port>();
         ArrayList<Port> allSPorts = new ArrayList<Port>();
         
         // Separates each port into source and destination
         for (Association association : allAssociations) {
            for (Port port : allPorts) {
               if (port.xmi().equals(association.xmiOfDestination)) {
                  allDPorts.add(port);
               }
            }
            for (Port port : allPorts) {
               if (port.xmi().equals(association.xmiOfSource)) {
                  allSPorts.add(port);
               }
            }
         }
         
         // Iterates over all the roles to check each has a source and destination port
         for (ClassifierRole role : allRoles) {
            
            // Creates fields to store the source and destination ports of the current role
            String dPortsList = "";
            String sPortsList = "";
            
            // Determines if the role has a destination port
            boolean hasDPort = false;
            for (Port port : allDPorts) {
               if (role.xmi().equals(port.xmiOfOwner())) {
                  dPortsList = dPortsList.concat(port.name()).concat(" ");
                  hasDPort = true;
               }
            }
            
            // Determines if the role has a source port
            boolean hasSPort = false;
            for (Port port : allSPorts) {
               if (role.xmi().equals(port.xmiOfOwner())) {
                  sPortsList = sPortsList.concat(port.name()).concat(" ");
                  hasSPort = true;
               }
            }
            
            // Removes extra spaces at the end of dPort and sPort strings
            if (hasDPort) {
               dPortsList = dPortsList.substring(0,dPortsList.length()-1);
            }
            if (hasSPort) {
               sPortsList = sPortsList.substring(0,sPortsList.length()-1);
            }
            
            // If missing either a source or destination port, add to list of all errors
            if (!(hasDPort || hasSPort)) {
               allFlows.add(new NoFlows (getIBD(allIBDs, role.xmi()), role.name(), role.xmi()));
            } else if (!(hasDPort && hasSPort)) {
               allLaws.add(new BalanceLaws (getIBD(allIBDs, role.xmi()), role.name(), role.xmi(), dPortsList, sPortsList));
            }

         }
         
         // Checks for dangling edges
         for (Association asso : allAssociations) {
            boolean validSource = false;
            boolean validDestination = false;
            for (Port port : allPorts) {
               if (asso.xmiOfSource().equals(port.xmi())) {
                  validSource = true;
               }
               if (asso.xmiOfDestination().equals(port.xmi())) {
                  validDestination = true;
               }
            }
            if (!(validSource && validDestination)) {
               allEdges.add(new DanglingEdge (asso.name(), asso.xmi(), asso.sourceName(),
                  asso.xmiOfSource(), asso.destinationName(), asso.xmiOfDestination()));
            }
         }
         
         
         // Begins validating the flow integrity inspection
         
         // Defines separate lists to distinguish between types
         String[] solids = {"m", "s", "liq", "gas"};
         String[] energies = {"e", "ee", "me", "the", "che", "eme", "nue"};
         
         // Iterates over each association to ensure both ends match types
         for (Association asso : allAssociations) {

            // Initially sets types different in case a type isn't listed in either list
            String sType = "0";
            String dType = "1";

            // Manipulates the Strings to represent only each port's type
            String outer = removeDigits(asso.sourceName()).toLowerCase();
            String inner = removeDigits(asso.destinationName()).toLowerCase();
            String[] outerS = outer.split("_");
            String[] innerS = inner.split("_");
            
            // Determines if the type of either port is a solid 
            for (int i = 0; i < solids.length; i++) {
               if (solids[i].equals(outerS[1])) {
                  sType = "solid";
               }
               if (solids[i].equals(innerS[1])) {
                  dType = "solid";
               }
            }
            
            // Determines if the type of either port is an energy
            for (int i = 0; i < energies.length; i++) {
               if (energies[i].equals(outerS[1])) {
                  sType = "energy";
               }
               if (energies[i].equals(innerS[1])) {
                  dType = "energy";
               }
            }
            
            // Validates that both ends of an association have the same type
            if (!(sType.equals(dType))) {

               // Gets the name of the current IBD and the name of the port's owner
               String currentIBD = getIBD(allIBDs, asso.xmi());
               String sPortOwner = getPortOwner(allPorts, allRoles, asso.xmiOfSource()).name();
               String dPortOwner = getPortOwner(allPorts, allRoles, asso.xmiOfDestination()).name();
               
               // If the port owner's name is blank then it's owner is the same as the current IBD
               if (sPortOwner.equals("")) {
                  sPortOwner = currentIBD;
               }
               if (dPortOwner.equals("")) {
                  dPortOwner = currentIBD;
               }
               
               // If the types don't match then a new Type I FLow Integrity error is added to the list
               allIntegritiesI.add(new FlowIntegrityTypeI (currentIBD,
                     asso.name(), asso.xmi(), sPortOwner,
                     asso.sourceName(), asso.xmiOfSource(), dPortOwner,
                     asso.destinationName(), asso.xmiOfDestination()));
            }
         }
         

         // Prints the type of each error caught and its location
         for (DanglingNode error : allNodes) {
            System.out.printf ("%s%n%n", error);
         }
         for (BalanceLaws error : allLaws) {
            System.out.printf ("%s%n%n", error);
         }
         for (NoFlows error : allFlows) {
            System.out.printf ("%s%n%n", error);
         }
         for (DanglingEdge error : allEdges) {
            System.out.printf ("%s%n%n", error);
         }
         for (FlowIntegrityTypeI error : allIntegritiesI) {
            System.out.printf ("%s%n%n", error);
         }
         for (FlowIntegrityTypeII error : allIntegritiesII) {
            System.out.printf ("%s%n%n", error);
         }
         int totalErrors = allNodes.size() + allLaws.size() + allFlows.size() + 
               allEdges.size() + allIntegritiesI.size() + allIntegritiesII.size();
         System.out.printf ("Total errors: %d%n", totalErrors);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   // Finds and returns the owner IBD of any port, role, or other component
   public static String getIBD (final ArrayList<IBD> ibds, final String xmiOfComponent) {
      for (IBD ibd : ibds) {
         for (int i = 0; i < ibd.subjects().size(); i++) {
            if (ibd.subjects.get(i).equals(xmiOfComponent)) {
               return ibd.name();
            }
         }
      }
      return "";
   }
   
   // Finds and returns the owner of a port (assuming that owner is a classifier role)
   public static ClassifierRole getPortOwner (final ArrayList<Port> ports, final ArrayList<ClassifierRole> roles, final String xmiOfPort) {
      String owner = "";
      for (Port port : ports) {
         if (port.xmi().equals(xmiOfPort)) {
            owner = port.xmiOfOwner();
            for (ClassifierRole role : roles) {
               if (role.xmi().equals(owner)) {
                  return role;
               }
            }
         }
      }
      return new ClassifierRole ("","","","","","");
   }
   
   // Removes all numbers from a String and returns the resulting String
   public static String removeDigits (final String original) {
      String result = "";
      for (int i = 0; i < original.length(); i++) {
         if (!(original.charAt(i) == '0' || original.charAt(i) == '1' || original.charAt(i) == '2'
               || original.charAt(i) == '3' || original.charAt(i) == '4' || original.charAt(i) == '5'
               || original.charAt(i) == '6' || original.charAt(i) == '7' || original.charAt(i) == '8'
               || original.charAt(i) == '9')) {
                  result = result + original.charAt(i);
               }
      }
      return result;
   }
}

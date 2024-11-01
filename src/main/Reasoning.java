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
    
   // Main components for the BDDs and IBDs 
   public static record Block(String name, String xmi, ArrayList<String> ports) {
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
   
   public static record IBD(String name, String xmi, ArrayList<String> subjects) {
   }
   
   // Components of the activity diagrams
   public static record ActivityParameter(String name, String type, String xmi, String xmiOfOwner) {
   }
   
   public static record ActionPin(String name, String type, String xmi, String xmiOfOwner) {
   }
   
   public static record Action(String name, String xmi, String xmiOfOwner, 
         ArrayList<ActionPin> inPins, ArrayList<ActionPin> outPins) {
   }

   public static record Transition(String xmi, String sourceName, String targetName,
         String xmiOfSource, String xmiOfTarget, String sourceElementType, String targetElementType) {
   }
   
   public static record ActD(String name, String xmi, ArrayList<String> subjects,
         ArrayList<Action> actions) {
   }
   
   // Types of errors
   public static record IncompleteTopologyI(String blockName, String xmiOfBlock) {
   }
   
   public static record IncompleteTopologyII(String nodeName, String nodeType, String ownerName, 
         String ownerType, String diagramName, String diagramType, String xmiOfNode) {
   }
   
   /*public static record BalanceLaws(String IBD, String roleName, String xmiOfRole,
         String dPorts, String sPorts) {
   }
   
   public static record NoFlows(String IBD, String roleName, String xmiOfRole) {
   }
   
   public static record DanglingEdge(String name, String xmi, String sourceName,
         String xmiOfSource, String destinationName, String xmiOfDestination) {
   }*/
   
   public static record BalanceLawsI(String IBD, String associationName,
         String xmiOfAssociation, String sPortOwner, String sPort,
         String xmiOfsPort, String dPortOwner, String dPort, String xmiOfdPort) {
   }
   
   public static record BalanceLawsII(String blockName, String flowType, 
         int inputs, int outputs) {
   }
   
   public static record DanglingNode(String ActDName, String xmi) {
   }
   
   public static record UnknownFunction(String actionName, String ActDName,
         String xmiOfAction, String xmiOfActD) {
   }
   
   public static record FunctionalKB(String actionName, String ActDName,
         String xmiOfAction, String xmiOfActD) {
   }

   // Creates ArrayLists of records to hold all the components of BDDs and IBDs
   public static ArrayList<Block> allBlocks = new ArrayList<Block>();
   public static ArrayList<Port> allPorts = new ArrayList<Port>();
   public static ArrayList<Association> allAssociations = new ArrayList<Association>();
   public static ArrayList<ClassifierRole> allRoles = new ArrayList<ClassifierRole>();
   public static ArrayList<IBD> allIBDs = new ArrayList<IBD>();
   
   // Creates ArrayLists of records to hold the components of activity diagrams
   public static ArrayList<Action> allActions = new ArrayList<Action>();
   public static ArrayList<ActivityParameter> allParameters = new ArrayList<ActivityParameter>();
   public static ArrayList<ActionPin> allPins = new ArrayList<ActionPin>();
   public static ArrayList<Transition> allTransitions = new ArrayList<Transition>();
   public static ArrayList<ActD> allActDs = new ArrayList<ActD>();
   public static ArrayList<String> allDataTypes = new ArrayList<String>();
   
   public static ArrayList<ActionPin> sourcePins = new ArrayList<ActionPin>();
   public static ArrayList<ActionPin> targetPins = new ArrayList<ActionPin>();

   public static void main(String[] args) {

      try {
         boolean hasActD = !true;
         // Sets up the file
         //String file = "data/DanglingNode_Condensed.xml";
         //String file = "data/coffeemaker_manual_2024_Condensed.xml";
         String file = "data/HairDryer_NLP.xml";
         //String file = "data/Speaker_Manual_Condensed.xml";
         //String file = "data/VacuumCleaner_Condensed.xml";
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
         start = file.lastIndexOf("/");
         end = file.indexOf(".xml");
         currentSystem = file.substring(start+1,end);
         
         // Prints out the file name
         System.out.printf ("%s%n%n%n", currentSystem);
        
        
/**************************************************************************************************/       
         // Begins the extract knowledge algorithm
            
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
                        classElement.getAttribute("xmi.id"), new ArrayList<String>()));
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
               
               // Adds a new data type to the list of all data types
               } else if (type.equals("DataType")) {
                  allDataTypes.add(classElement.getAttribute("name"));
                  
               // Prints an error if the UML:Class isn't a block or port
               } else {
                  System.out.printf ("Invalid UML:Class tag at %s%n", 
                        classElement.getAttribute("name"));
               }
            }
         }
         
         NodeList roleList = namespaceElement.getElementsByTagName("UML:ClassifierRole");
         
         // Iterates over each UML:ClassifierRole
         for (int i = 0; i < roleList.getLength(); i++) {
            
            // Where main info is labeled like the name and xmi
            Node roleNode = roleList.item(i);
            Element roleElement = (Element) roleNode;
            
            // Gets the type to determine what it is
            NodeList taggedValueList = roleElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode0 = taggedValueList.item(0);
            Element taggedValueElement0 = (Element) taggedValueNode0;
            String type = taggedValueElement0.getAttribute("value");
            
            // Other common values relevant to parts, pins, and parameters
            Node taggedValueNode3 = taggedValueList.item(3);
            Element taggedValueElement3 = (Element) taggedValueNode3;
            Node taggedValueNode4 = taggedValueList.item(4);
            Element taggedValueElement4 = (Element) taggedValueNode4;
            
            // If its a part in an IBD create a new classifier role object with apropriate fields
            if (type.equals("Part")) {
               
               // Gets xmi of owner
               Node taggedValueNode2 = taggedValueList.item(2);
               Element taggedValueElement2 = (Element) taggedValueNode2;
               
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
            
            // Adds a new activity parameter to the list of all activity parameters
            } else if (type.equals("ActivityParameter")) {
               allParameters.add(new ActivityParameter (roleElement.getAttribute("name"),
                     taggedValueElement4.getAttribute("value"),
                     roleElement.getAttribute("xmi.id"),
                     taggedValueElement3.getAttribute("value")));
            
            // Adds a new action pin to the list of all action pins
            } else if (type.equals("ActionPin")) {
               if (taggedValueList.getLength() > 4) {
                  allPins.add(new ActionPin (roleElement.getAttribute("name"),
                        taggedValueElement4.getAttribute("value"),
                        roleElement.getAttribute("xmi.id"),
                        taggedValueElement3.getAttribute("value")));
               }
            
            // Prints an error if the UML:ClassifierRole isn't a part, pin, or parameter
            } else {
               System.out.printf ("Invalid UML:ClassifierRole tag at %s%n", 
                     roleElement.getAttribute("name"));
            }
         }
         
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
         
         NodeList transitionList = namespaceElement.getElementsByTagName("UML:Transition");
         
         // Iterates over each UML:Transition tag
         for (int i = 0; i < transitionList.getLength(); i++) {
            
            Node transitionNode = transitionList.item(i);
            Element transitionElement = (Element) transitionNode;
            
            // Gets the source and target names
            NodeList taggedValueList = transitionElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode2 = taggedValueList.item(2);
            Element taggedValueElement2 = (Element) taggedValueNode2;
            Node taggedValueNode3 = taggedValueList.item(3);
            Element taggedValueElement3 = (Element) taggedValueNode3;
            Node taggedValueNode4 = taggedValueList.item(4);
            Element taggedValueElement4 = (Element) taggedValueNode4;
            Node taggedValueNode5 = taggedValueList.item(5);
            Element taggedValueElement5 = (Element) taggedValueNode5;
            
            // Adds a new transition to the list of all transitions
            allTransitions.add(new Transition (transitionElement.getAttribute("xmi.id"),
                  taggedValueElement2.getAttribute("value"),
                  taggedValueElement3.getAttribute("value"),
                  transitionElement.getAttribute("source"),
                  transitionElement.getAttribute("target"),
                  taggedValueElement4.getAttribute("value"),
                  taggedValueElement5.getAttribute("value")));
         }
         
         if (hasActD) {
            NodeList machineList = doc.getElementsByTagName("UML:StateMachine.top");
            Node machineNode = machineList.item(0);
            Element machineElement = (Element) machineNode;
            NodeList actionList = machineElement.getElementsByTagName("UML:ActionState");
            
            // Iterates over each UML:ActionState tag
            for (int i = 0; i < actionList.getLength(); i++) {
               
               Node actionNode = actionList.item(i);
               Element actionElement = (Element) actionNode;
               
               // Gets the type and owner's xmi
               NodeList taggedValueList = actionElement.getElementsByTagName("UML:TaggedValue");
               Node taggedValueNode0 = taggedValueList.item(0);
               Element taggedValueElement0 = (Element) taggedValueNode0;
               String type = taggedValueElement0.getAttribute("value");
               Node taggedValueNode2 = taggedValueList.item(2);
               Element taggedValueElement2 = (Element) taggedValueNode2;
               
               // Adds a new action to the list of all actions
               if (type.equals("Action")) {
                  allActions.add(new Action (actionElement.getAttribute("name"),
                        actionElement.getAttribute("xmi.id"),
                        taggedValueElement2.getAttribute("value"),
                        new ArrayList<ActionPin>(), new ArrayList<ActionPin>()));

               // Prints an error if the UML:ActionState isn't an action
               /*} else {
                  System.out.printf ("Invalid UML:ActionState tag at %s%n", 
                        actionElement.getAttribute("name"));*/
               }
            }
         }
         
         NodeList diagramList = doc.getElementsByTagName("UML:Diagram");
         
         // Iterates over each UML:Diagram and gets the type of diagram IBD/ActD
         for (int i = 0; i < diagramList.getLength(); i++) {
            Node diagramNode = diagramList.item(i);
            Element diagramElement = (Element) diagramNode;
            NodeList taggedValueList = diagramElement.getElementsByTagName("UML:TaggedValue");
            Node taggedValueNode1 = taggedValueList.item(1);
            Element taggedValueElement1 = (Element) taggedValueNode1;
            String type = taggedValueElement1.getAttribute("value");
            
            // Checks to make sure its not including the main BDD
            if (!(diagramElement.getAttribute("name").equals("One Level Block Hierarchy"))) {
               
               // Creates an ArrayList to store all the components of each IBD
               ArrayList<String> allSubjects = new ArrayList<String>();
               
               NodeList subjectList = diagramElement.getElementsByTagName("UML:DiagramElement");

               // Iterates over each UML:DiagramElement per diagram and adds the components to an ArrayList
               for (int j = 0; j < subjectList.getLength(); j++) {
                  Node subjectNode = subjectList.item(j);
                  Element subjectElement = (Element) subjectNode;
                  allSubjects.add(subjectElement.getAttribute("subject"));
               }
               
               // Adds a new ibd to the list of all ibds
               if (type.equals("CompositeStructure")) {
                  allIBDs.add(new IBD (diagramElement.getAttribute("name"), 
                        diagramElement.getAttribute("xmi.id"), allSubjects));
               
               // Adds a new ActD to the list of all ActDs
               } else if (type.equals("Activity")) {
                  Node taggedValueNode3 = taggedValueList.item(3);
                  Element taggedValueElement3 = (Element) taggedValueNode3;
                  allActDs.add(new ActD (diagramElement.getAttribute("name"),
                        taggedValueElement3.getAttribute("value"), allSubjects, 
                        new ArrayList<Action>()));
               
               // Prints an error if the UML:Diagram isn't an IBD or ActD
               } else {
                  System.out.printf ("Invalid UML:Diagram tag at %s%n", 
                        diagramElement.getAttribute("name"));
               }
            }
         }
         
         // Stores each block with all its ports
         for (Port port : allPorts) {
            for (Block block : allBlocks) {
               if (port.xmiOfOwner().equals(block.xmi())) {
                  block.ports().add(port.name());
               }
            }
         }
         
         // Stores each actD with its actions
         for (Action action : allActions) {
            for (ActD act : allActDs) {
               if (action.xmiOfOwner().equals(act.xmi())) {
                  act.actions().add(action);
               }
            }
         }

         // Tests by printing the first element of each list for BDD and IBD components
         /*System.out.println(allBlocks.get(0));
         System.out.println();
         System.out.println(allPorts.get(0));
         System.out.println();
         System.out.println(allAssociations.get(0));
         System.out.println();
         System.out.println(allRoles.get(0));
         System.out.println();
         System.out.println(allFunctions.get(0));
         for (Block block : allBlocks) {
            System.out.printf("%s%n%n", block);
         }*/
         // Tests by printing the first element of each list for the activity diagram components
         /*System.out.println(allActions.get(0));
         System.out.println();
         System.out.println(allParameters.get(0));
         System.out.println();
         System.out.println(allPins.get(0));
         System.out.println();
         System.out.println(allTransitions.get(0));
         System.out.println();
         System.out.println(allActDs.get(0));
         System.out.println();*/
         
/**************************************************************************************************/
         // Begins validating the Incomplete Topology inspection
         
         // Creates ArrayLists to hold the complete list of errors and their types
         ArrayList<IncompleteTopologyI> allTopsI = new ArrayList<IncompleteTopologyI>();
         ArrayList<IncompleteTopologyII> allTopsII = new ArrayList<IncompleteTopologyII>();
         ArrayList<BalanceLawsI> allIntegritiesI = new ArrayList<BalanceLawsI>();
         ArrayList<BalanceLawsII> allIntegritiesII = new ArrayList<BalanceLawsII>();
         ArrayList<DanglingNode> allNodes = new ArrayList<DanglingNode>();
         ArrayList<UnknownFunction> allFunctions = new ArrayList<UnknownFunction>();
         ArrayList<FunctionalKB> allKBs = new ArrayList<FunctionalKB>();
         
         // Validates that each block has at least one port
         for (Block block : allBlocks) {
            if (block.ports.size() == 0) {
               allTopsI.add(new IncompleteTopologyI (block.name(), block.xmi()));
            }
         }
         
         // Creates two HashMaps to separately define the sources and destinations of each association
         HashMap<String, String> aSources = new HashMap<String, String>();
         HashMap<String, String> aDestinations = new HashMap<String, String>();
         
         // Fills the maps with unique associations but not necessarily unique ports 
         for (Association association : allAssociations) {
           aSources.put(association.xmi(), association.xmiOfSource());
           aDestinations.put(association.xmi(), association.xmiOfDestination());
         }
   
         // Validates that each port within an IBD is either the source or destination of an association
         for (Port port : allPorts) {
            if (!(port.reusesProperty().equals(""))) {
               if (!(aSources.containsValue(port.xmi()) || aDestinations.containsValue(port.xmi()))) {
                  String ownerName = getPortOwner(port.xmi()).name();
                  allTopsII.add(new IncompleteTopologyII (port.name(), "Port", ownerName, "Property",
                        getIBD(allIBDs, port.xmi()), "IBD", port.xmi()));
               }     
            }
         }
         
         ArrayList<String> tSources = new ArrayList<String>();
         ArrayList<String> tTargets = new ArrayList<String>();
         
         for (Transition transition : allTransitions) {
            tSources.add(transition.xmiOfSource());
            tTargets.add(transition.xmiOfTarget());
         }
         
         for (ActionPin pin : allPins) {
            if (tSources.contains(pin.xmi())) {
               sourcePins.add(pin);
            } else if (tTargets.contains(pin.xmi())) {
               targetPins.add(pin);
            } else {
               String ownerName = getPinOwner(pin.xmi()).name();
               // Finds the name of the owner's action
               allTopsII.add(new IncompleteTopologyII (pin.name(), "ActionPin", ownerName, "Action",
                     "", "ActD", pin.xmi()));
            }
         }
         
         for (ActivityParameter par : allParameters) {
            if (tSources.contains(par.xmi())) {
               //valid
            } else if (tTargets.contains(par.xmi())) {
               //valid
            } else {
               String ownerName = "";
               // Finds the name of the owner's action
               allTopsII.add(new IncompleteTopologyII ("ActD", "", "ActD", ownerName,
                     "ActivityParameter", par.name(), par.xmi()));
            }
         }

         
         // Begins validating the old balance laws inspection
         
         // Creates ArrayLists to separately store source and destination ports
         /*ArrayList<Port> allDPorts = new ArrayList<Port>();
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

         }*/
         
         // Checks for dangling edges
         /*for (Association asso : allAssociations) {
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
         }*/
         
         
/**************************************************************************************************/         
         // Begins validating the balance laws inspection
         
         // Iterates over each association to ensure both ends match types
         for (Association asso : allAssociations) {

            // Gets the types of the source and destination ports for each association
            String sType = getType(asso.sourceName());
            String dType = getType(asso.destinationName());

            // Validates that both ends of an association have the same type
            if (!(sType.equals(dType))) {

               // Gets the name of the current IBD and the name of the port's owner
               String currentIBD = getIBD(allIBDs, asso.xmi());
               String sPortOwner = getPortOwner(asso.xmiOfSource()).name();
               String dPortOwner = getPortOwner(asso.xmiOfDestination()).name();
               
               // If the port owner's name is blank then it's owner is the same as the current IBD
               if (sPortOwner.equals("")) {
                  sPortOwner = currentIBD;
               }
               if (dPortOwner.equals("")) {
                  dPortOwner = currentIBD;
               }
               
               // If the types don't match then a new Type I FLow Integrity error is added to the list
               allIntegritiesI.add(new BalanceLawsI (currentIBD,
                     asso.name(), asso.xmi(), sPortOwner,
                     asso.sourceName(), asso.xmiOfSource(), dPortOwner,
                     asso.destinationName(), asso.xmiOfDestination()));
            }
         }
         
         // Iterates over each block to check that the input and output ports are balanced
         for (Block block : allBlocks) {
            
            // Creates counts for the number of types of input and output ports on each block
            int numInM = 0; int numInE = 0; int numOutM = 0; int numOutE = 0;
            
            // Creates new ArrayLists for separating storing the input and output ports of each block
            ArrayList<String> inputPorts = new ArrayList<String>();
            ArrayList<String> outputPorts = new ArrayList<String>();
            
            // Fills the ArrayLists with input and output ports
            for (String port : block.ports()) {
               port = port.toUpperCase();
               if (port.contains("IN")) {
                  inputPorts.add(port);
               }
               if (port.contains("OUT")) {
                  outputPorts.add(port);
               }
            }
            
            // Counts the number of materials and energies inputted
            for (String inPort : inputPorts) {
               String portType = getType(inPort);
               if (portType.equals("material")) {
                  numInM++;
               } else if (portType.equals("energy")) {
                  numInE++;
               }
            }
            
            // Counts the number of materials and energies outputted
            for (String outPort : outputPorts) {
               String portType = getType(outPort);
               if (portType.equals("material")) {
                  numOutM++;
               } else if (portType.equals("energy")) {
                  numOutE++;
               }
            }
            
            if (!(numInM == numOutM)) {
               allIntegritiesII.add(new BalanceLawsII (block.name(), 
                     "materials", numInM, numOutM));
            } else if (!(numInE == numOutE)) {
               allIntegritiesII.add(new BalanceLawsII (block.name(), 
                     "energies", numInE, numOutE));
            } else {
               //valid 
            }
         }
         
         
/**************************************************************************************************/
         // Begins validating Inferred Balance Laws and Dangling Node Inspection
         
         // Stores each action with its input action pins
         for (ActionPin pin : targetPins) {
            for (Action action : allActions) {
               if (pin.xmiOfOwner().equals(action.xmi())) {
                  action.inPins().add(pin);
               }
            }
         }
         
         // Stores each action with its output action pins
         for (ActionPin pin : sourcePins) {
            for (Action action : allActions) {
               if (pin.xmiOfOwner().equals(action.xmi())) {
                  action.outPins().add(pin);
               }
            }
         }
         
         // Iterates over each ActD
         for (ActD act : allActDs) {
            
            // If the actD has no parameters, raise a DanglingNode error
            if (!hasParameter(act)) {
               allNodes.add(new DanglingNode (act.name(), act.xmi()));
            }
            
            // Iterates over each action to check for inferred balance errors
            for (Action action : act.actions()) {
               if(!isAction(action)) {
                  allFunctions.add(new UnknownFunction (action.name(),
                        act.name(), action.xmi(), act.xmi()));
               } else if(!validAction(action)) {
                  allKBs.add(new FunctionalKB (action.name(),
                        act.name(), action.xmi(), act.xmi()));
               }
            }
         }
         
 
/**************************************************************************************************/
         // Calculates the total number of errors
         int totalErrors = allTopsI.size() + allTopsII.size() + allIntegritiesI.size() + 
               allNodes.size() + allFunctions.size() + allKBs.size();
         
         // Prints the type of each error caught and its location         
         for (BalanceLawsI error : allIntegritiesI) {
            System.out.printf ("%s%n%n", error);
         }
         if (!hasActD) {
            for (BalanceLawsII error : allIntegritiesII) {
               System.out.printf ("%s%n%n", error);
            }
            totalErrors += allIntegritiesII.size();
         }
         for (IncompleteTopologyI error : allTopsI) {
            System.out.printf ("%s%n%n", error);
         }
         for (IncompleteTopologyII error : allTopsII) {
            System.out.printf ("%s%n%n", error);
         }
         for (DanglingNode error : allNodes) {
            System.out.printf ("%s%n%n", error);
         }
         for (UnknownFunction error : allFunctions) {
            System.out.printf ("%s%n%n", error);
         }
         for (FunctionalKB error : allKBs) {
            System.out.printf ("%s%n%n", error);
         }
         System.out.printf ("Total errors: %d%n", totalErrors);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   
   // Determines if the given action is in the function knowledge base at all
   public static boolean isAction (final Action action) {
      
      String[] functions = {"IMPORT", "EXPORT", "STORE", "STOP", "SEPARATE", 
            "DISTRIBUTE", "TRANSFER", "GUIDE", "COUPLE", "MIX", "ENERGIZE", 
            "DEENERGIZE", "REGULATE", "CHANGE", "CONVERT", "SUPPLY", "ACTUATE",
            "DISPOSE"};
      
      String actionName = action.name().toUpperCase();
      for (String function : functions) {
         if (function.equals(actionName)) {
            return true;
         }
      }
      return false;
   }
   
   // Determines if the given action follows the function knowledge base
   public static boolean validAction(final Action action) {
     
     String function = action.name().toUpperCase();
     int numInputs = action.inPins().size(); 
     int numOutputs = action.outPins().size();
     
      if (function.equals("IMPORT") || function.equals("EXPORT")) {
         if (numInputs == 1 && numOutputs == 1) {
            if (action.inPins().get(0).type.equals(action.outPins().get(0).type)) {
               return true;
            }
         }
      
      } else if (function.equals("STORE") || function.equals("STOP")) {
         if (numInputs == 1 && numOutputs == 0) {
            if (getGeneralType(action.inPins().get(0)).equals("material")) {
               return true;
            }
         }
         
      } else if (function.equals("SEPARATE")) {
         if (numInputs == 1 && numOutputs > 1) {
            for (ActionPin outPin : action.outPins()) {
               if (!getGeneralType(outPin).equals("material")) {
                  return false;
               }
            }
            if (action.inPins().get(0).type().equals("M")) {
               return true;
            }
         }
         
         
      } else if (function.equals("DISTRIBUTE")) {
         if (numInputs == 1 && numOutputs == 2) {
            String typeIn1 = action.inPins().get(0).type();
            String typeOut1 = action.outPins().get(0).type();
            String typeOut2 = action.outPins().get(1).type();
            if (typeIn1.equals(typeOut1) && typeIn1.equals(typeOut2)) {
               return true;
            }
         }
         
      } else if (function.equals("TRANSFER") || function.equals("ACTUATE")) {
         if (numInputs == 1 && numOutputs == 1) {
            String typeIn1 = action.inPins().get(0).type();
            String typeOut1 = action.outPins().get(0).type();
            if (typeIn1.equals(typeOut1)) {
               if (getGeneralType(action.inPins().get(0)).equals("energy")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("GUIDE") || function.equals("REGULATE") || 
            function.equals("SUPPLY")) {
         if (numInputs == 1 && numOutputs == 1) {
            String typeIn1 = action.inPins().get(0).type();
            String typeOut1 = action.outPins().get(0).type();
            if (typeIn1.equals(typeOut1)) {
               if (getGeneralType(action.inPins().get(0)).equals("material")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("COUPLE")) {
         if (numInputs == 2 && numOutputs == 1) {
            String typeIn1 = action.inPins().get(0).type();
            String typeIn2 = action.inPins().get(1).type();
            String typeOut1 = action.outPins().get(0).type();
            if (typeIn1.equals(typeOut1) && typeIn2.equals(typeOut1)) {
               if (getGeneralType(action.inPins().get(0)).equals("material")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("MIX")) {
         if (numInputs > 1 && numOutputs == 1) {
            for (ActionPin inPin : action.inPins()) {
               if (!getGeneralType(inPin).equals("material")) {
                  return false;
               }
            }
            if (action.outPins().get(0).type().equals("M")) {
               return true;
            }
         }
         
      } else if (function.equals("ENERGIZE")) {
         if (numInputs == 2 && numOutputs == 1) {
            String typeIn1 = getGeneralType(action.inPins().get(0));
            String typeIn2 = getGeneralType(action.inPins().get(1));
            String typeOut1 = getGeneralType(action.outPins().get(0));
            if (!typeIn1.equals(typeIn2)) {
               if (typeOut1.equals("material")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("DEENERGIZE")) {
         if (numInputs == 1 && numOutputs == 2) {
            String typeIn1 = getGeneralType(action.inPins().get(0));
            String typeOut1 = getGeneralType(action.outPins().get(0));
            String typeOut2 = getGeneralType(action.outPins().get(1));
            if (!typeOut1.equals(typeOut2)) {
               if (typeIn1.equals("material")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("CHANGE")) {
         if (numInputs == 1 && numOutputs == 1) {
            String typeIn1 = getGeneralType(action.inPins().get(0));
            String typeOut1 = getGeneralType(action.outPins().get(0));
            if (typeIn1.equals(typeOut1)) {
               if (typeIn1.equals("material")) {
                  return true;
               }
            }
         }
         
      } else if (function.equals("CONVERT")) {
         if (numInputs == 1 && numOutputs == 1) {
            String typeGIn1 = getGeneralType(action.inPins().get(0));
            String typeGOut1 = getGeneralType(action.outPins().get(0));
            String typeSIn1 = action.inPins().get(0).type();
            String typeSOut1 = action.outPins().get(0).type();
            if (typeGIn1.equals("energy") && typeGOut1.equals("energy")) {
               if (!typeSIn1.equals(typeSOut1)) {
                  return true;
               }
            }
         }
      }
      
      return false;
   }
   
   // Determines if the actD has at least one parameter
   public static boolean hasParameter(final ActD act) {
      for (String xmiOfComponent : act.subjects()) {
         for (ActivityParameter par : allParameters) {
            if (xmiOfComponent.equals(par.xmi())) {
               return true;
            }
         }
      }
      return false;
   }
   
   public static String getGeneralType (final ActionPin pin) {
      
      // Defines separate lists to distinguish between types
      String[] materials = {"M", "S", "L", "G"};
      String[] energies = {"EE", "ME", "THE", "CHE", "EME", "NUE"};
      
      // Gets the type of the pin
      String pinType = pin.type().toUpperCase();
      
      // Determines if the type is a material
      for (int i = 0; i < materials.length; i++) {
         if (materials[i].equals(pinType)) {
            return "material";
         }
      }
   
      // Determines if the type is an energy
      for (int i = 0; i < energies.length; i++) {
         if (energies[i].equals(pinType)) {
            return "energy";
         }
      }
      
      // If the type isn't labeled as a material or energy, return an empty string
      return "";
   }
   
   
   // Determines the type of a port from its name
   public static String getType (final String portName) {
      
      // Defines separate lists to distinguish between types
      String[] materials = {"M", "S", "LIQ", "GAS"};
      String[] energies = {"E", "EE", "ME", "THE", "CHE", "EME", "NUE"};
      
      // Manipulates the Strings to represent the port's exact type
      String formattedPortName = removeDigits(portName.toUpperCase());
      String[] portNameSplit = formattedPortName.split("_");
      String exactType = portNameSplit[1];
      
      // Determines if the type is a material
      for (int i = 0; i < materials.length; i++) {
         if (materials[i].equals(exactType)) {
            return "material";
         }
      }
   
      // Determines if the type is an energy
      for (int i = 0; i < energies.length; i++) {
         if (energies[i].equals(exactType)) {
            return "energy";
         }
      }
      
      // If the type isn't labeled as a material or energy, return an empty string
      return "";
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
   public static ClassifierRole getPortOwner (final String xmiOfPort) {
      String owner = "";
      for (Port port : allPorts) {
         if (port.xmi().equals(xmiOfPort)) {
            owner = port.xmiOfOwner();
            for (ClassifierRole role : allRoles) {
               if (role.xmi().equals(owner)) {
                  return role;
               }
            }
         }
      }
      return new ClassifierRole ("","","","","","");
   }
   
   
   // Finds and return the owner of an action pin (assuming the owner is an action)
   public static Action getPinOwner (final String xmiOfPin) {
      String owner = "";
      for (ActionPin pin : allPins) {
         if (pin.xmi().equals(xmiOfPin)) {
            owner = pin.xmiOfOwner();
            for (Action action : allActions) {
               if (action.xmi().equals(owner)) {
                  return action;
               }
            }
         }
      }
      return new Action ("","","", null, null);
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
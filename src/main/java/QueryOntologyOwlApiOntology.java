

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class QueryOntologyOwlApiOntology {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private OpenlletReasoner reasoner;
    private OWLClass cls;
    private NodeSet<OWLClass> superClasses;

    QueryOntologyOwlApiOntology(String ont) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        //   manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.co-ode.org/ontologies/pizza/2.0.0"),
        //        IRI.create("C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\ee.owl")));

        ontology = manager.loadOntologyFromOntologyDocument(new File(getRelativeResourcePath(ont)));
        //  System.out.println(getRelativeResourcePath(ont));
        //OWLOntology ontology2 = manager.loadOntologyFromOntologyDocument(new File(getRelativeResourcePath("../../data/pizza.owl")));

//System.out.println(ontology2.getOntologyID().getOntologyIRI().get());

        //  OWLImportsDeclaration im = factory.getOWLImportsDeclaration(IRI.create("file:C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\pizza.owl"));
        //   manager.applyChange(new AddImport(ontology,im));
//
//        File file = File.createTempFile("owlapiexamples", "saving.owl");
//        System.out.println(file);
//        manager.saveOntology(ontology, IRI.create(file.toURI()));

        // manager.saveOntology(ontology,new FileOutputStream(new File("C:\\Users\\User\\Desktop\\iranopendatav2\\src\\data\\qq.owl")));
        reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);

        //*********************
        Set<OWLClass> allCls = ontology.getClassesInSignature(true);
        allCls.forEach(System.out::println);

        //*********************

    }

    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        QueryOntologyOwlApiOntology app = new QueryOntologyOwlApiOntology("data/family_v2.owl");
        //   app.queryByClassName("test");

    }

    public static String getRelativeResourcePath(String resource) throws FileNotFoundException {
        if (resource == null || resource.equals("")) throw new IllegalArgumentException(resource);

        URL url = QueryOntologyOwlApiOntology.class.getClassLoader().getResource(resource);

        if (url == null) throw new FileNotFoundException(resource);

        return url.getPath();
    }

    public String queryByClassName(String clsName) {
        Map<String, ArrayList> clsAndPropAndPropValue = new HashMap<>();


        //this.cls = findClass(clsName);

        // this.cls = factory.getOWLClass("http://www.semanticweb.org/user/ontologies/2019/3/untitled-ontology-45#test");
        //  System.out.println("++++++++"+cls.toStringID()+"++++++++");
        // System.out.println("++++++++"+cls.toString()+"++++++++");

        for (OWLClass oc : findClass(clsName)) {
            superClasses = getSuperClassesFromReasoner(oc.toStringID());
            ArrayList<Map> cp = new ArrayList<>();
            //  clsAndPropAndPropValue.put(cls.toStringID(),PrintClassAxioms(cls));
            //  Map sp = new HashMap<String,NodeSet<OWLClass>>();
            ArrayList<String> superclassesArrayList = new ArrayList<String>();
            // sp.put("superclasses",superClasses);
            // cp.add(sp);
            //now print cls super classes axioms
            for (Node<OWLClass> m : superClasses) {

                // System.out.println("class::::::::: " + m);
                OWLClass pCls = m.getRepresentativeElement();
                superclassesArrayList.add(pCls.toString());
                Map<String, Collection<ArrayList<String>>> classAxiom = PrintClassAxioms(pCls).asMap();

                if (classAxiom.size() > 0) cp.add(classAxiom);
            }

            Map<String, ArrayList> spMap = new HashMap<>();
            spMap.put("superclasses", superclassesArrayList);
            cp.add(spMap);


            cp.add((PrintClassAxioms(oc)).asMap());

            //  cp.forEach(x -> System.out.println("$$$$$" + x));
            clsAndPropAndPropValue.put(oc.toStringID(), cp);
            //  clsAndPropAndPropValue.forEach((x, y) -> System.out.println("####" + x + "****" + y + "***"));
        }


        String gson = new Gson().toJson(clsAndPropAndPropValue);

        System.out.println(gson);
        return gson;
    }

    private ArrayList<OWLClass> findClass(String cls) {
        ArrayList<OWLClass> findClasses = new ArrayList<OWLClass>();
        Set<OWLClass> allClasses = ontology.getClassesInSignature();
        Iterator<OWLClass> it;

        if (!allClasses.isEmpty()) {
            it = allClasses.iterator();

            while (it.hasNext()) {
                //   System.out.println(it.next().getIRI().getFragment());

                OWLClass nc = it.next();
                if (nc.getIRI().getFragment().toLowerCase().contains(cls)) {
                    //  clsNs =  nc;
                    findClasses.add(nc);
                }
            }
        }
        return findClasses;
    }

    private NodeSet<OWLClass> getSuperClassesFromReasoner(String clsNS) {
        OWLClass oc = factory.getOWLClass(clsNS);
        NodeSet<OWLClass> reasonerSuperClasses = reasoner.getSuperClasses(oc);
        return reasonerSuperClasses;
    }

    //Return property with its values
    private Multimap<String, ArrayList<String>> PrintClassAxioms(OWLClass cls) {
        //Map classPropertiesMapList = new HashMap<String, ArrayList<String>>();
        Multimap<String, ArrayList<String>> classPropertiesMapList = ArrayListMultimap.create();

        for (OWLAxiom axiom : ontology.axioms(cls).collect(Collectors.toSet())) {


            // create an object visitor to get to the subClass restrictions
            axiom.accept(new OWLObjectVisitor() {

                // found the subClassOf axiom
                public void visit(OWLSubClassOfAxiom subClassAxiom) {

                    // create an object visitor to read the underlying (subClassOf) restrictions

                    subClassAxiom.getSuperClass().accept(new OWLObjectVisitor() {

                        public void visit(OWLObjectSomeValuesFrom someValuesFromAxiom) {
                            printObjectQuantifiedRestriction(cls, classPropertiesMapList, someValuesFromAxiom);
                        }

                        public void visit(OWLObjectExactCardinality exactCardinalityAxiom) {
                            printObjectCardinalityRestriction(cls, classPropertiesMapList, exactCardinalityAxiom);
                        }

                        public void visit(OWLObjectMinCardinality minCardinalityAxiom) {
                            printObjectCardinalityRestriction(cls, classPropertiesMapList, minCardinalityAxiom);
                        }

                        public void visit(OWLObjectMaxCardinality maxCardinalityAxiom) {
                            printObjectCardinalityRestriction(cls, classPropertiesMapList, maxCardinalityAxiom);
                        }

                        public void visit(OWLDataMinCardinality owlDataMinCardinality) {

                            printDataCardinalityRestriction(cls, classPropertiesMapList, owlDataMinCardinality);
                        }

                        public void visit(OWLDataMaxCardinality owlDataMaxCardinality) {

                            printDataCardinalityRestriction(cls, classPropertiesMapList, owlDataMaxCardinality);
                        }

                        public void visit(OWLDataSomeValuesFrom owlDataSomeValuesFrom) {

                            printDataQuantifiedRestriction(cls, classPropertiesMapList, owlDataSomeValuesFrom);
                        }

                        public void visit(OWLDataAllValuesFrom owlDataAllValuesFrom) {

                            printDataQuantifiedRestriction(cls, classPropertiesMapList, owlDataAllValuesFrom);
                        }

                    });
                }
            });

        }
        //   classPropertiesMapList.forEach((x,y)->System.out.println("0000"+x));
        return classPropertiesMapList;
    }

    public void printObjectQuantifiedRestriction(OWLClass oc, Multimap<String, ArrayList<String>> classPropertiesMapList, OWLQuantifiedObjectRestriction restriction) {
        //	System.out.println( "\t\tClass: " + oc.toString() );

        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());

        ArrayList<String> prpValues = new ArrayList<>();
        Set<OWLClassAssertionAxiom> instances = ontology.getClassAssertionAxioms(restriction.getFiller());
        if (!instances.isEmpty()) {
            for (OWLClassAssertionAxiom ax : instances) {
                System.out.println(ax.getIndividual());
                prpValues.add(ax.getIndividual().toString());
            }

        } else {
            prpValues.add(restriction.getFiller().toString());
        }
        //  return prpValues;

        classPropertiesMapList.put(restriction.getProperty().toString(), prpValues);

        //  System.out.println("object prop fom:"+classPropertiesMapList.get(restriction.getProperty().toString()));

//    	OWLClassExpression owlClassExpression  = restriction.getFiller();
//        System.out.println(owlClassExpression.getClassExpressionType());
//    	restriction.getFiller().accept(new OWLClassExpressionVisitor() {
//			//@Override
//			public void visit(OWLClassExpression ce) {
//
//                System.out.println( "++++Class: " + ce.toString() );
//			}
//		});
        //  System.out.println();
    }

    public void printObjectCardinalityRestriction(OWLClass oc, Multimap<String, ArrayList<String>> classPropertiesMapList, OWLObjectCardinalityRestriction restriction) {
        //	System.out.println( "\t\tClass: " + oc.toString() );
        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tCardinality: " + restriction.getCardinality());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());
        System.out.println();
        ArrayList<String> prpValues = new ArrayList<>();
        Set<OWLClassAssertionAxiom> instances = ontology.getClassAssertionAxioms(restriction.getFiller());
        if (!instances.isEmpty()) {
            for (OWLClassAssertionAxiom ax : instances) {
                System.out.println(ax.getIndividual());
                prpValues.add(ax.getIndividual().toString());
            }

        } else {
            prpValues.add(restriction.getFiller().toString());
        }
        // return prpValues;
        classPropertiesMapList.put(restriction.getProperty().toString(), prpValues);
    }

    public void printDataQuantifiedRestriction(OWLClass oc, Multimap<String, ArrayList<String>> classPropertiesMapList, OWLQuantifiedDataRestriction restriction) {
        //	System.out.println( "\t\tClass: " + oc.toString() );
        ArrayList<String> prpValues = new ArrayList<>();

        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());
        System.out.println();
        prpValues.add(restriction.getFiller().toString());
        // return prpValues;
        classPropertiesMapList.put(restriction.getProperty().toString(), prpValues);

    }

    public void printDataCardinalityRestriction(OWLClass oc, Multimap<String, ArrayList<String>> classPropertiesMapList, OWLDataCardinalityRestriction restriction) {
        //	System.out.println( "\t\tClass: " + oc.toString() );
        ArrayList<String> prpValues = new ArrayList<>();

        System.out.println("\t\tClassExpressionType: " + restriction.getClassExpressionType().toString());
        System.out.println("\t\tCardinality: " + restriction.getCardinality());
        System.out.println("\t\tProperty: " + restriction.getProperty().toString());
        System.out.println("\t\tObject: " + restriction.getFiller().toString());
        System.out.println();
        prpValues.add(restriction.getFiller().toString());
        // return prpValues;
        classPropertiesMapList.put(restriction.getProperty().toString(), prpValues);
    }
}

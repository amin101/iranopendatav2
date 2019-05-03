import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class QueryOntology {

    OntModel vocabulary;

    public QueryOntology(String vocabulary) {
        this.vocabulary = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        this.vocabulary.read(vocabulary);
        // return ontModel;
    }


    public ArrayList<String> queryByClassName(String pattern) {
        ArrayList<String> result = new ArrayList<>();
        for (Iterator<OntClass> c = vocabulary.listNamedClasses(); c.hasNext(); ) {

            OntClass cl = c.next();
            //remove NS
            if (cl.getLocalName().toLowerCase().contains(pattern.toLowerCase())) {
                //return full qualified class name
                System.out.println(cl.toString());
                result.add(cl.toString());
            }
        }
        return result;
    }

    public ArrayList queryByPropertyName(String pattern) {
        ArrayList<String> result = new ArrayList<>();
        for (Iterator<OntProperty> c = vocabulary.listAllOntProperties(); c.hasNext(); ) {
            OntProperty property = c.next();

            if (property.getLocalName().toLowerCase().contains(pattern)) {
                result.add(property.toString());
            }
        }
        return result;
    }

    public ArrayList queryByIndividualName(String pattern) {
        ArrayList<String> result = new ArrayList<>();
        for (Iterator<Individual> c = vocabulary.listIndividuals(); c.hasNext(); ) {
            Individual individual = c.next();

            if (individual.getLocalName().toLowerCase().contains(pattern)) {
                result.add(individual.toString());
            }
        }
        return result;
    }

    public ArrayList getClassIndividuals(OntClass ontClass) {
        ArrayList<String> result = new ArrayList<>();
        for (Iterator ind = ontClass.listInstances(); ind.hasNext(); ) {
            Object ins = ind.next();
            result.add(ins.toString());
        }
        return result;
    }

    public ArrayList getIndividualProperties(Individual individual, boolean withNameSpace) {
        ArrayList<String> result = new ArrayList<>();
        for (Iterator prop = individual.listProperties(); prop.hasNext(); ) {
            Object ins = prop.next();
            Property propertyObject = vocabulary.getProperty(ins.toString());
//            withNameSpace? result.add(ins.toString()):result.add(propertyObject.getLocalName());

            if (withNameSpace) {
                result.add(propertyObject.toString());
            } else {
                result.add(propertyObject.getLocalName());
            }

        }
        return result;
    }

    public OntClass getClassByName(String ns) {
        OntClass ontClass = vocabulary.getOntClass(ns);
        return ontClass;
    }


    public Individual getIndividualByName(String ns) {
        Individual individual = vocabulary.getIndividual(ns);
        return individual;
    }

    public Map<String, String> getClassProperties(OntClass ontClass) {
        Map<String, String> result = new HashMap<>();
        OntProperty property;

        ExtendedIterator<OntProperty> properties = ontClass.listDeclaredProperties();
        while (properties.hasNext()) {
            property = properties.next();
            result.put(property.toString(), property.getRange().getLocalName());
        }
        return result;
    }

    public Map<String, ArrayList<String>> getClassProperties(String className) {

        Map<String, ArrayList<String>> result = new HashMap<>();
        OntProperty p;
        OntClass ontClass = vocabulary.getOntClass(className);
        ExtendedIterator<OntProperty> properties = ontClass.listDeclaredProperties();

        while (properties.hasNext()) {
            ArrayList<String> DataValueList = new ArrayList<>();
            //Add comment as the first item in array

            p = properties.next();
            //  System.out.println("88888"+p.toString());
            // result is comprise of Dataproperty and ObjectProperty
            // we parse each one in a separate IF clause
            if (p.isDatatypeProperty()) {
                dataPropertyRangeToList(p, DataValueList);
            } else if (p.isObjectProperty()) {
                objectPropertyToRange(p, DataValueList);
            }
            result.put(p.toString(), DataValueList);
        }
        ArrayList<String> comment = new ArrayList<String>();
        comment.add(ontClass.getComment(""));
        result.put("comment", comment);
        return result;
    }

    private void objectPropertyToRange(OntProperty p, ArrayList<String> dataValueList) {
        ExtendedIterator<? extends OntResource> listRange = p.listRange();
        OntResource child;
        if (listRange.hasNext()) {
            // as resonear return redundant classes as range(like Thing and Resource)
            // I'm trying to get the desired class
            child = listRange.next();
            while (listRange.hasNext()) {
                OntResource testClass = listRange.next();
                if (child.asClass().hasSubClass(testClass)) {
                    child = testClass;
                }
            }
            // Having the desired class, its time to get access to its individuals
            ExtendedIterator<? extends OntResource> listIndividuals = child.asClass().listInstances();
            while (listIndividuals.hasNext()) {
                dataValueList.add(listIndividuals.next().toString());
            }
            // Add property IRI and its values to result Map list

            // DataValueList.clear();
        }
    }

    private void dataPropertyRangeToList(OntProperty p, ArrayList<String> dataValueList) {
        OntResource range = p.getRange();
        // some Ranges are comprise of list values
        if (range != null && range.asClass().isEnumeratedClass()) {
            RDFList pr = range.asClass().asEnumeratedClass().getOneOf();
            Iterator it = pr.iterator();

            while (it.hasNext()) {
                dataValueList.add(it.next().toString());
            }
        } else {// here we have only a data type
            dataValueList.add(range != null ? range.toString() : "");
        }
    }

}

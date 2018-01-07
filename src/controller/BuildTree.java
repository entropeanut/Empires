package controller;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.BuildingType;
import server.EmpireServer;

@XmlRootElement(name="buildTree")
public class BuildTree{

    private static final Logger log = LoggerFactory.getLogger(EmpireServer.class);

    private static BuildTree instance;

    @XmlElement(name="building")
    private List<BuildingType> buildings;

    @Override
    public String toString() {
        return "BuildTree[" + buildings + "]";
    }

    public BuildingType get(String buildingName) {
        for (BuildingType type : buildings) {
            if (type.getName().equalsIgnoreCase(buildingName)) {
                return type;
            }
        }
        return null;
    }

    public static BuildTree getInstance() {
        if (instance == null) {
            try {
                JAXBContext context = JAXBContext.newInstance(new Class[]{
    //        JAXBContext context = JAXBContextFactory.createContext(new Class[]{
                    BuildTree.class}, Collections.emptyMap());
                Unmarshaller unmarshaller = context.createUnmarshaller();
                unmarshaller.setEventHandler(new ValidationEventHandler() {
                    public @Override boolean handleEvent(ValidationEvent event) {
                        log.warn(event.getMessage());
                        return true;
                    }
                });
                
                instance = (BuildTree) unmarshaller.unmarshal(new File("BuildTree.xml"));
                log.info("Loaded " + instance.toString());
            } catch (JAXBException e) {
                log.error("FATAL", e);
                throw new Error(e);
            }
        }
        return instance;
    }
    
}

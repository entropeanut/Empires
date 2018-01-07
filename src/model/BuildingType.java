package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="building")
public class BuildingType {

    @XmlAttribute(name="name")
    private String name;

    @XmlElement(name="buildTime")
    private int buildTime;
    
    @XmlElement(name="workers")
    private int workers;
    
    @Override
    public String toString() {
        return "Building[" + name + "]";
    }

    public String getName() {
        return name;
    }

    public int getBuildTime() {
        return buildTime;
    }

    public int getWorkers() {
        return workers;
    }
}

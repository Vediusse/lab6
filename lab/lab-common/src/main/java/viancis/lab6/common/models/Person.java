package viancis.lab6.common.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    @XmlElement(required = true)
    private String frontManName;

    @XmlElement(required = true)
    private Integer frontManHeight;

    @XmlElement(required = true)
    private Color eyeColor;

    @XmlElement(required = true)
    private Color hairColor;

    @XmlElement(required = true)
    private Country nationality;

    public Person() {
    }

    public Person(String frontManName, Integer frontManHeight, Color eyeColor, Color hairColor, Country nationality) {
        setName(frontManName);
        setHeight(frontManHeight);
        setEyeColor(eyeColor);
        setHairColor(hairColor);
        setNationality(nationality);
    }

    public String getName() {
        return frontManName;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.frontManName = name;
    }

    public Integer getHeight() {
        return frontManHeight;
    }

    public void setHeight(Integer height) {
        if (height != null && height <= 0) {
            throw new IllegalArgumentException("Height should be greater than 0");
        }
        this.frontManHeight = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Nationality cannot be null");
        }
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return String.format("""
                        
                        \t\t\tname='%s',
                        \t\t\teyeColor='%s',
                        \t\t\thairColor='%s',
                        \t\t\tnationality='%s'
                        \t\t""",
                getName(), getEyeColor(), getHairColor(), getNationality());
    }


}


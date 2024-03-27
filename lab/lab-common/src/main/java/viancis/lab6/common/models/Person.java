package viancis.lab6.common.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Person")
public class Person implements Serializable {

    private final StringProperty frontManName = new SimpleStringProperty();
    private final StringProperty eyeColor = new SimpleStringProperty();
    private final StringProperty hairColor = new SimpleStringProperty();
    private final StringProperty nationality = new SimpleStringProperty();

    public Person() {
    }

    public Person(String frontManName, Integer frontManHeight, Color eyeColor, Color hairColor, Country nationality) {
        this.frontManName.set(frontManName);
        this.eyeColor.set(String.valueOf(eyeColor));
        this.hairColor.set(String.valueOf(hairColor));
        this.nationality.set(String.valueOf(nationality));
    }

    public StringProperty frontManNameProperty() {
        return frontManName;
    }

    public String getFrontManName() {
        return frontManName.get();
    }

    public void setFrontManName(String frontManName) {
        if (frontManName == null || frontManName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.frontManName.set(frontManName);
    }

    public StringProperty eyeColorProperty() {
        return eyeColor;
    }

    public String getEyeColor() {
        return eyeColor.get();
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor.set(eyeColor);
    }

    public StringProperty hairColorProperty() {
        return hairColor;
    }

    public String getHairColor() {
        return hairColor.get();
    }

    public void setHairColor(String hairColor) {
        this.hairColor.set(hairColor);
    }

    public StringProperty nationalityProperty() {
        return nationality;
    }

    public String getNationality() {
        return nationality.get();
    }

    public void setNationality(String nationality) {
        if (nationality == null || nationality.isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be null or empty");
        }
        this.nationality.set(nationality);
    }

    @Override
    public String toString() {
        return String.format("""
                        
                        \t\t\tname='%s',
                        \t\t\teyeColor='%s',
                        \t\t\thairColor='%s',
                        \t\t\tnationality='%s'
                        \t\t""",
                getFrontManName(), getEyeColor(), getHairColor(), getNationality());
    }


}


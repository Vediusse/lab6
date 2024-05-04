package viancis.lab6.common.models;

import javax.validation.constraints.*;
import java.io.*;


public class Person implements Serializable {

    public long id=0;

    @Serial
    private static final long serialVersionUID = 2L;
    private static final int MAX_NAME_SIZE = 255;


    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
    @NotNull
    @NotEmpty
    @Size(min = 2, max = MAX_NAME_SIZE)
    private String frontManName;
    @Min(0)
    private Integer frontManHeight;

    private Color eyeColor;

    private Color hairColor;

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

    public String getEyeColor() {
        return eyeColor.toString();
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor.toString();
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public String getNationality() {
        return nationality.toString();
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}


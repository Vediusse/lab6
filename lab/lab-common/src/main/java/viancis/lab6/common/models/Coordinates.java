package viancis.lab6.common.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {

    @XmlElement(required = true)
    private Double x;
    @XmlElement(required = true)
    private Long y;

    public Coordinates() {
    }

    public Coordinates(Double x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }


    public void setX(Double x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }


    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("x=%s, y=%s", x, y);
    }
}

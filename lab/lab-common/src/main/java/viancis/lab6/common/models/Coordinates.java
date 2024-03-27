package viancis.lab6.common.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Coordinates")
public class Coordinates {
    private Double x;
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

    @XmlElement(name = "x")
    public void setX(Double x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    @XmlElement(name = "y")
    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("""
                        
                        \t\t\tx=%s,
                        \t\t\ty=%s
                        """, x, y);
    }
}

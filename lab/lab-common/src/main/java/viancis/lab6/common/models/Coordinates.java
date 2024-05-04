package viancis.lab6.common.models;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


public class Coordinates implements Serializable {
    private static final int MIN_Y_VALUE = -360;

    private Double x;

    @Min(MIN_Y_VALUE)
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

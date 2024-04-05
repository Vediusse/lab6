package viancis.lab6.common.models;


import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime unmarshal(String v) {
        return ZonedDateTime.parse(v);
    }

    @Override
    public String marshal(ZonedDateTime v) {
        return v.toString();
    }
}
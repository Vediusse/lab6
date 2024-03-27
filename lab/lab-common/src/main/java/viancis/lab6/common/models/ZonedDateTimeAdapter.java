package viancis.lab6.common.models;


import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime unmarshal(String v) {
        return ZonedDateTime.parse(v); // Adjust parsing logic as per your XML format
    }

    @Override
    public String marshal(ZonedDateTime v) {
        return v.toString(); // Adjust formatting logic as per your XML format
    }
}
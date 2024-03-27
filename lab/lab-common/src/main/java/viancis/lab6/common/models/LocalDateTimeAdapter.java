package viancis.lab6.common.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;



public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v); // You may need to adjust the parsing logic based on your XML format
    }

    @Override
    public String marshal(LocalDateTime v) {
        return v.toString(); // You may need to adjust the formatting logic based on your XML format
    }
}

package viancis.lab6.server.collection;

import viancis.lab6.common.models.MusicBand;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper; // TODO useless import
import javax.xml.bind.annotation.XmlRootElement;
import java.util.PriorityQueue;


// TODO попробуй lombok

@XmlRootElement(name = "Scheme")
public class Collection {
    private PriorityQueue<MusicBand> musicBands;

    public Collection(PriorityQueue<MusicBand> musicBands) {
        this.musicBands = musicBands;
    }

    public Collection() {
    }

    @XmlElement(name = "MusicBand")
    public PriorityQueue<MusicBand> getMusicBands() {
        return musicBands;
    }

    public void setMusicBands(PriorityQueue<MusicBand> musicBands) {
        this.musicBands = musicBands;
    }
}

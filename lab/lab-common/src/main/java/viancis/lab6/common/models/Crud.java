package viancis.lab6.common.models;

import java.util.PriorityQueue;



public class Crud {
    private PriorityQueue<MusicBand> musicBandPriorityQueue;

    public Crud(PriorityQueue<MusicBand> musicBands) {
        this.musicBandPriorityQueue = musicBands;
    }

    public Crud() {
    }


    public PriorityQueue<MusicBand> getMusicBands() {
        return musicBandPriorityQueue;
    }

    public void setMusicBands(PriorityQueue<MusicBand> musicBands) {
        this.musicBandPriorityQueue = musicBands;
    }

    public String show() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for (MusicBand musicBand : musicBandPriorityQueue) {
            result.append(musicBand).append("\n\n\n\n\n");
        }
        return result.toString();
    }
    public String add(MusicBand musicBand) {
        if (musicBand != null) {
            this.musicBandPriorityQueue.add(musicBand);
            return "     Новая банда была успешно создана";
        }
        return "     Interupt adding";
    }
}

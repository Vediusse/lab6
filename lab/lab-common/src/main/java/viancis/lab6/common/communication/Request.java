package viancis.lab6.common.communication;

import viancis.lab6.common.models.MusicBand;

import java.io.Serializable;
import java.util.PriorityQueue;

public record Request(String command, String[] commandArgs, MusicBand element) implements Serializable {


}

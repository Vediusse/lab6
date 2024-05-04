package viancis.lab6.common.communication;

import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.User;

import java.io.Serializable;
import java.util.PriorityQueue;

public record Request(String command, String[] commandArgs, MusicBand element, User user, String token) implements Serializable {


}

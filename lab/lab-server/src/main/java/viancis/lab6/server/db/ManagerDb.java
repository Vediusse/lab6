package viancis.lab6.server.db;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import viancis.lab6.server.collection.Collection;

import viancis.lab6.common.models.*;
import viancis.lab6.server.collection.Storage;

public class ManagerDb {
    private final ConnectionBataDase ConnectinDb;

    public ManagerDb(ConnectionBataDase dbConnector) {
        this.ConnectinDb = dbConnector;
    }

    public void initializeDB() throws SQLException {
        String idSequenceCreationQuery = RequestsDb.CREATE_SEQUENCE.getQuery();
        String idSequenceFrontmenCreationQuery = RequestsDb.CREATE_FRONTMEN_SEQUENCE.getQuery();
        String userTableCreationQuery = RequestsDb.CREATE_USERS_TABLE.getQuery();
        String frontmenTableQuery = RequestsDb.CREATE_FRONTMEN_TABLE.getQuery();
        String TableCreationQuery = RequestsDb.CREATE_MUSIK_BAND_TABLE.getQuery();
        String userSeqQuery = RequestsDb.CREATE_USER_SEQUENCE.getQuery();
        try (Connection connection = new ConnectionBataDase().getConnection();
             PreparedStatement idSequenceStatement = connection.prepareStatement(idSequenceCreationQuery);
             PreparedStatement idSequenceFrontMenStatement = connection.prepareStatement(idSequenceFrontmenCreationQuery);
             PreparedStatement frontMenTableStatement = connection.prepareStatement(frontmenTableQuery);
             PreparedStatement idSequenceUserStatement = connection.prepareStatement(userSeqQuery);
             PreparedStatement userTableStatement = connection.prepareStatement(userTableCreationQuery);
             PreparedStatement musicTableStatement = connection.prepareStatement(TableCreationQuery)
        ) {
            idSequenceFrontMenStatement.execute();
            idSequenceStatement.execute();
            idSequenceUserStatement.execute();
            userTableStatement.execute();
            frontMenTableStatement.execute();
            musicTableStatement.execute();
        }
    }

    public long getNextSequenceValue() throws SQLException {
        String query = "SELECT nextval('ids')";
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return -1;
    }

    public void addMusikBand(MusicBand musicBand, User author) throws SQLException {
        String query = RequestsDb.INSERT_MUSIK_BAND.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, musicBand.getName());

            statement.setDouble(2, musicBand.getCoordinates().getX());
            statement.setLong(3, musicBand.getCoordinates().getY());

            statement.setInt(4, musicBand.getNumberOfParticipants());

            statement.setLong(5, musicBand.getSinglesCount());


            LocalDateTime localDateTime = musicBand.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            statement.setTimestamp(6, timestamp);


            ZonedDateTime establishDate = musicBand.getEstablishmentDate();
            Instant instant = establishDate.toInstant();
            Timestamp timestampEstablish = Timestamp.from(instant);
            statement.setTimestamp(7, timestampEstablish);


            statement.setString(8, musicBand.getGenre().toString());
            statement.setString(9, author.getLogin());
            if (musicBand.getFrontMan() != null) {
                long index = this.addFrontMen(musicBand.getFrontMan(), author.getLogin());
                statement.setLong(10, index);
            } else {
                statement.setNull(10, Types.BIGINT);
            }

            statement.executeUpdate();
        }
    }

    public MusicBand findMusikBandById(long id) throws SQLException {
        String query = RequestsDb.FIND_MUSIK_BAND_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractMusikBandFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public void deleteMusikBandById(long id) throws SQLException {
        String query = RequestsDb.DELETE_MUSIK_BAND_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public long updateMusikBandById(long id, MusicBand element) throws SQLException {
        String query = RequestsDb.UPDATE_MUSIK_BAND_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, element.getName());
            statement.setDouble(2, element.getCoordinates().getX());
            statement.setFloat(3, element.getCoordinates().getY());
            statement.setInt(4, element.getNumberOfParticipants());
            LocalDateTime localDateTime = element.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            statement.setTimestamp(5, timestamp);


            ZonedDateTime establishDate = element.getEstablishmentDate();
            Instant instant = establishDate.toInstant();
            Timestamp timestampEstablish = Timestamp.from(instant);
            statement.setTimestamp(6, timestampEstablish);

            statement.setLong(7, element.getSinglesCount());
            statement.setString(8, element.getGenre().toString());
            if (element.getFrontMan() != null) {
                long idFrontMen = this.addFrontMen(element.getFrontMan(), element.getAuthor().getLogin());
                element.getFrontMan().setId(idFrontMen);
                statement.setLong(9, idFrontMen);
            } else {
                statement.setNull(9, Types.BIGINT);
            }
            statement.setString(10, element.getAuthor().getLogin());
            statement.setLong(11, id);
            statement.executeUpdate();
            return id;
        }
    }

    public List<MusicBand> getAllMusikBands() throws SQLException {
        List<MusicBand> musikBands = new ArrayList<>();
        String query = RequestsDb.SELECT_ALL_BANDS.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            long generatedId = 0;
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }

            while (resultSet.next()) {
                musikBands.add(extractMusikBandFromResultSet(resultSet));
            }
        }
        return musikBands;
    }

    private MusicBand extractMusikBandFromResultSet(ResultSet resultSet) throws SQLException {
        MusicGenre genre = MusicGenre.valueOf(resultSet.getString("genre"));
        LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
        ZonedDateTime establishmentDate = ZonedDateTime.parse(resultSet.getString("establishmentDate"));

        return new MusicBand(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("x"),
                resultSet.getLong("y"),
                creationDate,
                resultSet.getInt("numberOfParticipants"),
                resultSet.getLong("singlesCount"),
                establishmentDate,
                genre,
                this.findFrontMenById(resultSet.getLong("frontMen_id")),
                this.findUserById(resultSet.getLong("author"))
        );
    }

    public long addFrontMen(Person frontMan, String author) throws SQLException {
        String query = RequestsDb.INSERT_FRONT_MEN.getQuery();
        long generatedId = -1;
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, frontMan.getName());
            statement.setInt(2, frontMan.getHeight());
            statement.setString(3, frontMan.getEyeColor());
            statement.setString(4, frontMan.getHairColor());
            statement.setString(5, frontMan.getNationality());
            statement.setString(6, author);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        }
        return generatedId;
    }

    public Person findFrontMenById(long id) throws SQLException {
        String query = RequestsDb.FIND_FRONT_MEN_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractFrontMenFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public void deleteFrontMenById(long id) throws SQLException {
        String query = RequestsDb.DELETE_FRONT_MEN_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void updateFrontMenById(long id, String frontManName, int frontManHeight, String eyeColor, String hairColor, String nationality) throws SQLException {
        String query = RequestsDb.UPDATE_FRONT_MEN_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, frontManName);
            statement.setInt(2, frontManHeight);
            statement.setString(3, eyeColor);
            statement.setString(4, hairColor);
            statement.setString(5, nationality);
            statement.setLong(6, id);
            statement.executeUpdate();
        }
    }

    public List<Person> getAllFrontMen() throws SQLException {
        List<Person> frontMenList = new ArrayList<>();
        String query = RequestsDb.SELECT_ALL_FRONTMEN.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                frontMenList.add(extractFrontMenFromResultSet(resultSet));
            }
        }
        return frontMenList;
    }

    private Person extractFrontMenFromResultSet(ResultSet resultSet) throws SQLException {
        Color eyeColor = Color.valueOf(resultSet.getString("eyeColor"));
        Color hairColor = Color.valueOf(resultSet.getString("hairColor"));
        Country nationality = Country.valueOf(resultSet.getString("nationality"));

        return new Person(
                resultSet.getString("frontManName"),
                resultSet.getInt("frontManHeight"),
                eyeColor,
                hairColor,
                nationality
        );
    }

    public void addUser(String username, String password, String role) throws SQLException {
        String query = RequestsDb.INSERT_USER.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        }
    }

    public User findUserById(long id) throws SQLException {
        String query = RequestsDb.FIND_USER_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public void deleteUserById(long id) throws SQLException {
        String query = RequestsDb.DELETE_USER_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void updateUserById(long id, String username, String password, String role) throws SQLException {
        String query = RequestsDb.UPDATE_USER_BY_ID.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.setLong(4, id);
            statement.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = RequestsDb.SELECT_ALL_USERS.getQuery();
        try (Connection connection = ConnectinDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                userList.add(extractUserFromResultSet(resultSet));
            }
        }
        return userList;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User.Roles role = User.Roles.valueOf(resultSet.getString("role"));

        return new User(
                resultSet.getString("username"),
                resultSet.getString("password"),
                role
        );
    }


    public Collection readCollection() throws IOException {
        Collection collection = new Collection();
        HashMap<Integer, Person> frontmenMap = new HashMap<>();
        HashMap<String, User> usersMap = new HashMap<>();

        String musicBandQuery = RequestsDb.SELECT_ALL_BANDS_BY_USER.getQuery();
        try (Connection connection = new ConnectionBataDase().getConnection();
             PreparedStatement statementMusicBand = connection.prepareStatement(musicBandQuery)) {
            Statement statement = connection.createStatement();
            ResultSet frontmenResultSet = statement.executeQuery(RequestsDb.SELECT_ALL_FRONTMEN.getQuery());
            while (frontmenResultSet.next()) {
                Person frontMan = new Person();
                int frontManId = frontmenResultSet.getInt("id");
                frontMan.setName(frontmenResultSet.getString("frontManName"));
                frontMan.setEyeColor(Color.valueOf(frontmenResultSet.getString("eyeColor")));
                frontMan.setHairColor(Color.valueOf(frontmenResultSet.getString("hairColor")));
                frontMan.setNationality(Country.valueOf(frontmenResultSet.getString("nationality")));
                frontmenMap.put(frontManId, frontMan);
            }
            frontmenResultSet.close();

            ResultSet usersResultSet = statement.executeQuery(RequestsDb.SELECT_ALL_USERS.getQuery());
            while (usersResultSet.next()) {
                String authorName = usersResultSet.getString("login");
                String role = usersResultSet.getString("role");
                User author = new User(authorName, role);
                usersMap.put(author.getLogin(), author);
            }

            usersResultSet.close();

            ResultSet musicBandResultSet = statement.executeQuery(RequestsDb.SELECT_ALL_BANDS.getQuery());

            while (musicBandResultSet.next()) {
                MusicBand band = new MusicBand();
                int frontManId = musicBandResultSet.getInt("frontMen_id");
                String authorUser = musicBandResultSet.getString("author");

                Person frontMan = frontmenMap.get(frontManId);
                band.setFrontMan(frontMan);

                User author = usersMap.get(authorUser);
                if (author != null) {
                    band.setAuthor(author);
                }

                band.setId(musicBandResultSet.getInt("id"));
                band.setName(musicBandResultSet.getString("name"));
                band.setCoordinates(new Coordinates(musicBandResultSet.getDouble("x"), musicBandResultSet.getLong("y")));
                band.setCreationDate(convertDateToLocalDateTime(musicBandResultSet, "creationdate"));
                band.setNumberOfParticipants(musicBandResultSet.getInt("numberOfParticipants"));
                band.setSinglesCount(musicBandResultSet.getInt("singlesCount"));
                band.setGenre(MusicGenre.valueOf(musicBandResultSet.getString("genre")));

                collection.addInCollection(band, author);
            }
            musicBandResultSet.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collection;
    }


    public LocalDateTime convertDateToLocalDateTime(ResultSet resultSet, String columnName) throws SQLException {
        Date date = resultSet.getDate(columnName);
        return (date != null) ? date.toLocalDate().atStartOfDay() : null;
    }
}

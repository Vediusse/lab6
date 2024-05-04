package viancis.lab6.server.db;

public enum RequestsDb {
    CREATE_MUSIK_BAND_TABLE("CREATE TABLE IF NOT EXISTS s409463MusikBand ("
            + "id bigint PRIMARY KEY DEFAULT (nextval('ids')),"
            + "name varchar(255) NOT NULL CHECK(name <> ''),"
            + "x double precision NOT NULL,"
            + "y bigint NOT NULL CHECK(y > -360),"
            + "creationdate timestamp NOT NULL DEFAULT current_timestamp,"
            + "establishmentDate timestamp with time zone NOT NULL,"
            + "numberOfParticipants int NOT NULL,"
            + "singlesCount bigint NOT NULL,"
            + "genre varchar(50) CHECK("
            + "genre='RAP' OR genre='BRIT_POP' OR "
            + "genre='TOTALITARIANISM' OR genre='THEOCRACY' "
            + "OR genre='THALASSOCRACY'),"
            + "frontMen_id bigint REFERENCES s409463FrontMen (id),"
            + "author VARCHAR(255) REFERENCES s409463Users (login)"
            + ")"),

    GENERATE_NEXT_ID_MUSIC("SELECT nextval('ids')"),

    CREATE_FRONTMEN_TABLE("CREATE TABLE IF NOT EXISTS s409463FrontMen ("
            + "id bigint PRIMARY KEY DEFAULT (nextval('ids_human')),"
            + "frontManName varchar(255) NOT NULL,"
            + "frontManHeight int NOT NULL,"
            + "eyeColor VARCHAR(20) CHECK("
            + "eyeColor='RED' OR eyeColor='BLACK' OR "
            + "eyeColor='BLUE' OR eyeColor='YELLOW' "
            + "OR eyeColor='BROWN'),"
            + "hairColor VARCHAR(20) CHECK("
            + "hairColor='RED' OR hairColor='BLACK' OR "
            + "hairColor='BLUE' OR hairColor='YELLOW' "
            + "OR hairColor='BROWN'),"
            + "nationality VARCHAR(30) CHECK("
            + "nationality='GERMANY' OR nationality='SPAIN' OR nationality='ITALY'),"
            + "author VARCHAR(255) REFERENCES s409463Users (login)"
            + ")"),

    CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS s409463Users ("
            + "login varchar(255) PRIMARY KEY CHECK (TRIM(login) <> ''),"
            + "password char(128) DEFAULT NULL,"
            + "role varchar(100) DEFAULT 'user' CHECK("
            + "role='admin' OR role='moderator' OR role='user')"
            + ")"),

    CREATE_FRONTMEN_SEQUENCE("CREATE SEQUENCE IF NOT EXISTS ids_human START 1"),

    CREATE_USER_SEQUENCE("CREATE SEQUENCE IF NOT EXISTS ids_users START 1"),

    CREATE_SEQUENCE("CREATE SEQUENCE IF NOT EXISTS ids START 1"),


    INSERT_MUSIK_BAND("INSERT INTO s409463MusikBand (name, x, y, numberOfParticipants, singlesCount, creationDate, establishmentDate, genre, author, frontMen_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    FIND_MUSIK_BAND_BY_ID("SELECT * FROM s409463MusikBand WHERE id = ?"),

    DELETE_MUSIK_BAND_BY_ID("DELETE FROM s409463MusikBand WHERE id = ?"),
    UPDATE_MUSIK_BAND_BY_ID("UPDATE s409463MusikBand SET name = ?, x = ?, y = ?, numberOfParticipants = ?,creationDate = ?, establishmentDate = ?, singlesCount = ?, genre = ?, frontMen_id = ?, author = ? WHERE id = ?"),
    SELECT_ALL_BANDS("SELECT * FROM s409463MusikBand"),

    SELECT_ALL_BANDS_BY_USER("SELECT * FROM s409463MusikBand WHERE author = ?"),

    // Add new SQL queries for FrontMen table
    INSERT_FRONT_MEN("INSERT INTO s409463FrontMen (frontManName, frontManHeight, eyeColor, hairColor, nationality, author) VALUES (?, ?, ?, ?, ?, ?)"),
    FIND_FRONT_MEN_BY_ID("SELECT * FROM s409463FrontMen WHERE id = ?"),
    DELETE_FRONT_MEN_BY_ID("DELETE FROM s409463FrontMen WHERE id = ?"),
    UPDATE_FRONT_MEN_BY_ID("UPDATE s409463FrontMen SET frontManName = ?, frontManHeight = ?, eyeColor = ?, hairColor = ?, nationality = ? WHERE id = ?"),
    SELECT_ALL_FRONTMEN("SELECT * FROM s409463FrontMen"),
    SELECT_ALL_FRONTMEN_BY_USER("SELECT * FROM s409463FrontMen WHERE author = ?"),

    // Add new SQL queries for Users table
    ADD_USER("INSERT INTO s409463Users (login, password) VALUES (?, ?)"),

    INSERT_USER("INSERT INTO s409463Users (login, password, role) VALUES (?, ?, ?)"),
    FIND_USER_BY_ID("SELECT * FROM s409463Users WHERE id = ?"),
    DELETE_USER_BY_ID("DELETE FROM s409463Users WHERE id = ?"),

    FIND_USER_BY_LOG_AND_PASS("SELECT * FROM s409463Users WHERE login=? AND password=?"),

    UPDATE_USER_BY_ID("UPDATE s409463Users SET login = ?, password = ?, role = ? WHERE id = ?"),
    SELECT_ALL_USERS("SELECT * FROM s409463Users");

    private final String query;

    RequestsDb(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

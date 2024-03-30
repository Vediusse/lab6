package viancis.lab6.common.models;

import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;


// TODO мб поставить \t что бы инпут не ломался при разных размерах консоли
public class MusicBandForm {

    private final Validator validator;
    private final Sender sender;

    public MusicBandForm(Sender sender) {
        this.validator = new Validator();
        this.sender = sender;
    }

    public MusicBand createNew() {
        Scanner scanner = new Scanner(System.in);
        try {
            sender.printInLineMessage(new Message(Category.INPUT, "     Имя новой Банды: "));
            String name = getNonEmptyInput(scanner);
            validator.validateName(name, "Name");

            sender.printInLineMessage(new Message(Category.INPUT, "     Координата X: "));
            Long x = Long.parseLong(getNonEmptyInput(scanner));

            validator.validateLongNotNull(x, "X");

            sender.printInLineMessage(new Message(Category.INPUT, "     Координата Y: "));
            Double y = Double.parseDouble(getNonEmptyInput(scanner));
            validator.validateDoubleNotNull(y, "Y");
            validator.validateYValue(y);

            sender.printInLineMessage(new Message(Category.INPUT, "     Количество участников: "));
            Integer numberOfParticipants = Integer.parseInt(getNonEmptyInput(scanner));
            validator.validateNumberOfParticipants(numberOfParticipants, "Number of parcipitants");

            sender.printInLineMessage(new Message(Category.INPUT, "     Количество синглов: "));
            long singlesCount = Long.parseLong(getNonEmptyInput(scanner));
            validator.validateSinglesCount(singlesCount, "Singles Count");

            sender.printInLineMessage(new Message(Category.INPUT, "     Жанр (RAP, PSYCHEDELIC_CLOUD_RAP, BRIT_POP): "));
            String genreString = getNonEmptyInput(scanner).toUpperCase();
            MusicGenre genre = MusicGenre.valueOf(genreString);
            validator.validateMusicGenre(genre, "Genre");

            ZonedDateTime establishmentDate = this.inputEstablishmentDate(scanner);

            sender.printInLineMessage(new Message(Category.INPUT, "     Кто такой фронтмен (Y - если очень хочется , N - если хочется): "));
            String isFrontman = scanner.next();

            if (isFrontman.equalsIgnoreCase("Y")) {
                Person frontman = FrontmenForm(scanner);
                if (frontman != null) {
                    return new MusicBand(name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate, frontman);
                }
            } else {
                return new MusicBand(name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
            }

        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.INPUT, "     └──Неверная запись поля\n"));
            this.createNew(); // TODO т.е при ошибки у тебя заного запускается весь инпут? потанциально можно переполнить рекурсию
        }
        return null;
    }

    private String getNonEmptyInput(Scanner scanner) {
        String input = "";
        while (input.isEmpty()) {
            try {
                input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    sender.printInLineMessage(new Message(Category.INPUT, "Please enter a non-empty value."));
                    sender.printInLineMessage(new Message(Category.INPUT, "\t"));
                }
            } catch (NoSuchElementException e) {
                sender.printInLineMessage(new Message(Category.INPUT, "Input terminated unexpectedly. Please try again."));
                System.exit(400);
            }
        }
        return input;
    }

    private Person FrontmenForm(Scanner scanner) {
        try {
            sender.printInLineMessage(new Message(Category.INPUT, "          Имя фронтмена: "));
            String frontManName = scanner.next();
            scanner.nextLine();

            sender.printInLineMessage(new Message(Category.INPUT, "          Рост фронтмена: "));
            Integer frontManHeight = Integer.parseInt(scanner.next());
            validator.validateIntegerNotNull(frontManHeight, "Рост");

            sender.printInLineMessage(new Message(Category.INPUT, "          Цвет глаз фронтмена (RED, BLACK, BLUE, YELLOW, BROWN): "));
            String eyeColorString = scanner.next();
            Color eyeColor = Color.valueOf(eyeColorString);
            validator.validateColor(eyeColor, "Цвет глаз");

            sender.printInLineMessage(new Message(Category.INPUT, "          Цвет волос фронтмена (GREEN, BLACK, BLUE, ORANGE, BROWN): "));
            String hairColorString = scanner.next();
            Color hairColor = Color.valueOf(hairColorString);
            validator.validateColor(hairColor, "Цвет волос");

            sender.printInLineMessage(new Message(Category.INPUT, "          Национальность фронтмена (GERMANY, SPAIN, ITALY): "));
            String nationalityString = scanner.next();
            Country nationality = Country.valueOf(nationalityString);
            validator.validateCountry(nationality, "Национальность");

            return new Person(frontManName, frontManHeight, eyeColor, hairColor, nationality);

        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.INPUT, "          └──Неверная запись поля"));
            return FrontmenForm(scanner); // Recursive call to handle incorrect input
        } catch (Exception e) {
            scanner.close();
            sender.printInLineMessage(new Message(Category.INPUT, "\nEnd of input. Exiting... \n"));
        }
        return null; // Return null if no frontman is specified
    }

    private ZonedDateTime inputEstablishmentDate(Scanner scanner) {
        while (true) {
            try {

                int currentYear = Year.now().getValue();

                sender.printInLineMessage(new Message(Category.INPUT, "          Год (гггг) (1600-" + currentYear + "): "));
                int year = Integer.parseInt(scanner.next());
                if (year < 1600 || year > currentYear) {
                    throw new DateTimeException("Invalid year. Please enter a valid year (1600-" + currentYear + ").");
                }
                int currentMonth = LocalDate.now().getMonthValue();
                if (year == currentYear) {
                    sender.printInLineMessage(new Message(Category.INPUT, "          Месяц (мм) (1-" + currentMonth + "): "));
                } else {
                    sender.printInLineMessage(new Message(Category.INPUT, "          Месяц (мм) (1-12): "));
                }
                int month = Integer.parseInt(scanner.next());


                if ((year == currentYear && month > currentMonth) || month < 1 || month > 12) {
                    throw new DateTimeException("Invalid month. Please enter a valid month (1-" + currentMonth + ").");
                }
                int currentDay = LocalDate.now().getDayOfMonth();
                int maxDaysInMonth = YearMonth.of(year, month).lengthOfMonth();

                if (year == currentYear && month == currentMonth) {
                    sender.printInLineMessage(new Message(Category.INPUT, "          День (дд) (1-" + currentDay + "): "));
                } else {
                    sender.printInLineMessage(new Message(Category.INPUT, "          День (дд) (1-" + maxDaysInMonth + "): "));
                }
                int day = Integer.parseInt(scanner.next());


                if (day < 1 || day > maxDaysInMonth ||
                        (year == currentYear && month == currentMonth && day > LocalDate.now().getDayOfMonth())) {
                    throw new DateTimeException("Invalid day. Please enter a valid day.");
                }

                sender.printInLineMessage(new Message(Category.INPUT, "          Время (чч:мм:сс): "));
                String timeString = scanner.next();

                if (!timeString.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    throw new DateTimeException("Invalid time format. Please enter a valid time in HH:mm:ss format. \n");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String fullDateTimeString = String.format("%04d-%02d-%02d %s", year, month, day, timeString);
                LocalDateTime localDateTime = LocalDateTime.parse(fullDateTimeString, formatter);

                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime userDateTime = localDateTime.atZone(zoneId);
                ZonedDateTime currentDateTime = ZonedDateTime.now(zoneId);

                if (userDateTime.isAfter(currentDateTime)) {
                    throw new DateTimeException("Invalid date. Please enter a date not exceeding the current date. \n");
                }

                return userDateTime;

            } catch (NumberFormatException | DateTimeException e) {
                sender.printInLineMessage(new Message(Category.INPUT, "          └──  Пожалуйста, введите снова.\n"));
            }
        }
    }

    public MusicBand update(MusicBand oldMusikBand) {
        Scanner scanner = new Scanner(System.in);
        try {
            sender.printInLineMessage(new Message(Category.INPUT, "     Новое имя для " + oldMusikBand.getName() + " : "));
            String name = scanner.next();
            if (name != null) {
                validator.validateName(name, "Name");
                oldMusikBand.setName(name);
            }


            sender.printInLineMessage(new Message(Category.INPUT, "     Координата X: "));
            String xString = scanner.next();
            if (xString != null) {
                Double x = Double.parseDouble(xString);
                oldMusikBand.getCoordinates().setX(x);
            }

            sender.printInLineMessage(new Message(Category.INPUT, "     Координата Y: "));
            String yString = scanner.next();
            if (yString != null) {
                Long y = Long.parseLong(yString);
                validator.validateYValue(Double.valueOf(y));
                oldMusikBand.setCoordinates(new Coordinates(oldMusikBand.getCoordinates().getX(), y));
            }

            sender.printInLineMessage(new Message(Category.INPUT, "     Количество участников (" + oldMusikBand.getNumberOfParticipants() + ") : "));
            String numberOfParticipantsString = scanner.next();
            if (numberOfParticipantsString != null) {
                Integer numberOfParticipants = Integer.parseInt(numberOfParticipantsString);
                validator.validateNumberOfParticipants(numberOfParticipants, "Number of parcipitants");
                oldMusikBand.setNumberOfParticipants(Integer.parseInt(numberOfParticipantsString));
            }
            sender.printInLineMessage(new Message(Category.INPUT, "     Количество синглов(" + oldMusikBand.getSinglesCount() + ") : "));

            String singlesCountString = scanner.next();
            if (singlesCountString != null) {
                long singlesCount = Long.parseLong(singlesCountString);
                validator.validateSinglesCount(singlesCount, "Singles Count");
                oldMusikBand.setSinglesCount(singlesCount);
            }

            sender.printInLineMessage(new Message(Category.INPUT, "     Жанр (RAP, PSYCHEDELIC_CLOUD_RAP, BRIT_POP) сейчас (" + oldMusikBand.getGenre() + ") : "));
            String genreString = scanner.next();

            if (genreString != null) {
                MusicGenre genre = MusicGenre.valueOf(genreString);
                validator.validateMusicGenre(genre, "Genre");
                oldMusikBand.setGenre(genre);
            }

            sender.printInLineMessage(new Message(Category.INPUT, "     establishmentDate будем меняеть ? (сейчас " + oldMusikBand.getEstablishmentDate() + " ) (Y - если да) : "));
            String isEstablishmentDate = scanner.next();
            ZonedDateTime establishmentDate;
            if (isEstablishmentDate.equalsIgnoreCase("Y")) {
                establishmentDate = this.inputEstablishmentDate(scanner);
                oldMusikBand.setEstablishmentDate(establishmentDate);
            }

            Person frontMan = null;
            if (oldMusikBand.getFrontMan() == null) {
                sender.printInLineMessage(new Message(Category.INPUT, "     Создать фронтмена (Y - да, N - нет): "));
                String createFrontman = scanner.next();
                if (createFrontman.equalsIgnoreCase("Y")) {
                    frontMan = FrontmenForm(scanner);
                }
            } else {
                sender.printInLineMessage(new Message(Category.INPUT, "     Изменить фронтмена (Y - да, N - нет): "));
                String updateFrontman = scanner.next();
                if (updateFrontman.equalsIgnoreCase("Y")) {
                    frontMan = FrontmenForm(scanner);
                }
            }

            return oldMusikBand;
        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.INPUT, "     └──Неверная запись поля \n"));
            this.createNew();
        } catch (Exception e) {
            scanner.close();
            sender.printInLineMessage(new Message(Category.INPUT, "\nEnd of input. Exiting..."));
        }
        return oldMusikBand;
    }
}
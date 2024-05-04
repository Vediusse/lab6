package viancis.lab6.common.models;

import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MusicBandForm {

    private final Validator validator;
    private final Sender sender;


    MusicBand result = null;

    public MusicBandForm(Sender sender) {
        this.validator = new Validator();
        this.sender = sender;
    }

    public MusicBand createNew() {
        Scanner scanner = new Scanner(System.in);
        try {
            sender.printInLineMessage(new Message(Category.Input, "     Имя новой Банды: "));
            String name = getNonEmptyInput(scanner);
            validator.validateName(name, "Name");

            sender.printInLineMessage(new Message(Category.Input, "     Координата X: "));
            Double x = Double.parseDouble(getNonEmptyInput(scanner));

            validator.validateDoubleNotNull(x, "X");

            sender.printInLineMessage(new Message(Category.Input, "     Координата Y: "));
            Long y = Long.parseLong(getNonEmptyInput(scanner));
            validator.validateLongNotNull(y, "Y");
            validator.validateYValue(y);

            sender.printInLineMessage(new Message(Category.Input, "     Количество участников: "));
            Integer numberOfParticipants = Integer.parseInt(getNonEmptyInput(scanner));
            validator.validateNumberOfParticipants(numberOfParticipants, "Number of parcipitants");

            sender.printInLineMessage(new Message(Category.Input, "     Количество синглов: "));
            long singlesCount = Long.parseLong(getNonEmptyInput(scanner));
            validator.validateSinglesCount(singlesCount, "Singles Count");

            sender.printInLineMessage(new Message(Category.Input, "     Жанр (RAP, PSYCHEDELIC_CLOUD_RAP, BRIT_POP): "));
            String genreString = getNonEmptyInput(scanner).trim().toUpperCase(); // Преобразование к верхнему регистру и удаление пробелов
            MusicGenre genre = MusicGenre.valueOf(genreString);
            validator.validateMusicGenre(genre, "Genre");

            ZonedDateTime establishmentDate = this.inputEstablishmentDate(scanner);

            sender.printInLineMessage(new Message(Category.Input, "     Кто такой фронтмен (Y - если очень хочется , N - если  хочется): "));
            String isFrontman = scanner.next();

            if (isFrontman.equalsIgnoreCase("Y")) {
                Person frontman = FrontmenForm(scanner);
                if (frontman != null) {
                    result = new MusicBand(0, name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate, frontman);
                }else{
                    result = new MusicBand(0, name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
                }
            }else{
                result = new MusicBand(0, name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
            }
            } catch (IllegalArgumentException e) {
                sender.printInLineMessage(new Message(Category.Input, "     └──Неверная запись поля\n"));
                MusicBand result = createNew();
            }
        return result;
    }

    private String getNonEmptyInput(Scanner scanner) {
        String input = "";
        while (input.isEmpty()) {
            try {
                input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    sender.printInLineMessage(new Message(Category.Input, "Please enter a non-empty value.\n"));
                    sender.printInLineMessage(new Message(Category.Input, "     "));
                }
            } catch (NoSuchElementException e) {
                sender.printInLineMessage(new Message(Category.Input, "Input terminated unexpectedly. Please try again."));
                System.exit(400);
            }
        }
        return input;
    }

    private Person FrontmenForm(Scanner scanner) {
        try {
            sender.printInLineMessage(new Message(Category.Input, "          Имя фронтмена: "));
            String frontManName = scanner.next();
            scanner.nextLine();

            sender.printInLineMessage(new Message(Category.Input, "          Рост фронтмена: "));
            Integer frontManHeight = Integer.parseInt(scanner.next());
            validator.validateIntegerNotNull(frontManHeight, "Рост");

            sender.printInLineMessage(new Message(Category.Input, "          Цвет глаз фронтмена (RED, BLACK, BLUE, YELLOW, BROWN): "));
            String eyeColorString = scanner.next();
            Color eyeColor = Color.valueOf(eyeColorString);
            validator.validateColor(eyeColor, "Цвет глаз");

            sender.printInLineMessage(new Message(Category.Input, "          Цвет волос фронтмена (GREEN, BLACK, BLUE, ORANGE, BROWN): "));
            String hairColorString = scanner.next();
            Color hairColor = Color.valueOf(hairColorString);
            validator.validateColor(hairColor, "Цвет волос");

            sender.printInLineMessage(new Message(Category.Input, "          Национальность фронтмена (GERMANY, SPAIN, ITALY): "));
            String nationalityString = scanner.next();
            Country nationality = Country.valueOf(nationalityString);
            validator.validateCountry(nationality, "Национальность");

            return new Person(frontManName, frontManHeight, eyeColor, hairColor, nationality);

        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.Input, "          └──Неверная запись поля"));
            return FrontmenForm(scanner); // Recursive call to handle incorrect input
        } catch (Exception e) {
            scanner.close();
            sender.printInLineMessage(new Message(Category.Input, "\nEnd of input. Exiting... \n"));
        }
        return null; // Return null if no frontman is specified
    }

    private ZonedDateTime inputEstablishmentDate(Scanner scanner) {
        while (true) {
            try {

                int currentYear = Year.now().getValue();

                sender.printInLineMessage(new Message(Category.Input, "          Год (гггг) (1600-" + currentYear + "): "));
                int year = Integer.parseInt(scanner.next());
                if (year < 1600 || year > currentYear) {
                    throw new DateTimeException("Invalid year. Please enter a valid year (1600-" + currentYear + ").");
                }
                int currentMonth = LocalDate.now().getMonthValue();
                if (year == currentYear) {
                    sender.printInLineMessage(new Message(Category.Input, "          Месяц (мм) (1-" + currentMonth + "): "));
                } else {
                    sender.printInLineMessage(new Message(Category.Input, "          Месяц (мм) (1-12): "));
                }
                int month = Integer.parseInt(scanner.next());


                if ((year == currentYear && month > currentMonth) || month < 1 || month > 12) {
                    throw new DateTimeException("Invalid month. Please enter a valid month (1-" + currentMonth + ").");
                }
                int currentDay = LocalDate.now().getDayOfMonth();
                int maxDaysInMonth = YearMonth.of(year, month).lengthOfMonth();

                if (year == currentYear && month == currentMonth) {
                    sender.printInLineMessage(new Message(Category.Input, "          День (дд) (1-" + currentDay + "): "));
                } else {
                    sender.printInLineMessage(new Message(Category.Input, "          День (дд) (1-" + maxDaysInMonth + "): "));
                }
                int day = Integer.parseInt(scanner.next());


                if (day < 1 || day > maxDaysInMonth ||
                        (year == currentYear && month == currentMonth && day > LocalDate.now().getDayOfMonth())) {
                    throw new DateTimeException("Invalid day. Please enter a valid day.");
                }

                sender.printInLineMessage(new Message(Category.Input, "          Время (чч:мм:сс): "));
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
                sender.printInLineMessage(new Message(Category.Input, "          └──  Пожалуйста, введите снова.\n"));
            }
        }
    }

    public MusicBand update(MusicBand oldMusikBand) {
        Scanner scanner = new Scanner(System.in);
        try {
            sender.printInLineMessage(new Message(Category.Input, "     Новое имя для " + oldMusikBand.getName() + " : "));
            String name = scanner.next();
            if (name != null) {
                validator.validateName(name, "Name");
                oldMusikBand.setName(name);
            }


            sender.printInLineMessage(new Message(Category.Input, "     Координата X: "));
            String xString = scanner.next();
            if (xString != null) {
                Double x = Double.parseDouble(xString);
                oldMusikBand.getCoordinates().setX(x);
            }

            sender.printInLineMessage(new Message(Category.Input, "     Координата Y: "));
            String yString = scanner.next();
            if (yString != null) {
                Long y = Long.parseLong(yString);
                validator.validateYValue(y);
                oldMusikBand.setCoordinates(new Coordinates(oldMusikBand.getCoordinates().getX(), y));
            }

            sender.printInLineMessage(new Message(Category.Input, "     Количество участников (" + oldMusikBand.getNumberOfParticipants() + ") : "));
            String numberOfParticipantsString = scanner.next();
            if (numberOfParticipantsString != null) {
                Integer numberOfParticipants = Integer.parseInt(numberOfParticipantsString);
                validator.validateNumberOfParticipants(numberOfParticipants, "Number of parcipitants");
                oldMusikBand.setNumberOfParticipants(Integer.parseInt(numberOfParticipantsString));
            }
            sender.printInLineMessage(new Message(Category.Input, "     Количество синглов(" + oldMusikBand.getSinglesCount() + ") : "));

            String singlesCountString = scanner.next();
            if (singlesCountString != null) {
                long singlesCount = Long.parseLong(singlesCountString);
                validator.validateSinglesCount(singlesCount, "Singles Count");
                oldMusikBand.setSinglesCount(singlesCount);
            }

            sender.printInLineMessage(new Message(Category.Input, "     Жанр (RAP, PSYCHEDELIC_CLOUD_RAP, BRIT_POP) сейчас (" + oldMusikBand.getGenre() + ") : "));
            String genreString = scanner.next();

            if (genreString != null) {
                MusicGenre genre = MusicGenre.valueOf(genreString);
                validator.validateMusicGenre(genre, "Genre");
                oldMusikBand.setGenre(genre);
            }

            sender.printInLineMessage(new Message(Category.Input, "     establishmentDate будем меняеть ? (сейчас " + oldMusikBand.getEstablishmentDate() + " ) (Y - если да) : "));
            String isEstablishmentDate = scanner.next();
            ZonedDateTime establishmentDate;
            if (isEstablishmentDate.equalsIgnoreCase("Y")) {
                establishmentDate = this.inputEstablishmentDate(scanner);
                oldMusikBand.setEstablishmentDate(establishmentDate);
            }

            Person frontMan = null;
            if (oldMusikBand.getFrontMan() == null) {
                sender.printInLineMessage(new Message(Category.Input, "     Создать фронтмена (Y - да, N - нет): "));
                String createFrontman = scanner.next();
                if (createFrontman.equalsIgnoreCase("Y")) {
                    frontMan = FrontmenForm(scanner);
                }
            } else {
                sender.printInLineMessage(new Message(Category.Input, "     Изменить фронтмена (Y - да, N - нет): "));
                String updateFrontman = scanner.next();
                if (updateFrontman.equalsIgnoreCase("Y")) {
                    frontMan = FrontmenForm(scanner);
                }
            }

            return oldMusikBand;
        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.Input, "     └──Неверная запись поля \n"));
            this.createNew();
        } catch (Exception e) {
            scanner.close();
            sender.printInLineMessage(new Message(Category.Input, "\nEnd of input. Exiting..."));
        }
        return oldMusikBand;
    }
}
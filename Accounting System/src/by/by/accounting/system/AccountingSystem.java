package by.accounting.system;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AccountingSystem {

    private static final String file = "db.txt";
    private static double summ = 0;
    private static int i = 1;
    private static String exportFile = "exported.txt";

    public static void main(String[] args) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));  //Читаем входной консольный поток
        String str;
        File f = new File(file);
        if (!f.exists()) {  //Проверяем наличие файла
            if (f.createNewFile()) {  //Создаём новый файл базы данных
                System.out.println("Файл " + file + " был успешно создан!");
            } else {
                System.out.println("Не могу создать файл " + file);
                return;
            }
        }
        while ((str = bf.readLine()) != null) {  //Если строка не пустая - пускаем выполнение кода
            String[] commands = str.split(" ");   //Разбиваем входную строку на множество команд, разделённых пробелом
            switch (commands[0]) {  //Вместо if используем более удобный switch
                case "set":
                case "create":
                    System.out.println("Введите тип операции(1(+) - зачисление, 2(-) - списание): ");
                    byte type;
                    String s = bf.readLine();  //Читаем входную строку
                    if ((s.equalsIgnoreCase("+")) || (s.equalsIgnoreCase("1"))) {  //Отсеиваем аргументы
                        type = 0;
                    } else if ((s.equalsIgnoreCase("-")) || (s.equalsIgnoreCase("2"))) {
                        type = 1;
                    } else {
                        System.out.println("Это не тип операции!");
                        break;  //Если аргументы не правильные - отправляем пользователя в "главное меню"
                    }

                    System.out.println("Введите номер карты: ");
                    int card;
                    try {
                        card = Integer.parseInt(bf.readLine());  //Превращаем входной String в int
                    } catch (NumberFormatException ex) {  //Если это не int - ловим ошибку NumberFormatException и отправляем свой вывод
                        System.out.println("Это не номер карты!");
                        break;
                    }

                    System.out.println("Введите дату проведения операции(строго дд:мм:гг): ");
                    String dateFirst = bf.readLine();
                    try {
                        SimpleDateFormat date = new SimpleDateFormat("dd:MM:yyyy");  //Задаём формат даты
                        date.parse(dateFirst);  //Парсим входную строку на наличие даты в, установленном ранее, формате
                    } catch (ParseException ex) {
                        try {
                            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
                            date.parse(dateFirst);
                        } catch (ParseException exc) {
                            try {
                                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                                date.parse(dateFirst);
                            } catch (ParseException excep) {
                                System.out.println("Это не дата! Вводить строго дд:мм:гггг!");
                                break;
                            }
                        }
                    }

                    System.out.println("Введите место проведения операции: ");
                    String shop = bf.readLine();
                    if (shop == null) {  //Если нажать просто Enter после вызова BufferedReader.readLine(), переменная примет значение null т.к. ничего не было введено. Отсеиваем такие случаи
                        System.out.println("Вы не ввели место проведения операции!");
                        break;
                    }

                    System.out.println("Введите сумму операции: ");
                    try {
                        summ = Double.parseDouble(bf.readLine());  //Парсим строку на наличие double
                    } catch (NumberFormatException ex) {
                        System.out.println("Это не сумма операции!");
                        break;
                    }

                    System.out.println("Введите город проведения операции: ");
                    String sity = bf.readLine();
                    if (sity == null) {
                        System.out.println("Вы не ввели город проведения операции!");
                        break;
                    }
                    writeData(card, dateFirst, shop, summ, sity, type);  //Производим запись в файл, посредством вызова кастомного метода, который сам уже всё и записывает
                    System.out.println("Запись была создана.");
                    break;
                case "get":
                    String cmd;
                    if (commands.length < 2) {  //Если аргументов меньше 2-х, выводим сообщение и просим ввести второй аргумент
                        System.out.println("Введите тип операции. 1(+) - зачисление, 2(-) - списание, all - все значения, card - вывод данных о определённой карте");
                        byte stop = 0;  //Требуется для корректного выхода в "главное меню"
                        switch (cmd = bf.readLine()) {
                            case "1":
                            case "2":
                            case "+":
                            case "-":
                            case "card":
                            case "all":
                                break;
                            default:
                                System.out.println("Это не тип операции!");
                                stop = 1;  //Требуется для корректного выхода в "главное меню"
                                break;
                        }
                        if (stop == 1) {  //Требуется для корректного выхода в "главное меню"
                            break;
                        }
                    } else {
                        cmd = commands[1];  //Записываем 2 значение массива, с индексом 1, в переменную "cmd"
                    }
                    switch (cmd) {
                        case "card":
                            System.out.println("Введите номер карты: ");
                            try {
                                card = Integer.parseInt(bf.readLine());  //Парсим строку на наличие int и отсеиваем другой ввод
                            } catch (NumberFormatException ex) {
                                System.out.println("Это не номер карты!");
                                break;
                            }
                            if (commands.length < 3) {  //Теперь нам требуется 3-й аргумент
                                System.out.println("Введите тип операции. 1(+) - зачисление, 2(-) - списание, all - все значения, card - вывод данных о определённой карте");
                                byte stop = 0;
                                switch (cmd = bf.readLine()) {
                                    case "1":
                                    case "2":
                                    case "+":
                                    case "-":
                                    case "card":
                                    case "all":
                                        break;
                                    default:
                                        System.out.println("Это не тип операции!");
                                        stop = 1;
                                        break;
                                }
                                if (stop == 1) {
                                    break;
                                }
                            } else {
                                cmd = commands[2];
                            }
                            switch (cmd) {
                                case "all":
                                    printAllWithCard(card);  //Вызываем метод, который по номеру карты выведет все значения в консоль
                                    break;
                                case "+":
                                case "1":
                                    List<String> lines = Files.readAllLines(Paths.get(file));  //Получаем все строки из файла
                                    long count = countCard(5, card, "0");  //Получаем выборку по типу и номеру карты
                                    System.out.println("Всего операций зачисления: " + count);
                                    if (count != 0) {  //Отсеиваем пустые ответы
                                        System.out.println("карта:дата:место:сумма:город");
                                        lines.stream().filter((s1) -> s1.split(":")[0].equals("" + card)).filter((s1) -> s1.endsWith("0")).forEach((s1) -> System.out.println(s1.substring(0, s1.length() - 2)));  //Первая выборка - выборка по картам. Вторая - выборка по типу. Далее идёт вывод сообщения с обрезанным типом
                                    }
                                    break;
                                case "-":
                                case "2":
                                    lines = Files.readAllLines(Paths.get(file));  //Получаем все строки из файла
                                    count = countCard(5, card, "1");  //Получаем выборку по типу и номеру карты
                                    System.out.println("Всего операций списания: " + count);
                                    if (count != 0) { //Отсеиваем пустые ответы
                                        System.out.println("карта:дата:место:сумма:город");
                                        lines.stream().filter((s1) -> s1.split(":")[0].equals("" + card)).filter((s1) -> s1.endsWith("1")).forEach((s1) -> System.out.println(s1.substring(0, s1.length() - 2)));  //Первая выборка - выборка по картам. Вторая - выборка по типу. Далее идёт вывод сообщения с обрезанным типом
                                    }
                                    break;
                                default:
                                    System.out.println("Это не тип операции!");
                                    break;
                            }
                            break;
                        case "all":
                            printAll();  //Выводим все записи
                            break;
                        case "+":
                        case "1":
                            List<String> lines = Files.readAllLines(Paths.get(file));  //Получаем все строки из файла
                            long count = count(5, "0");  //Получаем выборку по типу
                            System.out.println("Всего операций зачисления: " + count);
                            if (count != 0) {
                                System.out.println("карта:дата:место:сумма:город");
                                lines.stream().filter((s1) -> s1.endsWith("0")).forEach((s1) -> System.out.println(s1.substring(0, s1.length() - 2)));  //Выборка по типу и вывод сообщения с обрезанным типом
                            }
                            break;
                        case "-":
                        case "2":
                            lines = Files.readAllLines(Paths.get(file));
                            count = count(5, "1");
                            System.out.println("Всего операций списания: " + count);
                            if (count != 0) {
                                System.out.println("карта:дата:место:сумма:город");
                                lines.stream().filter((s1) -> s1.endsWith("1")).forEach((s1) -> System.out.println(s1.substring(0, s1.length() - 2)));  //Выборка по типу и вывод сообщения с обрезанным типом
                            }
                            break;
                        default:
                            System.out.println("Это не тип операции!");
                            break;
                    }
                    break;
                case "del":
                case "delete":
                case "rem":
                case "remove":
                    System.out.println("Прежде чем удалять запомните номер нужной записи используя команду get");
                    System.out.println("Введите номер записи: ");
                    int record;
                    try {
                        record = Integer.parseInt(bf.readLine());  //Парсим входную перменную на наличие int
                    } catch (NumberFormatException ex) {
                        System.out.println("Это не номер записи!");
                        break;
                    }
                    deleteData(record);  //Удаляем нужную нам запись
                    System.out.println("Запись была удалена.");
                    break;
                case "exportFile":
                case "exportfile":
                    System.out.println("Введите название файла экспорта: ");
                    str = bf.readLine();
                    if (str == null) {
                        System.out.println("Название файла экспорта не было введено!");
                        break;
                    }
                    exportFile = str;
                    System.out.println("Файл экспорта был установлен на " + exportFile);
                    break;
                case "export":
                    if (exportFile == null) {
                        System.out.println("Сначала установите файл командой exportFile!");
                        break;
                    } else {
                        System.out.println("Экспорт будет произведён в файл " + exportFile);
                    }
                    if (commands.length < 2) {  //Если аргументов меньше 2-х, выводим сообщение и просим ввести второй аргумент
                        System.out.println("Введите тип операции. 1(+) - зачисление, 2(-) - списание, all - все значения, card - вывод данных о определённой карте");
                        byte stop = 0;  //Требуется для корректного выхода в "главное меню"
                        switch (cmd = bf.readLine()) {
                            case "1":
                            case "2":
                            case "+":
                            case "-":
                            case "card":
                            case "all":
                                break;
                            default:
                                System.out.println("Это не тип операции!");
                                stop = 1;  //Требуется для корректного выхода в "главное меню"
                                break;
                        }
                        if (stop == 1) {  //Требуется для корректного выхода в "главное меню"
                            break;
                        }
                    } else {
                        cmd = commands[1];  //Записываем 2 значение массива, с индексом 1, в переменную "cmd"
                    }
                    switch (cmd) {
                        case "card":
                            System.out.println("Введите номер карты: ");
                            try {
                                card = Integer.parseInt(bf.readLine());  //Парсим строку на наличие int и отсеиваем другой ввод
                            } catch (NumberFormatException ex) {
                                System.out.println("Это не номер карты!");
                                break;
                            }
                            if (commands.length < 3) {  //Теперь нам требуется 3-й аргумент
                                System.out.println("Введите тип операции. 1(+) - зачисление, 2(-) - списание, all - все значения, card - вывод данных о определённой карте");
                                byte stop = 0;
                                switch (cmd = bf.readLine()) {
                                    case "1":
                                    case "2":
                                    case "+":
                                    case "-":
                                    case "card":
                                    case "all":
                                        break;
                                    default:
                                        System.out.println("Это не тип операции!");
                                        stop = 1;
                                        break;
                                }
                                if (stop == 1) {
                                    break;
                                }
                            } else {
                                cmd = commands[2];
                            }
                            switch (cmd) {
                                case "all":
                                    exportAllCard(card);  //Вызываем метод, который по номеру карты экспортирует всё в отдельный файл
                                    System.out.println("Экспорт был успешно произведён!");
                                    break;
                                case "+":
                                case "1":
                                    long count = countCard(5, card, "0");  //Получаем выборку по типу и номеру карты
                                    if (count != 0) {
                                        exportFilterCard(5, "0", card, count);  //Фильтруем и экспортируем всё в отдельный файл
                                        System.out.println("Экспорт был успешно произведён!");
                                    } else {
                                        System.out.println("Записей нет. Экспортировать нечего");
                                    }
                                    break;
                                case "-":
                                case "2":
                                    count = countCard(5, card, "1");  //Получаем выборку по типу и номеру карты
                                    if (count != 0) {
                                        exportFilterCard(5, "1", card, count);  //Фильтруем и экспортируем всё в отдельный файл
                                        System.out.println("Экспорт был успешно произведён!");
                                    } else {
                                        System.out.println("Записей нет. Экспортировать нечего");
                                    }
                                    break;
                                default:
                                    System.out.println("Это не тип операции!");
                                    break;
                            }
                            break;
                        case "all":
                            exportAll();  //Выводим все записи
                            break;
                        case "+":
                        case "1":
                            long count = count(5, "0");  //Получаем выборку по типу
                            if (count != 0) {
                                exportFilter(5, "0", count);  //Фильтруем и экспортируем всё в отдельный файл
                                System.out.println("Экспорт был успешно произведён!");
                            } else {
                                System.out.println("Записей нет. Экспортировать нечего");
                            }
                            break;
                        case "-":
                        case "2":
                            count = count(5, "1");
                            if (count != 0) {
                                exportFilter(5, "1", count);  //Фильтруем и экспортируем всё в отдельный файл
                                System.out.println("Экспорт был успешно произведён!");
                            } else {
                                System.out.println("Записей нет. Экспортировать нечего");
                            }
                            break;
                        default:
                            System.out.println("Это не тип операции!");
                            break;
                    }
                    break;
                case "stop":
                case "end":
                    System.out.println("Останавливаю систему...");
                    return;  //Останавливаем исполнение бесконечного цикла, соответственно, останавливается и программа
                default:
                    System.out.println("Введите команду! set(create), get, del(delete,rem,remove), stop(end)");
                    break;
            }
        }
    }

    private static void printAll() throws IOException {  //Метод вывода всех записей
        List<String> lines = Files.readAllLines(Paths.get(file));  //Читаем все строки в файле
        System.out.println("Всего операций: " + lines.size());  //Получаем количество строк в списке
        long count0 = count(5, "0");  //Получаем количество операций зачисления
        long count1 = count(5, "1");  //Получаем количество операций списания
        lines.stream().filter((s1) -> s1.endsWith(":0")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
        System.out.println("Всего операций зачисления: " + count0);
        System.out.println("Сумма всех операций зачисления: " + summ);
        summ = 0;  //Очищаем переменную для корректного повторного использования
        lines.stream().filter((s1) -> s1.endsWith(":1")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
        System.out.println("Всего операций списания: " + count1);
        System.out.println("Сумма всех операций списания: " + summ);
        summ = 0;  //Очищаем переменную для корректного повторного использования
        if (count0 == 0 && count1 == 0) { //Если записей вообще нет, то ничего не делаем
            return;
        }
        System.out.println("запись - карта:дата:место:сумма:город:тип");
        lines.stream().forEach((s) -> {  //С помощью StreamAPI пускаем анонимный цикл
            if (s.endsWith(":0")) {
                System.out.println((i++) + " - " + s.substring(0, s.length() - 1) + "зачисление"); //Убираем последний символ(тип) и добавляем слово "списание"
            } else if (s.endsWith(":1")) {
                System.out.println((i++) + " - " + s.substring(0, s.length() - 1) + "списание"); //Убираем последний символ(тип) и добавляем слово "зачисление"
            }
        });
        i = 1;  //Очищаем переменную для корректного повторного использования
    }

    private static void printAllWithCard(int card) throws IOException {  //Метод вывода всех записей одной карты
        List<String> lines = Files.readAllLines(Paths.get(file));  //Читаем все строки в файле
        lines.removeIf((s) -> !s.split(":")[0].equals("" + card));  //Удаляем все строки, которые не равны номеру карты
        System.out.println("Всего операций: " + lines.size());
        long count0 = countCard(5, card, "0");  //Получаем количество записей с определённым номером карты и определённым типом
        long count1 = countCard(5, card, "1");
        lines.stream().filter((s1) -> s1.endsWith(":0")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
        System.out.println("Всего операций зачисления: " + count0);
        System.out.println("Сумма всех операций зачисления: " + summ);
        summ = 0;  //Очищаем переменную для корректного повторного использования
        lines.stream().filter((s1) -> s1.endsWith(":1")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
        System.out.println("Всего операций списания: " + count1);
        System.out.println("Сумма всех операций списания: " + summ);
        summ = 0;  //Очищаем переменную для корректного повторного использования
        if (count0 == 0 && count1 == 0) {
            return;
        }
        System.out.println("запись - карта:дата:место:сумма:город:тип");
        lines.stream().forEach((s) -> {
            if (s.endsWith(":0")) {
                System.out.println((i++) + " - " + s.substring(0, s.length() - 1) + "зачисление");
            } else if (s.endsWith(":1")) {
                System.out.println((i++) + " - " + s.substring(0, s.length() - 1) + "списание");
            }
        });
        i = 1;  //Очищаем переменную для корректного повторного использования
    }

    private static long count(int index, String pattern) throws IOException {  //Метод, возвращающий количество записей с определённым значением стоящим в массиве под номером index и равным переменной pattern
        return Files.readAllLines(Paths.get(file)).stream().filter((s) -> s.split(":")[index].equals(pattern)).count();  //Читаем все строки в файле, фильтруем по совпадению значения из массива с соответсвующим индексом и сравниваем его с паттерном, указанным в переменной
    }

    private static long countCard(int index, int card, String pattern) throws IOException {  //Метод, возвращающий количество записей с определённым значением стоящим в массиве под номером index и равным переменной pattern + выборка по карте
        return Files.readAllLines(Paths.get(file)).stream().filter((s) -> s.split(":")[0].equals("" + card)).filter((s) -> s.split(":")[index].equals(pattern)).count();  //Читаем все строки в файле, фильтруем сначала по совпадению номера карты, а потом по совпадению значения из массива с соответсвующим индексом и сравниваем его с паттерном, указанным в переменной
    }

    private static void writeData(Object... objects) throws IOException {  //Метод записи объектов в файл
        String tempFile = file.replace(".txt", "Temp.txt");  //Меняем имя и создаём новую переменную
        try (BufferedReader reader = new BufferedReader(new FileReader(file)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {  //Открываем потоки чтения и записи в try-with-resources, который потом сам выолнит методы flush() и close()
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {  //Читаем все строки и записываем в новый временный файл
                writer.write(currentLine + "\n");  //Пишем строки в файл, включая сепаратор \n, который переносит курсор на новую строку
            }
            boolean one = true;
            for (Object obj : objects) {  //Перебираем, собираем в одно целое и записываем некое множество разных объектов и пишем их как единый String
                String str = "" + obj;
                if (!one) {
                    str = ':' + str;
                } else {
                    one = false;
                }
                writer.write(str);
            }
            writer.write("\n");  //Создаём новую (пустую) строку
        }
        File f = new File(file);  //Открываем файл
        f.delete();  //Удаляем файл
        new File(tempFile).renameTo(f);  //Переименовываем временный файл в постоянный
    }

    private static void deleteData(int record) throws IOException {  //Метод удаления записи в файле по номеру (строки)
        String tempFile = file.replace(".txt", "Temp.txt");    //Меняем имя и создаём новую переменную
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {  //Открываем поток записи в try-with-resources, который потом сам выолнит методы flush() и close()
            Files.readAllLines(Paths.get(file)).stream().filter((s) -> (i++) != record).forEach((s1) -> {  //Читаем все строки, фильтруем по номерам, а точнее исключаем из всех номеров один единственный - номер удаляемой строки и записываем в новый временный файл
                try {
                    writer.write(s1 + "\n");  //Пишем строки в файл, включая сепаратор \n, который переносит курсор на новую строку
                } catch (IOException ex) {
                }
            });
            i = 1;  //Очищаем переменную для корректного повторного использования
        }
        File f = new File(file);  //Открываем файл
        f.delete();  //Удаляем файл
        new File(tempFile).renameTo(f);  //Переименовываем временный файл в постоянный
    }

    private static void exportAllCard(int card) throws IOException {  //Метод удаления записи в файле по номеру (строки)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {  //Открываем поток записи в try-with-resources, который потом сам выолнит методы flush() и close()
            List<String> lines = Files.readAllLines(Paths.get(file));  //Читаем все строки в файле
            lines.removeIf((s) -> !s.split(":")[0].equals("" + card));  //Удаляем все строки, которые не равны номеру карты
            writer.write("Всего операций: " + lines.size() + "\n");
            long count0 = countCard(5, card, "0");  //Получаем количество записей с определённым номером карты и определённым типом
            long count1 = countCard(5, card, "1");
            lines.stream().filter((s1) -> s1.endsWith(":0")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
            writer.write("Всего операций зачисления: " + count0 + "\n");
            writer.write("Сумма всех операций зачисления: " + summ + "\n");
            summ = 0;  //Очищаем переменную для корректного повторного использования
            lines.stream().filter((s1) -> s1.endsWith(":1")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
            writer.write("Всего операций списания: " + count1 + "\n");
            writer.write("Сумма всех операций списания: " + summ + "\n");
            summ = 0;  //Очищаем переменную для корректного повторного использования
            if (count0 == 0 && count1 == 0) {
                return;
            }
            writer.write("запись - карта:дата:место:сумма:город:тип" + "\n");
            lines.stream().forEach((s) -> {
                try {
                    if (s.endsWith(":0")) {
                        writer.write((i++) + " - " + s.substring(0, s.length() - 1) + "зачисление" + "\n"); //Убираем последний символ(тип) и добавляем слово "списание"
                    } else if (s.endsWith(":1")) {
                        writer.write((i++) + " - " + s.substring(0, s.length() - 1) + "списание" + "\n"); //Убираем последний символ(тип) и добавляем слово "зачисление"
                    }
                } catch (IOException ex) {
                }
            });
            i = 1;  //Очищаем переменную для корректного повторного использования
        }
    }

    private static void exportAll() throws IOException {  //Метод удаления записи в файле по номеру (строки)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {  //Открываем поток записи в try-with-resources, который потом сам выолнит методы flush() и close()
            List<String> lines = Files.readAllLines(Paths.get(file));  //Читаем все строки в файле
            writer.write("Всего операций: " + lines.size() + "\n");  //Получаем количество строк в списке
            long count0 = count(5, "0");  //Получаем количество операций зачисления
            long count1 = count(5, "1");  //Получаем количество операций списания
            lines.stream().filter((s1) -> s1.endsWith(":0")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
            writer.write("Всего операций зачисления: " + count0 + "\n");
            writer.write("Сумма всех операций зачисления: " + summ + "\n");
            summ = 0;  //Очищаем переменную для корректного повторного использования
            lines.stream().filter((s1) -> s1.endsWith(":1")).map((s1) -> s1 = s1.split(":")[3]).forEach((s1) -> summ = summ + Double.parseDouble(s1));  //Фильтруем записи по их типу -> полную запись заменяем на выборку суммы -> парсим и складываем суммы записей
            writer.write("Всего операций списания: " + count1 + "\n");
            writer.write("Сумма всех операций списания: " + summ + "\n");
            summ = 0;  //Очищаем переменную для корректного повторного использования
            if (count0 == 0 && count1 == 0) { //Если записей вообще нет, то ничего не делаем
                return;
            }
            writer.write("запись - карта:дата:место:сумма:город:тип" + "\n");
            lines.stream().forEach((s) -> {  //С помощью StreamAPI пускаем анонимный цикл
                try {
                    if (s.endsWith(":0")) {
                        writer.write((i++) + " - " + s.substring(0, s.length() - 1) + "зачисление" + "\n"); //Убираем последний символ(тип) и добавляем слово "списание"
                    } else if (s.endsWith(":1")) {
                        writer.write((i++) + " - " + s.substring(0, s.length() - 1) + "списание" + "\n"); //Убираем последний символ(тип) и добавляем слово "зачисление"
                    }
                } catch (IOException ex) {
                }
            });
            i = 1;  //Очищаем переменную для корректного повторного использования
        }
    }

    private static void exportFilterCard(int index, String pattern, int card, long count) throws IOException {  //Метод удаления записи в файле по номеру (строки)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {  //Открываем поток записи в try-with-resources, который потом сам выолнит методы flush() и close()
            if (pattern.equals("0")) {
                writer.write("Всего операций зачисления: " + count + "\n");
            } else if (pattern.equals("1")) {
                writer.write("Всего операций списания: " + count + "\n");
            }
            Files.readAllLines(Paths.get(file)).stream().filter((s1) -> s1.split(":")[index].equals("" + card)).filter((s1) -> s1.endsWith(pattern)).forEach((s1) -> {  //Читаем все строки, фильтруем по номеру карты и записываем в новый файл
                try {
                    writer.write(s1 + "\n");  //Пишем строки в файл, включая сепаратор \n, который переносит курсор на новую строку
                } catch (IOException ex) {
                }
            });
            i = 1;  //Очищаем переменную для корректного повторного использования
        }
    }

    private static void exportFilter(int index, String pattern, long count) throws IOException {  //Метод удаления записи в файле по номеру (строки)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {  //Открываем поток записи в try-with-resources, который потом сам выолнит методы flush() и close()
            if (pattern.equals("0")) {
                writer.write("Всего операций зачисления: " + count + "\n");
            } else if (pattern.equals("1")) {
                writer.write("Всего операций списания: " + count + "\n");
            }
            Files.readAllLines(Paths.get(file)).stream().filter((s1) -> s1.split(":")[index].equals(pattern)).forEach((s1) -> {  //Читаем все строки, фильтруем по номеру карты и записываем в новый файл
                try {
                    writer.write(s1 + "\n");  //Пишем строки в файл, включая сепаратор \n, который переносит курсор на новую строку
                } catch (IOException ex) {
                }
            });
            i = 1;  //Очищаем переменную для корректного повторного использования
        }
    }

}














//  Можно реализовать сервер для хранения в файле движений, и 2 класса - касса и оператор. Через кассу платят, а через оператора получают отчеты


//Можно реализовать сервер для хранения в файле движений, и 2 класса - касса и оператор. Через кассу платят, а через оператора получают отчеты
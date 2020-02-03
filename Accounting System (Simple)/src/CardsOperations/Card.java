//Система учета операций по картам
package CardsOperations;

import java.util.Scanner;
import java.io.*;

//Номер карты, количество операций по карте, дата операции, место проведения операции, сумма операции
public class Card {
    private String numbCard;
    private ListCard[] list_card;

    public Card() {
    }

    public void setNumbCard(String numbCard) {
        this.numbCard = numbCard;
    }

    class ListCard {
        private String date;
        private String place;
        private double sum;

        public ListCard(String date, String place, double sum) {
            this.date = date;
            this.place = place;
            this.sum = sum;
        }

        public String getDate() {
            return this.date;
        }

        public String getPlace() {
            return this.place;
        }

        public double getSum() {
            return this.sum;
        }
    }

    //ввод данных пользователем
    public void setList() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите количество операций:");
        int k = sc.nextInt();
        list_card = new ListCard[k];
        for (int i = 0; i < list_card.length; i++) {
            System.out.println("Дата " + (i + 1) + "-й операции: ");
            sc.nextLine();
            String date;
            date = sc.nextLine();
            System.out.println("Место проведения операции?");
            String place = sc.nextLine();
            System.out.println("Сумма операции:");
            double sum = sc.nextDouble();
            list_card[i] = new ListCard(date, place, sum);
        }
    }

    //вывод в консоль
    public void printAll() {
        if (list_card.length != 0)
            System.out.println("Номер карты: " + this.numbCard + "\nКоличество операций по карте: " + list_card.length);
        for (int i = 0; i < list_card.length; i++) {
            System.out.print(" Дата " + (i + 1) + "-й операции: " + this.list_card[i].getDate() + "; ");
            System.out.print(" Место проведения операции: " + this.list_card[i].getPlace() + "; ");
            System.out.println(" Сумма операции: " + this.list_card[i].getSum() + "; ");
            String source = "Номер карты: " + this.numbCard + "\n" + " Дата " + (i + 1) + "-й операции: " + this.list_card[i].getDate() + "; " + "Место проведения " + (i + 1) + "-й операции: " + this.list_card[i].getPlace() + "; " + "Сумма " + (i + 1) + "-й операции: " + this.list_card[i].getSum() + ";\n";

            //Запись в файл

            File myFile = new File("C:\\Users\\asgri\\IdeaProjects\\myPrjct\\Cards.txt");

            /*System.out.println("Удалаяем файлы если они уже были созданы.");
            if (myFile.exists()) {
                myFile.delete();
            }

            File fp = new File("C:\\Users\\asgri\\IdeaProjects\\myPrjct");
            System.out.println("Создаем директорию C:\\Users\\asgri\\IdeaProjects\\myPrjct");
            System.out.println("Проверяем: ");
            fp.mkdirs();
            if (fp.exists()) {
                System.out.println("Каталог " + fp.getName() + " существует.");
            }*/
            try {
                FileWriter f2 = new FileWriter("C:\\Users\\asgri\\IdeaProjects\\myPrjct\\Cards.txt", true);
                f2.write(source);
                f2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Выполнено успешно.");
        }
        if (list_card.length == 0)
            System.out.println("Ошибка! Выполните все сначала!");









       /* вывод всех операций из файла
       try(FileReader reader = new FileReader("Cards.txt"))
        {
         // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
*/
    }
}
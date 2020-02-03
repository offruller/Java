package CardsOperations;

import CardsOperations.Card;

import java.util.*;

public class ListCard {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите номер карты: ");
        String numb = sc.nextLine();
        Card card = new Card();
        card.setNumbCard(numb);
        card.setList();
        card.printAll();
    }
}
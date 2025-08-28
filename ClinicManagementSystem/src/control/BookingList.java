package control;

import adt.MyLinkedList;
import entity.Consultation;

public class BookingList {

    // This is the one shared list for the whole program
    public static MyLinkedList<Consultation> bookingList = new MyLinkedList<>();

}

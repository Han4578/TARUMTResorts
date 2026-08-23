/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author chao_
 */

import adt.ArrayList;
import adt.DictionaryInterface;
import adt.HashedDictionary;
import adt.ListInterface;
import adt.SortedArrayList;
import adt.SortedListInterface;
import boundary.FrontDeskBoundary;
import dao.CheckInRepository;
import dao.ReservationRepository;
import dao.RoomRepository;
import entity.Bill;
import entity.CheckIn;
import entity.Item;
import entity.Order;
import entity.OrderedItem;
import entity.Reservation;
import entity.Room;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import tarumtresorts.TARUMTResorts;
import utility.Input;


public class FrontDeskControl {
    
    private final CheckInRepository checkInRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    
    private final FrontDeskBoundary frontDeskUI = new FrontDeskBoundary();
            
    private final ListInterface<Item> itemList = new ArrayList<>();
    private ListInterface<CheckIn> checkInList = new ArrayList<>();
    private DictionaryInterface<String, Room> roomTable = new HashedDictionary<>();
    private DictionaryInterface<String, Reservation> reservationTable = new HashedDictionary<>();
    private DictionaryInterface<Integer, CheckIn> checkInTable = new HashedDictionary<>();
    private DictionaryInterface<String, Order> orderTable = new HashedDictionary<>();
    DateTimeFormatter DateTimeformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public FrontDeskControl(CheckInRepository checkInRepository, ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.checkInRepository = checkInRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        readItemFile(itemList, "item.txt");
    }
    
    //guest management
    public void frontDeskMainMenu() {
        while (true) {
            int option = Input.getIntInput(frontDeskUI.frontDeskMainMenuUI(), 1, 7);
            switch (option) {
                case 1:
                    checkIn();
                    break;
                    
                case 2:
                case 3: 
                case 4:
                    getRoomNumber(option);
                    break;
                    
                case 5:
                    showAvailableRoom();
                    break;
                
                case 6:
                    reportManagement();
                    break;
                    
                case 7:
                    return;
            }
        }
    }
    
    //Check in
    //----------------------------------------------------------------
    public void checkIn() {
            
        String confirmNo = Input.getStringInput(frontDeskUI.getConfirmNoUI());
        
        String formatConfirmNo = confirmNo.trim();
        roomTable = reservationRepository.getRoomTable();
        reservationTable = reservationRepository.getReservationTable();
        
        if (reservationTable.containsKey(formatConfirmNo)) {
            Reservation reservation = reservationTable.get(formatConfirmNo);
            Room room = roomTable.get(confirmNo);
            
            frontDeskUI.showReservationInfo(reservation.getCustomer().getName(), 
                                            reservation.getCustomer().getPhoneNumber(), 
                                            reservation.getCustomer().getEmail(),
                                            room.getRoomNumber(),
                                            room.getStatus(),
                                            reservation.getStartDate(),
                                            reservation.getEndDate()
                                            );
            
            LocalDate today = LocalDate.now();
            
            if (reservation.getStartDate().isAfter(today) || reservation.getStartDate().isBefore(today)) {
                frontDeskUI.errorCheckInDateMessage();
            } else {
                if (room.getStatus().equals("Ready for Check-In")) {
                    Input.getBooleanInput(frontDeskUI.roomReadyMessage());
                    
                    Bill bill = new Bill();
                    
                    long numberOfNights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
                    bill.setRoomPrice(numberOfNights);
                    
                    CheckIn checkin = new CheckIn(reservation, room, bill);
                    
                    reservation.setCheckIn(true);

                    checkInRepository.addToCheckInTable(checkin.getRoom().getRoomNumber(), checkin);
                    checkInRepository.addToCheckInList(checkin);
                    checkInTable = checkInRepository.getCheckInTable();

                    TARUMTResorts.save();

                    frontDeskUI.successCheckInMessage(); 
                } else {
                    frontDeskUI.errorRoomStatusMessage(room.getStatus());
                }
            }
        } else {
            frontDeskUI.notValidConfirmNoMessage();
        }
    }
    
    // Room Service, Check Bill, CheckOut
    //-----------------------------------------------------------------
    
    public void getRoomNumber(int option) {
        frontDeskUI.showCheckInRoom(getCheckedInRoom());
        int roomNo = Input.getIntInput(frontDeskUI.getRoomNumberPrompt());
        checkInTable = checkInRepository.getCheckInTable();
        if (checkInTable.containsKey(roomNo)) { 
        
            switch(option) {
                case 2:
                    roomService(roomNo);
                    break;
                    
                case 3:
                    getBillDetails(roomNo);
                    break;
                    
                case 4:
                    checkOut(roomNo);
                    break;
            }
        } else {
            frontDeskUI.invalidRoomNoMessage();
        }
    }
    
    public String getCheckedInRoom() {
        checkInList = checkInRepository.getCheckInList();
        String checkInListStr = "";
        for (int i = 0; i < checkInList.size(); i++) {
            if (checkInList.get(i).isCheckout() == false) {
                checkInListStr += String.format("%-13d %s\n",
                            checkInList.get(i).getRoom().getRoomNumber(),
                            checkInList.get(i).getReservation().getCustomer().getName());
            }
        }
        return checkInListStr;
    }
    
    // Room Service
    //----------------------------------------------------------------
    public void roomService(int roomNo) {
        while(true) {
            int option = Input.getIntInput(frontDeskUI.roomServiceUI(), 1, 3);

            switch (option) {
                case 1:
                    addOrder(roomNo);
                    break;

                case 2:
                    cancelOrder(roomNo);
                    break;

                case 3:
                    return;
            }   
        }
    }
    
    public String getItemList() {
        String itemListStr = "";
        for(int i = 0; i < itemList.size(); i++) {
            itemListStr += String.format("%-2d %-20s\n",
                                        i + 1, 
                                    itemList.get(i).getItemName());
                                    
            }
        return itemListStr;
    }
    
    public void addOrder(int roomNo) {
        SortedListInterface<OrderedItem> orderedItemList = new SortedArrayList<>();
        boolean yesNo;
        int option = 0;
        do {
            do {
                int choice = Input.getIntInput(frontDeskUI.showItemList(getItemList()), 1, itemList.size());
                int quantity = Input.getIntInput(frontDeskUI.getQuantity());

                OrderedItem orderedItem = new OrderedItem(
                                                itemList.get(choice - 1).getItemID(),
                                                itemList.get(choice - 1).getItemName(),
                                                itemList.get(choice - 1).getPrice(),
                                                quantity);

                if (orderedItemList.isEmpty()) {
                    orderedItemList.add(orderedItem);
                } else {
                    if (orderedItemList.contains(orderedItem)) {
                        int index = orderedItemList.indexOf(orderedItem);
                        OrderedItem existingItem = orderedItemList.get(index);
                        existingItem.addOrderQty(orderedItem.getOrderQty());
                    } else {
                        orderedItemList.add(orderedItem);
                    }
                }

                yesNo = Input.getBooleanInput(frontDeskUI.addOrderMessage());
            } while (yesNo);
            
            option = 0;
            while(option != 1 && option != 3) {
                frontDeskUI.showOrderedItemList(getOrderedItemList(orderedItemList));
                option = Input.getIntInput(frontDeskUI.getConfirmOrderOption(), 1, 3);
                switch (option) {
                    case 1:
                        break;
                    case 2:
                        editOrder(orderedItemList);
                        break;
                    case 3: 
                        break;
                }
            }
        } while(option == 3);
        
        
        frontDeskUI.showOrderDetails(getOrderDetails(orderedItemList));
        
        yesNo = Input.getBooleanInput(frontDeskUI.sendOrderMessage());
        
        if(yesNo) {
            Order order = new Order(orderedItemList);
            checkInRepository.addToOrderTable(order.getOrderID(), order);
            checkInRepository.addToOrderList(order);
            
            
            checkInTable = checkInRepository.getCheckInTable();
            Bill bill = checkInTable.get(roomNo).getBill();
            bill.getOrderList().add(order);
            
            TARUMTResorts.save();
            
            frontDeskUI.successOrderMessage();
            
        } else {
            frontDeskUI.notSuccessOrderMessage();
        }
    }
    
    public void editOrder(SortedListInterface<OrderedItem> orderedItemList) {
        int option = 0;
        while (option != 3) {
            frontDeskUI.editUI(getOrderedItemList(orderedItemList));
            if (orderedItemList.isEmpty()) {
            int choice = Input.getIntInput(frontDeskUI.getItemNo(), 1, orderedItemList.size());
            option = Input.getIntInput(frontDeskUI.getEditOption(), 1, 3);
                switch (option) {
                    case 1:
                        orderedItemList.remove(choice - 1);
                        frontDeskUI.removeItemSucessMessage();
                        if(orderedItemList.isEmpty()) {
                            frontDeskUI.noItemMessage();
                            return;
                        }
                        break;

                    case 2:
                        int quantity = Input.getIntInput(frontDeskUI.getNewItemQuantity());
                        OrderedItem item = orderedItemList.get(choice - 1);
                        item.setOrderQty(quantity);
                        frontDeskUI.qtyChangeSuccessMessage();
                        break;
                }
            }
        }
    }
    
    public String getOrderedItemList(SortedListInterface<OrderedItem> orderedItemList) {
        String orderedItemListStr = "";
        for(int i = 0; i < orderedItemList.size(); i++) {
            orderedItemListStr += String.format("%-3d %-20s %8d\n", 
                                                i + 1, 
                                                orderedItemList.get(i).getItemName(),
                                                orderedItemList.get(i).getOrderQty());
        }
        return orderedItemListStr;
    }
    
    public String getOrderDetails(SortedListInterface<OrderedItem> orderedItemList) {
        String orderedDetailsStr = "";
        float subTotal = 0;
        for(int i = 0; i < orderedItemList.size(); i++) {
            orderedDetailsStr += String.format("%-2d %-20s %8d %12.2f\n", 
                                                i + 1, 
                                                orderedItemList.get(i).getItemName(),
                                                orderedItemList.get(i).getOrderQty(),
                                                orderedItemList.get(i).getTotal());
            
            subTotal += orderedItemList.get(i).getTotal();
        }
        orderedDetailsStr += """
                              ---------------------------------------------
                                                        Total: """ + String.format("%-12.2f\n", subTotal);
        return orderedDetailsStr;
    }
    
    public void cancelOrder(int roomNo) {
        checkInTable = checkInRepository.getCheckInTable();
        Bill bill = checkInTable.get(roomNo).getBill();
        
        SortedListInterface<Order> orderList = bill.getOrderList();
        
        String orderListStr = "";
        ListInterface<String> orderIDList = new ArrayList<>();
        int number = 1;
        for(Order order : orderList) {
            order.updateOrderStatus();
            if(order.getStatus().equals("Pending")) {
                orderIDList.add(order.getOrderID());
                orderListStr += String.format("%-2d %-9s %s", number++, order.getOrderID(), order.getOrderTime().format(DateTimeformatter));
            }
        }
        
        if(!orderIDList.isEmpty()) {
            int choice = Input.getIntInput(frontDeskUI.showOrderList(orderListStr), 1, orderIDList.size());
            
            String chosenID = orderIDList.get(choice - 1);
            orderTable = checkInRepository.getOrderTable();
            Order order = orderTable.get(chosenID);
            order.setStatus("Cancelled");
            
            frontDeskUI.cancelOrderMessage();
        } else {
            frontDeskUI.noOrderMessage();
        }
    }
    
    // Bill Details
    //-------------------------------------------------------------------
    
    public void getBillDetails(int roomNo) {
        checkInTable = checkInRepository.getCheckInTable();
        CheckIn checkin = checkInTable.get(roomNo);
        long numberOfNights = ChronoUnit.DAYS.between(checkin.getReservation().getStartDate(), checkin.getReservation().getEndDate());
        ArrayList<String> billDetails = new ArrayList<>();
        billDetails.add(checkin.getBill().getBillID());
        billDetails.add(checkin.getReservation().getCustomer().getName());
        billDetails.add(String.valueOf(checkin.getRoom().getRoomNumber()));
        billDetails.add(String.valueOf(checkin.getReservation().getStartDate()));
        billDetails.add(String.valueOf(checkin.getReservation().getEndDate()));
        billDetails.add(String.valueOf(numberOfNights));
        billDetails.add(String.format("%.2f",checkin.getBill().getRoomPrice()));
        billDetails.add(String.format("%.2f",checkin.getBill().getOrderTotalAmount()));
        billDetails.add(String.format("%.2f",checkin.getBill().getBillAmount()));
        frontDeskUI.billDetailsUI(billDetails);
    }
    
    // Available Room
    //-------------------------------------------------------------------------
    
    public void showAvailableRoom() {
        SortedListInterface<Room> rooms = roomRepository.getRooms();
        String availableStr = "";
        for (Room room : rooms) {
            if (room.getStatus().equalsIgnoreCase("Ready for Check-In")) {
                availableStr += room.getRoomNumber() + "\n";
            }
        }
        frontDeskUI.showAvailableRoomUI(availableStr);
    }
    
    // Check Out
    //----------------------------------------------------------------------------------
    public void checkOut(int roomNo) {
        checkInTable = checkInRepository.getCheckInTable();
        CheckIn checkin = checkInTable.get(roomNo);
        frontDeskUI.checkOutUI(checkin.getReservation().getCustomer().getName(),
                               checkin.getReservation().getStartDate().format(Dateformatter),
                               checkin.getReservation().getEndDate().format(Dateformatter));
        Input.getBooleanInput(frontDeskUI.checkOutConfirmMessage());
        if(true) {
            frontDeskUI.checkOutSuccessMessage();
            checkin.getBill().setPaid(true);
            checkin.setCheckOut(true);
            checkin.getRoom().setStatus("Dirty");
        }
    }
    
    // Report
    //-----------------------------------------------------------------------
    public void reportManagement() {
        while (true) {
            int option = Input.getIntInput(frontDeskUI.reportManagementUI(), 1, 3);

            switch (option) {

                case 1:
                    salesReport();
                    break;

                case 2:
                    roomServiceOrderReport();
                    break;

                case 3:
                    return;
            }
        }
    }
    
    public void salesReport() {

        int paymentOption = Input.getIntInput(frontDeskUI.salesReportFilterUI(),1,3);

        String startDateStr = Input.getStringInput(frontDeskUI.getReportStartDatePrompt());

        String endDateStr = Input.getStringInput(   frontDeskUI.getReportEndDatePrompt() );

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateStr, Dateformatter);
            endDate = LocalDate.parse(endDateStr, Dateformatter);

        } catch (DateTimeParseException e) {
            frontDeskUI.invalidDateFormatMessage();
            return;
        }

        if (endDate.isBefore(startDate)) {
            frontDeskUI.invalidDateMessage();
            return;
        }

        float minimumSales = Input.getFloatInput(frontDeskUI.getMinimumSalesPrompt(), 0, Float.MAX_VALUE);

        int sortOption = Input.getIntInput(frontDeskUI.salesReportSortUI(), 1, 2);

        SortedListInterface<SalesReportItem> reportList = generateSalesReport(startDate, endDate, paymentOption, minimumSales, sortOption);

        displaySalesReport(reportList);
    }
    
    public SortedListInterface<SalesReportItem> generateSalesReport(LocalDate startDate, LocalDate endDate, int paymentOption, float minimumSales, int sortOption) {

        SortedListInterface<SalesReportItem> reportList = new SortedArrayList<>();

        checkInList = checkInRepository.getCheckInList();

        for (int i = 0; i < checkInList.size(); i++) {
            CheckIn checkin = checkInList.get(i);
            
            //if (!checkin.isCheckout()) {
            //    continue;
            //}

            LocalDate checkInDate = checkin.getReservation().getStartDate();

            LocalDate checkOutDate = checkin.getReservation().getEndDate();
            
            if (checkInDate.isBefore(startDate) || checkInDate.isAfter(endDate)) {
                continue;
            }

            Bill bill = checkin.getBill();
            
            if (paymentOption == 2 && !bill.isPaid()) {
                continue;
            }

            if (paymentOption == 3 && bill.isPaid()) {
                continue;
            }

            float roomSales = bill.getRoomPrice();

            float roomServiceSales = bill.getOrderTotalAmount();

            float totalSales = bill.getBillAmount();

            // Minimum sales filtering
            if (totalSales < minimumSales) {
                continue;
            }

            SalesReportItem reportItem = new SalesReportItem(
                                       bill.getBillID(),
                                    checkin.getRoom().getRoomNumber(), 
                                     checkin.getReservation().getCustomer().getName(), 
                                            checkInDate, 
                                            checkOutDate, 
                                            roomSales, 
                                            roomServiceSales, 
                                            totalSales);

            reportList.add(reportItem);
        }

        return reportList;
    }
    
    public void displaySalesReport(
        SortedListInterface<SalesReportItem> reportList) {

        String reportStr = "";

        float totalRoomSales = 0;
        float totalRoomServiceSales = 0;
        float totalSales = 0;

        for (int i = 0; i < reportList.size(); i++) {

            SalesReportItem item = reportList.get(i);

            reportStr += String.format(
                            "%-10s %-6d %-22s RM %8.2f RM %12.2f RM %10.2f%n",
                            item.billID(),
                            item.roomNumber(),
                            item.guestName(),
                            item.roomSales(),
                            item.roomServiceSales(),
                            item.totalSales());

            totalRoomSales += item.roomSales();
            totalRoomServiceSales += item.roomServiceSales();
            totalSales += item.totalSales();
        }

        float averageSales = 0;

        if (reportList.size() > 0) {
            averageSales = totalSales / reportList.size();
        }

        frontDeskUI.showSalesReport(
                reportStr,
                reportList.size(),
                totalRoomSales,
                totalRoomServiceSales,
                totalSales,
                averageSales
        );
    }
    
    public void roomServiceOrderReport() {

        int statusOption = Input.getIntInput(
                frontDeskUI.orderReportFilterUI(),
                1,
                5
        );

        String startDateStr = Input.getStringInput(
                frontDeskUI.getReportStartDatePrompt()
        );

        String endDateStr = Input.getStringInput(
                frontDeskUI.getReportEndDatePrompt()
        );

        LocalDate startDate;
        LocalDate endDate;

        try {

            startDate = LocalDate.parse(
                    startDateStr,
                    Dateformatter
            );

            endDate = LocalDate.parse(
                    endDateStr,
                    Dateformatter
            );

        } catch (DateTimeParseException e) {
            frontDeskUI.invalidDateFormatMessage();
            return;
        }

        if (endDate.isBefore(startDate)) {
            frontDeskUI.invalidDateMessage();
        }

        float minimumAmount = Input.getFloatInput(
                frontDeskUI.getMinimumOrderAmountPrompt(),
                0,
                Float.MAX_VALUE
        );

        int sortOption = Input.getIntInput(
                frontDeskUI.orderReportSortUI(),
                1,
                2
        );

        SortedListInterface<OrderReportItem> reportList = generateOrderReport(
                                                            startDate,
                                                            endDate,
                                                            statusOption,
                                                            minimumAmount,
                                                            sortOption);

        displayOrderReport(reportList);
    }
    
    public SortedListInterface<OrderReportItem> generateOrderReport(
        LocalDate startDate,
        LocalDate endDate,
        int statusOption,
        float minimumAmount,
        int sortOption) {

        SortedListInterface<OrderReportItem> reportList = new SortedArrayList<>();

        ListInterface<Order> orderList = checkInRepository.getOrderList();

        for (int i = 0; i < orderList.size(); i++) {

            Order order = orderList.get(i);
            
            order.updateOrderStatus();

            LocalDate orderDate = order.getOrderTime().toLocalDate();

            if (orderDate.isBefore(startDate) || orderDate.isAfter(endDate)) {
                continue;
            }

            String status = order.getStatus();

            if (statusOption == 2 && !status.equals("Pending")) {
                continue;
            }

            if (statusOption == 3 && !status.equals("Preparing")) {  
                continue;
            }

            if (statusOption == 4 && !status.equals("Delivered")) {            
                continue;
            }

            if (statusOption == 5 && !status.equals("Cancelled")) {             
                continue;
            }

            float totalAmount = order.getSubTotal();
            
            if (totalAmount < minimumAmount) {
                continue;
            }


            CheckIn matchingCheckIn = null;

            checkInList = checkInRepository.getCheckInList();
            
            for (int j = 0; j < checkInList.size(); j++) {

                CheckIn checkin = checkInList.get(j);
                if (checkin.getBill().getOrderList().contains(order)) {
                    
                    matchingCheckIn = checkin;
                    break;
                }
            }

            if (matchingCheckIn == null) {
                continue;
            }

            OrderReportItem reportItem;
            reportItem = new OrderReportItem(
                    order.getOrderID(),
                    matchingCheckIn.getRoom().getRoomNumber(),
                    matchingCheckIn.getReservation().getCustomer().getName(),
                    order.getOrderTime(),
                    status,
                    totalAmount);

            reportList.add(reportItem);
        }

        return reportList;
    }
    
    public void displayOrderReport(
        SortedListInterface<OrderReportItem> reportList) {

        String reportStr = "";

        int pending = 0;
        int preparing = 0;
        int delivered = 0;
        int cancelled = 0;

        float totalSales = 0;

        for (int i = 0; i < reportList.size(); i++) {

            OrderReportItem item = reportList.get(i);

            reportStr += String.format(
                        "%-10s %-6d %-22s %-20s %-12s RM %.2f%n",
                        item.orderID(),
                        item.roomNumber(),
                        item.guestName(),
                        item.orderTime().format(DateTimeformatter),
                        item.status(),
                        item.totalAmount());

            switch (item.status()) {

                case "Pending":
                    pending++;
                    break;

                case "Preparing":
                    preparing++;
                    break;

                case "Delivered":
                    delivered++;
                    totalSales += item.totalAmount();
                    break;

                case "Cancelled":
                    cancelled++;
                    break;
            }
        }

        frontDeskUI.showOrderReport(
                        reportStr,
                        reportList.size(),
                        pending,
                        preparing,
                        delivered,
                        cancelled,
                        totalSales);
    }
    
    public record SalesReportItem(String billID, int roomNumber, String guestName, LocalDate checkInDate, LocalDate checkOutDate, float roomSales, float roomServiceSales, float totalSales) implements Comparable<SalesReportItem> {

    @Override
    public int compareTo(SalesReportItem other) {
            // Highest total sales first
            return Float.compare(other.totalSales(), this.totalSales());
        }
    }
    
    public record OrderReportItem(String orderID, int roomNumber, String guestName, LocalDateTime orderTime, String status, float totalAmount) implements Comparable<OrderReportItem> {

    @Override
    public int compareTo(OrderReportItem other) {
            return other.orderTime().compareTo(this.orderTime());
        }
    }
    
    // Read file
    //------------------------------------------------------------------
    private void readItemFile(ListInterface<Item> list, String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String itemID = data[0];
                String itemName = data[1];
                float price = Float.parseFloat(data[2]);
                
                Item item = new Item(itemID, itemName, price);
                
                list.add(item);
                
            }

        } catch (IOException e) {
            frontDeskUI.errorReadFileMessage(e);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;
import java.util.Scanner;
import adt.ArrayList;
import java.io.IOException;
import java.time.LocalDate;


/**
 *
 * @author Tan Kah Chao
 */
public class FrontDeskBoundary {
    
    Scanner scanner = new Scanner(System.in);
    public FrontDeskBoundary() {
        
    }
    
    public String frontDeskMainMenuUI() {
        return 
        """
        ====== Front Desk Management ======  
        [1] Check-In 
        [2] Room Service
        [3] View Billing Details 
        [4] Check out
        [5] Show Available Room
        [6] Report Management
        [7] Return

        Enter number: \
        """;  
    }
    
    
    // CHECK IN
    //----------------------------------------------------------------
    public String getConfirmNoUI() {
        return 
        """
        Verification
        ------------
        Confirmation Number: \
        """;
    }
    
    public void showReservationInfo(String name, String phoneNo, String email, int roomNo, String roomStatus, LocalDate startDate, LocalDate endDate) {
        System.out.println("\n============== Check In ==============");
        
        System.out.println("------- Reservation -------");
        System.out.println("Guest Name  : " + name);
        System.out.println("Phone Number: " + phoneNo);
        System.out.println("Email:      : " + email);
        System.out.println();
        System.out.println("Room Number : " + roomNo);
        System.out.println("Room Status : " + roomStatus);
        System.out.println();
        System.out.println("Start Date  : " + startDate);
        System.out.println("End Date    : " + endDate);
        System.out.println();
    }
    
    public String roomReadyMessage() {
        return
        """
        
        Room is ready for check in.
        Proceed to check In? (Y/N): \
        """;
    }
    
    public void errorRoomStatusMessage(String status) {
        System.out.println("The room status: " + status + ". Unable to check in.");
        pressEnter();
    }
            
    public void successCheckInMessage() {
        System.out.println("Check in successfully.");
        pressEnter();   
    }
    
    public void errorCheckInDateMessage() {
        System.out.println("The reservation start date is not today");
        pressEnter();
    }
    
    public void notValidConfirmNoMessage() {
        System.out.println("Invalid confirmation number.");
        pressEnter();
    }
    
    // Room Service,Check Bill, CheckOut
    //----------------------------------------------------------------- 
    
    public void showCheckInRoom(String checkedInRoomStr) {
        System.out.println("-------- Checked-In Room List --------");
        System.out.println("Room number   Guest Name");
        System.out.println("-----------   -----------");
        System.out.print(checkedInRoomStr);
    }
    
    public String getRoomNumberPrompt() {
        return
        """
        Room number: \
        """;
    }
    
    public void invalidRoomNoMessage() {
        System.out.println("Invalid room number.");
        pressEnter();
    }
    
    // Room Service
    //----------------------------------------------------------------
    public String roomServiceUI() {
        return
        """
        =========== Room Service ===========
        [1] Add Order
        [2] Cancel order
        [3] Return
        
        Enter number: \
        """;
    } 
    
    public String showItemList(String itemListStr) {
        return 
        """
        ============= Add Order =============
        ------------ Product List ------------
        No  Item Name            
        --- -------------------- 
        """ + itemListStr +
        """
        Select number: \
        """;
    }
    
    public String getQuantity() {
        return "Quantity: ";
    }
    
    public String addOrderMessage() {
        return
        """
        
        Item Added.
        Add another item? (Y/N): \
        """;
    } 
    
    public void showOrderedItemList(String orderItemListStr) {
        System.out.println("------------ Ordered Item ------------");
        System.out.println("No  Item Name            Quantity");
        System.out.println("--  -------------------- --------");
        System.out.print(orderItemListStr);
        System.out.println();
    }
    
    public void editUI(String orderItemListStr) {
        System.out.println("============== Edit item ==============");
        showOrderedItemList(orderItemListStr);
    }
    
    public String getEditOption() {
        return
        """
        
        [1] Remove item
        [2] Change item quantity
        [3] Done
        
        Enter number: \
        """;
    }
    
    public void noItemMessage() {
        System.out.println("\nNo more item...return to room service page.");
        
    }
    
    public String getItemNo() {
        return "Select number: ";
    }
    
    public String getNewItemQuantity() {
        return "Enter new quantity: ";
    }
    
    public void removeItemSucessMessage() {
        System.out.println("Remove item successfully.");
        pressEnter();
    }
    
    public void qtyChangeSuccessMessage() {
        System.out.println("Item quantity changed successfully");
        pressEnter();
    }
    
    public String getConfirmOrderOption() {
        return
        """
                
        [1] Confirm Order
        [2] Edit item 
        [3] Add more item
     
        Enter number: \
        """;
    } 
    
    public String showOrderDetails(String orderedDetailsStr) {
        return 
        """
        ------------ Order Details ------------
        No  Item Name            Quantity Subtotal(RM)
        --- -------------------- -------- ------------
        """ + orderedDetailsStr;
    }
    
    public String sendOrderMessage() {
        return "\nPlace order (Y/N): ";
    }
    
    public void successOrderMessage() {
        System.out.println("Order placed successfully.");
        pressEnter();
    }
    
    public void notSuccessOrderMessage() {
        System.out.println("Order placement failed.");
        pressEnter();
    }
    
    public String showOrderList(String orderListStr) {
        return 
        """
        =========== Cancel Order ===========
        ------------ Order List ------------
        No  Order ID Order Time
        --- -------- ----------
        """ + orderListStr + 
        """
        \nEnter number: \
        """;
    }
    
    public void cancelOrderMessage() {
        System.out.println("Order cancelled successfully.");
        pressEnter();
    }
    
    public void noOrderMessage() {
        System.out.println("No active orders yet.");
        pressEnter();
    }
    
    // Bill Details
    //-----------------------------------------------------------------    
    public void billDetailsUI( ArrayList<String> billDetails) {
        System.out.println("=========== Billing Details ===========");
        System.out.println("Bill ID   : " + billDetails.get(0));
        System.out.println();
        System.out.println("Guest Name       : " + billDetails.get(1));
        System.out.println("Room Number      : " + billDetails.get(2));
        System.out.println("Check-In         : " + billDetails.get(3));
        System.out.println("Check-Out        : " + billDetails.get(4));
        System.out.println("Number Of Nights : " + billDetails.get(5));
        System.out.println();
        System.out.println("Room Charge      : RM" + billDetails.get(6));
        System.out.println("Room Service     : RM" + billDetails.get(7));
        System.out.println("--------------------------------------------------");
        System.out.println("Total            : RM" + billDetails.get(8));
        System.out.println();
        pressEnter(); 
    }
            
    // CheckOut
    //----------------------------------------------------------------
    
    public void checkOutUI(String customerName, String startDate, String endDate) {
        System.out.println("============== Check Out ==============");
        System.out.println("Guest Name: " + customerName);
        System.out.println("Check-In Date: " + startDate);
        System.out.println("Check-Out Date: " + endDate);
        System.out.println();
    }
    
    public String checkOutConfirmMessage() {
        return "Confirm guest check-out? (Y/N): ";
    }
    
    public void checkOutSuccessMessage() {
        System.out.println("Guest check-out completed.");
    }
    
    // Available Room
    //----------------------------------------------------------------------
    
    public void showAvailableRoomUI(String availableStr) {
        System.out.println("============= Available Room =============");
        System.out.println(availableStr);
        pressEnter();
    }
    
    // Report
    //--------------------------------------------------------------------
    
    public String reportManagementUI() {
        return """

                ========== Report Management ==========

                [1] Sales Report
                [2] Room Service Order Report
                [3] Return

                Enter number: \s""";
    }
    
    public String salesReportFilterUI() {
        return """

                ========== Sales Report ==========

                Payment Status
                [1] All
                [2] Paid
                [3] Unpaid

                """;
    }
    
    public String getMinimumSalesPrompt() {
        return "Minimum total sales (RM): ";
    }
    
    public String getReportStartDatePrompt() {
        return "Start date (dd-MM-yyyy): ";
    }
    
    public String getReportEndDatePrompt() {
        return "End date (dd-MM-yyyy): ";
    }
    
    public String salesReportSortUI() {
        return 
        """

        Sort by:
        [1] Highest Sales
        [2] Lowest Sales

        Enter number: \s
        """;
    }
    
    public void showSalesReport(
        String reportStr,
        int totalBills,
        float totalRoomSales,
        float totalRoomServiceSales,
        float totalSales,
        float averageSales) {

        System.out.println(
        """

        ==================== SALES REPORT ====================

        Bill ID    Room   Guest Name             Room Sales   Service Sales     Total
        --------   ----   ---------------------  -----------  ---------------   -----------
        """);

        System.out.print(reportStr);

        System.out.println("-----------------------------------------------------------------------------------");

        System.out.printf("Total Bills          : %d%n", totalBills);
        System.out.printf("Total Room Sales     : RM %.2f%n", totalRoomSales);
        System.out.printf("Total Room Service   : RM %.2f%n", totalRoomServiceSales);
        System.out.printf("Total Sales          : RM %.2f%n", totalSales);
        System.out.printf("Average Bill         : RM %.2f%n", averageSales);

        System.out.println("==============================================================");

        pressEnter();
    }
    
    public String orderReportFilterUI() {
    return 
    """
            
    ========== Room Service Order Report ==========

    Order Status
    [1] All
    [2] Pending
    [3] Preparing
    [4] Delivered
    [5] Cancelled

    Enter number: \s
    """;
    }
    
    public String orderReportSortUI() {
        return 
        """

        Sort by:
        [1] Latest Order
        [2] Highest Order Amount

        Enter number: \s
        """;
    }
    
    public String getMinimumOrderAmountPrompt() {
        return "Minimum order amount (RM): ";
    }
    
    public void showOrderReport(
        String reportStr,
        int totalOrders,
        int pending,
        int preparing,
        int delivered,
        int cancelled,
        float totalSales) {

        System.out.println(
        """

        ================= ROOM SERVICE ORDER REPORT =================

        Order ID    Room   Guest Name             Order Time            Status
        --------    ----   ---------------------  -------------------   -----------
        """);

        System.out.print(reportStr);

        System.out.println("--------------------------------------------------------------------------");

        System.out.printf("Total Orders       : %d%n", totalOrders);
        System.out.printf("Pending            : %d%n", pending);
        System.out.printf("Preparing          : %d%n", preparing);
        System.out.printf("Delivered          : %d%n", delivered);
        System.out.printf("Cancelled          : %d%n", cancelled);
        System.out.printf("Total Sales        : RM %.2f%n", totalSales);

        System.out.println("==============================================================");

        pressEnter();
    }
    
    public void invalidDateFormatMessage() {
        System.out.println("Invalid date format.");
        pressEnter();
    }
    
    public void invalidDateMessage() {
        System.out.println("End date cannot be before start date.");
        pressEnter();
    }
    //--------------------------------------------------------------------
    
    public void pressEnter() {
        System.out.println("Press \"Enter\" to proceed");
        scanner.nextLine();
        System.out.println();
    }
    
    public void errorReadFileMessage(IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
}

/*  Name:  Jerry Wang
    Course: CNT 4714 – Spring 2025
    Assignment title: Project 1 – An Event-driven Enterprise Simulation
    Date: Sunday January 26, 2025
*/ 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import javax.swing.*;

public class NileDotCom extends JFrame{
	// GUI Dimensions
    private static final int WIDTH = 900, HEIGHT = 550;

	// Labels, Text Fields, and Buttons
    private JLabel blankL, idL, quantityL, itemL, subtotalL, bTitleL, mTitleL;
    private JTextField blankTF, idTF, quantityTF, itemTF, subtotalTF, m1TF, m2TF, m3TF, m4TF, m5TF;
    private JButton searchBtn, addBtn, deleteBtn, checkOutBtn, newOrderBtn, exitBtn;
	
	// Event Handlers
    private SearchBtnH searchBtnHlr;
    private AddBtnH addBtnHlr;
    private DeleteBtnH deleteBtnHlr;
    private CheckOutBtnH checkOutBtnHlr;
    private NewOrderBtnH newOrderBtnHlr;
    private ExitBtnH exitBtnHlr;
	
	// Formatting
	static NumberFormat currencyF = NumberFormat.getCurrencyInstance();
	static NumberFormat percentF = NumberFormat.getPercentInstance();
	static DecimalFormat decimalF = (DecimalFormat) percentF;
	
	// Arrays
	static String [] idARR;
	static String[] titleArr;
	static String[] stockArr;
	static double[] priceArr;
	static int[] quantityArr;
	static double[] discountArr;
	static double[] subtotalArr;
	
	// Variables
	static String id = "", title = "", outputStr = "", maxArraySizeStr = "", price = "", stock = "", quantity = "", subtotal = "", discount = "", orderSub = "";
	static double nPrice = 0, nSubtotal = 0, nOrderSubtotal = 0, nOrderTotal = 0, nDiscount = 0, nTax = 0;
	private static int itemIdx = 0, itemQuantity = 0, maxArraySize = 0;
	
	// Constants
	final static double TAX = 0.060, DISCOUNT_OF_10 = .10, DISCOUNT_OF_15 = .15, DISCOUNT_OF_20 = .20;
	
	String fileName;

	// Constructor of NileDotCom
	public NileDotCom() {
		setTitle("Nile.Com - Spring 2025");
		setSize(WIDTH, HEIGHT);
		
		// Labels
		blankL = new JLabel(" ", SwingConstants.RIGHT);
		idL = new JLabel("Enter item ID for Item #" + (itemIdx + 1) + ":", SwingConstants.RIGHT);
		quantityL = new JLabel("Enter quantity for Item #"+ (itemIdx + 1) + ":", SwingConstants.RIGHT);
		itemL = new JLabel("Details for Item #"+ (itemIdx + 1) + ":", SwingConstants.RIGHT);
		subtotalL = new JLabel("Current Subtotal for " + itemIdx + " item(s):", SwingConstants.RIGHT);
		bTitleL = new JLabel("<html><b>USER CONTROLS</b></html>", SwingConstants.CENTER);
		mTitleL = new JLabel("<html><b>Your Shopping Cart is Currently Empty</b></hmtl>", SwingConstants.CENTER);

		// Text fields
		blankTF = new JTextField();
		idTF = new JTextField();
		quantityTF = new JTextField();
		itemTF = new JTextField();
		subtotalTF = new JTextField();
		m1TF = new JTextField(10);
		m2TF = new JTextField(10);
		m3TF = new JTextField(10);
		m4TF = new JTextField(10);
		m5TF = new JTextField(10);

		// Buttons
		searchBtn = new JButton("Search For Item #" + (itemIdx + 1));
		searchBtnHlr = new SearchBtnH();
		searchBtn.addActionListener(searchBtnHlr);
		
		addBtn = new JButton("Add Item #" + (itemIdx + 1) + " To Cart");
		addBtnHlr = new AddBtnH();
		addBtn.addActionListener(addBtnHlr);
		
		deleteBtn = new JButton("Delete Last Item Added To Cart");
		deleteBtnHlr = new DeleteBtnH();
		deleteBtn.addActionListener(deleteBtnHlr);
		
		checkOutBtn = new JButton("Check Out");
		checkOutBtnHlr = new CheckOutBtnH();
		checkOutBtn.addActionListener(checkOutBtnHlr);

		newOrderBtn = new JButton("Empty Cart - Start A New Order");
		newOrderBtnHlr = new NewOrderBtnH();
		newOrderBtn.addActionListener(newOrderBtnHlr);
		
		exitBtn = new JButton("Exit (Close App)");
		exitBtnHlr = new ExitBtnH();
		exitBtn.addActionListener(exitBtnHlr);


		// Organize panels
		Container pane = getContentPane();

		// Top panel
		GridLayout grid5by2 = new GridLayout(6,2,8,2); 
		JPanel topPanel = new JPanel();
		topPanel.setLayout(grid5by2);
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		topPanel.add(idL);
		topPanel.add(idTF);
		idL.setForeground(Color.yellow);
		
		topPanel.add(quantityL);
		topPanel.add(quantityTF);	
		quantityL.setForeground(Color.yellow);
		
		topPanel.add(itemL);
		topPanel.add(itemTF);	
		itemL.setForeground(Color.red);
		
		topPanel.add(subtotalL);
		topPanel.add(subtotalTF);	
		subtotalL.setForeground(Color.CYAN);

		// Middle panel
		GridLayout grid5by1 = new GridLayout(6,1,8,2);
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(grid5by1);
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		middlePanel.add(mTitleL);
		middlePanel.add(m1TF);
		middlePanel.add(m2TF);
		middlePanel.add(m3TF);
		middlePanel.add(m4TF);
		middlePanel.add(m5TF);

		// Bottom panel
		GridLayout grid3by3 = new GridLayout(5,2,2,2);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(grid3by3);

		bottomPanel.add(bTitleL);
		bottomPanel.add(blankL);
		bottomPanel.add(searchBtn);
		bottomPanel.add(addBtn);
		bottomPanel.add(deleteBtn);
		bottomPanel.add(checkOutBtn);
		bottomPanel.add(newOrderBtn);
		bottomPanel.add(exitBtn);
		
		// Layout
		pane.add(topPanel, BorderLayout.NORTH);
		pane.add(middlePanel,BorderLayout.CENTER);
		pane.add(bottomPanel, BorderLayout.SOUTH);

		// Visibility
		addBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		checkOutBtn.setEnabled(false);
		itemTF.setEditable(false);
		subtotalTF.setEditable(false);
		blankTF.setEditable(false);
		blankTF.setVisible(false);
		mTitleL.setForeground(Color.red);
		m1TF.setEditable(false);
		m2TF.setEditable(false);
		m3TF.setEditable(false);
		m4TF.setEditable(false);
		m5TF.setEditable(false);
		bTitleL.setForeground(Color.white);
		pane.setBackground(Color.black);
		topPanel.setBackground(Color.darkGray);
		middlePanel.setBackground(Color.LIGHT_GRAY);
		bottomPanel.setBackground(Color.blue);
	}
	
	// Search button
	private class SearchBtnH implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			String out, pd, fID;
			boolean found = false, max = false, min = false, stockFlag = true, realstock = false;
			
			File file= new File("inventory.csv");
			FileReader fileReader = null;
			BufferedReader buff = null;
			Scanner scanner = null;
			
			try {
				maxArraySize = 5;
				
				// 5 or less items in cart
				if(itemIdx <= maxArraySize) {
					max = true;
				}

				// Disable search after 5 items
				if(itemIdx == maxArraySize) {
					searchBtn.setEnabled(false);
				}

				// Positive num
				if(Integer.parseInt(quantityTF.getText()) > 0){
					min = true;
				}
				
				if(itemIdx == 0) {
					idARR = new String[maxArraySize];
					titleArr = new String[maxArraySize];
					stockArr = new String[maxArraySize];
					quantityArr = new int[maxArraySize];
					priceArr = new double[maxArraySize];
					discountArr = new double[maxArraySize];
					subtotalArr = new double[maxArraySize];
				}
				
				id = idTF.getText();
				itemQuantity = Integer.parseInt(quantityTF.getText());
				
				fileReader = new FileReader(file);
				buff = new BufferedReader(fileReader);
				pd = buff.readLine();
				
				// While loop
				while(pd != null && stockFlag) {
					scanner = new Scanner(pd);
					scanner.useDelimiter(", ");
					fID = scanner.next();
					
					if(id.equals(fID)) {
						found = true;
						title = scanner.next();
						stock = scanner.next();
						
						// If file says they're not in stock, change to that
						if(!stock.equals("true")) {
							stockFlag = false;
						}

						quantity = scanner.next();

						// Compare request vs stock
						if(itemQuantity <= Integer.parseInt(quantity))
						{
							realstock = true;
						}

						price = scanner.next();
						nPrice = Double.parseDouble(price);
						break;
					}
					pd = buff.readLine();
					
				}
				
				if(found == false || max == false || min == false || stockFlag == false || realstock == false) {
					if(found == false) {
						out = "Item ID " + id + " not in file";
						JOptionPane.showMessageDialog(null, out, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
						idTF.setText("");
						quantityTF.setText("");
					}
					
					if(max == false || min == false) {
						out = "Please enter positive numbers for number of items";
						JOptionPane.showMessageDialog(null, out, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
					}
					
					if(stockFlag == false) {
						out = "Sorry... that item is out of stock, please try another item";
						JOptionPane.showMessageDialog(null, out, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
						idTF.setText("");
						quantityTF.setText("");
					}

					if(realstock == false && stockFlag == true && found == true) {
						out = "Insufficient stock. Only " + quantity + " on hand. Please reduce the quantity.";
						JOptionPane.showMessageDialog(null, out, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
						quantityTF.setText("");
					}
					
				}
				else { // Search worked
					itemQuantity = Integer.parseInt(quantityTF.getText());
					quantity = String.valueOf(itemQuantity);
					if(itemQuantity > 0 && itemQuantity < 5) {
						nDiscount = 0;
					}
					else if(itemQuantity >= 5 && itemQuantity < 10) {
						nDiscount = DISCOUNT_OF_10;
					}
					else if(itemQuantity >= 10 && itemQuantity < 15) {
						nDiscount = DISCOUNT_OF_15;
					}
					else {
						nDiscount = DISCOUNT_OF_20;
					}
					
					// Calculate prices
					nSubtotal = (nPrice * (1 - nDiscount)) * itemQuantity;
					nOrderSubtotal += nSubtotal;
					
					// Format price
					price = currencyF.format(nPrice);
					discount = decimalF.format(nDiscount);
					subtotal = currencyF.format(nSubtotal);
					orderSub = currencyF.format(nOrderSubtotal);
					
					// Details message
					out = id + " " + title + " " + price + " "
							+ itemQuantity + " " + discount + " " + subtotal;
					
					// Update
					idL.setText("Enter item ID for Item #" + (itemIdx + 1) + ":");
					quantityL.setText("Enter quantity for Item #" + (itemIdx + 1) + ":");
					itemL.setText("Details for Item #" + (itemIdx + 1) + ":");

					itemTF.setText(out);

					searchBtn.setEnabled(false);
					addBtn.setEnabled(true);
				}
				
			} catch(NumberFormatException e) {
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}
	
	// Add Button
	private class AddBtnH implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			// Save arrays
			idARR[itemIdx] = id;
			titleArr[itemIdx] = title;
			priceArr[itemIdx] = nPrice;
			quantityArr[itemIdx] = itemQuantity;
			discountArr[itemIdx] = nDiscount;
			subtotalArr[itemIdx] = nSubtotal;
			
			// Update index
			itemIdx++;
			
			idL.setText("Enter item ID for Item #" + (itemIdx + 1) + ":");
			quantityL.setText("Enter quantity for Item #" + (itemIdx + 1) + ":");
			itemL.setText("Details for Item #" + itemIdx + ":");
			subtotalL.setText("Current Subtotal for " + itemIdx + " item(s)" + ":");
			mTitleL.setText("<html><b>Your Shopping Cart Currently Contains " + (itemIdx) + " Item(s)</b></html>");

			checkOutBtn.setEnabled(true);	
			deleteBtn.setEnabled(true);
			searchBtn.setEnabled(true);
			searchBtn.setText("Search For Item #" + (itemIdx + 1));
			addBtn.setEnabled(false);
			addBtn.setText("Add Item #" + (itemIdx + 1) + " To Cart");
			
			idTF.setText("");
			quantityTF.setText("");
			subtotalTF.setText(orderSub);
			switch(itemIdx){
				case 1:
					m1TF.setText("Item 1 - SKU: " + id+ ", Desc: " + title + ", Price Ea. " + price + ", Qty: " + itemQuantity + ", Total: " + subtotal);
					break;
				case 2:
					m2TF.setText("Item 2 - SKU: " + id+ ", Desc: " + title + ", Price Ea. " + price + ", Qty: " + itemQuantity + ", Total: " + subtotal);
					break;
				case 3:
					m3TF.setText("Item 3 - SKU: " + id+ ", Desc: " + title + ", Price Ea. " + price + ", Qty: " + itemQuantity + ", Total: " + subtotal);
					break;
				case 4:
					m4TF.setText("Item 4 - SKU: " + id+ ", Desc: " + title + ", Price Ea. " + price + ", Qty: " + itemQuantity + ", Total: " + subtotal);
					break;
				case 5:
					m5TF.setText("Item 5 - SKU: " + id+ ", Desc: " + title + ", Price Ea. " + price + ", Qty: " + itemQuantity + ", Total: " + subtotal);
					break;
			}
		}
	}
	
	// Delete button
	private class DeleteBtnH implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (itemIdx > 0) {
				// Reduce order subtotal
				nOrderSubtotal -= subtotalArr[itemIdx - 1];
	
				// Clear last added item
				idARR[itemIdx - 1] = null;
				titleArr[itemIdx - 1] = null;
				priceArr[itemIdx - 1] = 0;
				quantityArr[itemIdx - 1] = 0;
				discountArr[itemIdx - 1] = 0;
				subtotalArr[itemIdx - 1] = 0;
	
				// Decrement item index
				itemIdx--;
	
				// Update subtotal display
				subtotalTF.setText(currencyF.format(nOrderSubtotal));
	
				// Clear the corresponding text field for removed item
				switch (itemIdx) {
					case 0: m1TF.setText(""); break;
					case 1: m2TF.setText(""); break;
					case 2: m3TF.setText(""); break;
					case 3: m4TF.setText(""); break;
					case 4: m5TF.setText(""); break;
				}
	
				// Update labels
				idL.setText("Enter item ID for Item #" + (itemIdx + 1) + ":");
				quantityL.setText("Enter quantity for Item #" + (itemIdx + 1) + ":");
				itemL.setText("Details for Item #" + itemIdx + ":");
				subtotalL.setText("Current Subtotal for " + itemIdx + " item(s):");
				mTitleL.setText("<html><b>Your Shopping Cart Currently Contains " + itemIdx + " Item(s)</b></html>");
	
				// Re-enable search button if previously disabled
				searchBtn.setEnabled(true);
	
				// Disable delete and checkout buttons if cart is empty
				if (itemIdx == 0) {
					checkOutBtn.setEnabled(false);
					deleteBtn.setEnabled(false);
					mTitleL.setText("<html><b>Your Shopping Cart is Currently Empty</b></html>");
				}
			} else {
				JOptionPane.showMessageDialog(null, "No items to delete.", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// Check out button
	private class CheckOutBtnH implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			String finalOutput = "";
			
			FileWriter transaction;
			PrintWriter printer = null;
			
			try {
				transaction = new FileWriter("transaction.csv", true);
				printer = new PrintWriter(transaction);
				
				StringBuilder outStrBlr = new StringBuilder();
				
				// Final total after tax
				nTax = nOrderSubtotal * TAX;
				nOrderTotal = nOrderSubtotal + nTax;
				
				// Format time
			  	LocalDateTime myDateObj = LocalDateTime.now();
				ZonedDateTime zonedDateTime = myDateObj.atZone(ZoneId.systemDefault());
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
				DateTimeFormatter myFormatObj2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm:ssa z");
				DateTimeFormatter myFormatObj3 = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm:ss a z");
			  	String formattedDate = myDateObj.format(myFormatObj);
				String formattedDate2 = zonedDateTime.format(myFormatObj2);
				String formattedDate3 = zonedDateTime.format(myFormatObj3);
			  
				// Final check out button message
				finalOutput = "Date: " + formattedDate2 + "\n\nNumber of line items: " + itemIdx +" \n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal: \n\n";
				
				for (int i = 0 ; i < itemIdx; i++) {
					finalOutput += ((i + 1) + ". " + idARR[i] + " " + titleArr[i] + " " + currencyF.format(priceArr[i]) + " " + quantityArr[i] + " " + decimalF.format(discountArr[i]) + " " + currencyF.format(subtotalArr[i]) + "\n");
					
					// Same thing but for transaction file
					outStrBlr.append( formattedDate + ", " + idARR[i] +", " + titleArr[i] + ", "+ priceArr[i] + ", " + quantityArr[i] + ", " + discountArr[i] + ", " + currencyF.format(subtotalArr[i]) + ", " + formattedDate3 + "\n");
				}
				
				printer.print(outStrBlr.toString());
			} 	catch(IOException ioException) {
			}
			finally {
				printer.close();
			}
			
			finalOutput += "\n\nOrder subtotal: " + currencyF.format(nOrderSubtotal) +"\n\nTax rate:      " + percentF.format(TAX) + "\n\nTax amount:    " + currencyF.format(nTax) + "\n\nORDER TOTAL:   " + currencyF.format(nOrderTotal) + "\n\nThanks for shopping at Nile Dot Com!";
			JOptionPane.showMessageDialog(null, finalOutput, "Nile Dot Com - FINAL INVOICE", JOptionPane.INFORMATION_MESSAGE);
			

			// Update visibility 
			idTF.setEditable(false);
			quantityTF.setEditable(false);
			
			deleteBtn.setEnabled(true);
			searchBtn.setEnabled(false);
			checkOutBtn.setEnabled(false);
			
		}
	}
	
	// New order button
	private class NewOrderBtnH implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			// Reset 
			idARR = new String[maxArraySize];
			titleArr = new String[maxArraySize];
			priceArr  = new double[maxArraySize];
			quantityArr = new int[maxArraySize];
			discountArr = new double[maxArraySize];
			subtotalArr = new double[maxArraySize];
			
			id = ""; title = ""; outputStr = ""; maxArraySizeStr = ""; price = ""; stock = ""; quantity = ""; subtotal = ""; discount = ""; orderSub = "";
			nPrice = 0; nSubtotal = 0; nOrderSubtotal = 0; nOrderTotal = 0; nDiscount = 0; nTax = 0;
			itemIdx = 0; itemQuantity = 0; maxArraySize = 0;
			
			idL.setText("Enter item ID for Item #" + (itemIdx + 1) + ":");
			quantityL.setText("Enter quantity for Item #"+ (itemIdx + 1) + ":");
			itemL.setText("Details for Item #"+ (itemIdx + 1) + ":");
			subtotalL.setText("Current Subtotal for " + itemIdx + " item(s):");
      		mTitleL.setText("<html><b>Your Shopping Cart is Currently Empty</b></hmtl>");

			
			idTF.setText("");
			idTF.setEditable(true);
			quantityTF.setText("");
			quantityTF.setEditable(true);
			itemTF.setText("");
			subtotalTF.setText("");
      		m1TF.setText("");
		  	m2TF.setText("");
		  	m3TF.setText("");
		  	m4TF.setText("");
		  	m5TF.setText("");

			searchBtn.setEnabled(true);
			searchBtn.setText("Search For Item #" + (itemIdx + 1));
			addBtn.setText("Add Item #" + (itemIdx + 1) + " To Cart");
			addBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
			checkOutBtn.setEnabled(false);
		}
	}
	
	// Exit button
	private class ExitBtnH implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			System.exit(0);
		}
	}
	public static void main(String[] args) {
		NileDotCom project = new NileDotCom();
		project.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		project.setVisible(true);
	}
}
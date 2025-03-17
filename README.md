# Event Driven Programming Overview
Created a Java GUI app simulating an e-store called Nile Dot Com. Users can add in-stock items to a cart, total costs (including tax), generate an invoice, and log transactions. The app will allow users to manage their cart, review costs, and complete purchases efficiently while maintaining a transaction history for future reference.

## Enterprise Computing Project Objectives:  
- Practice event-driven programming using Java-based GUIs.  
- Refresh basic Java skills.  
- Simulate a high-level enterprise application.  

## Description:  
Develop a standalone Java GUI application that simulates an e-store called **Nile Dot Com**. The application allows users to:  
- Add in-stock items to a shopping cart.  
- Calculate total costs (including tax).  
- Generate an invoice.  
- Log each transaction to a file.  

## Features:  
- **Main GUI**:  
  - Input area for item details.  
  - Display area for cart contents.  
  - Six functional buttons for interaction.  

- **Inventory File** (`inventory.csv`):  
  - Contains item ID, description, stock status, quantity, and unit price.  

- **Transaction Log** (`transactions.csv`):  
  - Logs each transaction with a unique ID based on the date and time.  

## Restrictions:  
- Shopping cart holds a maximum of **5 items**.  
- Tax rate: **6%**.  
- Discount rates:  
  - 1–4 items = 0%  
  - 5–9 items = 10%  
  - 10–14 items = 15%  
  - 15+ items = 20%  

## Input and Output:  
- **Input**: `inventory.csv`  
- **Output**: GUI display, invoice message, `transactions.csv`  

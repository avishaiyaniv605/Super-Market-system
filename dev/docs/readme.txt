## Submitters:	Alex Abramov	314129438
## 		Avishai Yaniv	307916023

# Supler-Li Suppliers System

Welcome to Super-Li suppliers system, in this tutorial we will guide you through the general use of the application.

1. Description:
In this module you will be able to add, edit, watch and manipulate suppliers cards, contacts, items and contracts.
- Supplier card represents the suppliers' basic details, such as company number, bank account and payment terms.
- Contact describes a person Super-Li will contact to discuss for a certain problem.
- Contract contains the agreement between Super-Li and a supplier. It might contain delivery days, items an a 	discount for a big quantity bought for a certain item.
- Products are basic objects which have a catalog number, given by the supplier. Item, is the “real” product object and so, can be really used, thus has a serial number. For example, suppose we have the new Galaxy S10, its product catalog number 115, made by Samsung. A particular S10, which can be held, might have the following serial number- 115.00110102.

2. Menus and options:
- Main menu:
- Add supplier
- Show information
- Change supplier details
- Remove supplier
- Exit

2.1. Adding a new supplier:
* If you wish to add a new supplier you will be guided to add the following details:
- Supplier details- mandatory.
- Contact details- optional, which contain contact name, phone number and email address.
- Contract details- optional, which contain delivery days (if has), items (if included) and quantity list (if agreed).

2.2. Showing information:
* The following details can be shown:
- Suppliers, contacts, contracts, items in contracts and quantity list agreements.

2.3. Changing details:
* The following changes can be done:
- Bank account change, payment terms change and contact details change.
- Contact removal.
* The following details can be added:
- Contracts.
- Contact.

2.4. Supplier removal:
* This option will only remove supplier’s contacts. It will not clear all data collected in order to keep the system backed-up for previous buyings and contracts.

***** Please notice: each option might ask some questions to validate information to change and new information to insert or update. *****

3. Supported applications and frameworks:
* Java 11.
* Hibernate 5.4.1.
* Sqlite.

4. Data contained in system:
* The database is injected with the following data:
4.1. Suppliers:
- Company id 1, bank account 123456 and payment terms: “pay on 1st”.
- Company id 2, bank account 654321 and payment terms: “pay on 10th”.

4.2. Contacts:
- Yossi, phone: 050505011, email: yossi@gmail.com
- Yaacov, phone: 05111111, email: yaacov@gmail.com
- Moshe, phone: 050123123, email: moshe@gmail.com
- Isaac, phone: 0501235050, email: isaac@gmail.com

4.3. Contracts:
- Id 1 with permanent delivery days.
- Id 2 without permanent delivery days.

4.4. Delivery Days:
- Sunday deliveries which is part of contract id 1, supplied by company 1.

4.5. Products:
- Catalog number 1, “Bamba” made by “Osem”
- Catalog number 2, “Bisli” made by “Osem”

4.6. Items:
- 3 “Bamba” items made by “Osem” supplied by supplier 2, contained in contract 2, with price of 3.2
- 3 “Bamba” items made by “Osem” supplied by supplier 1, contained in contract 1, with price of 3.5.
- 3 “Bisli” items made by “Osem” supplied by supplier 1, contained in contract 1, with price of 2.1.

4.7. Quantity List:
- Discount of 1.5% for 100 units of Osem’s “Bamba” bought from supplier 2 are contained in contract id 2.


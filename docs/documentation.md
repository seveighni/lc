# Logistics Company website

Direct links:
- [Pages on the website](#pages) </br>
- [Permissions to view pages](#permissions-to-view-pages) </br>
- [Website capabilities with examples](#website-capabilities) </br>

---

## Pages

### Navigation bar

The navigation bar is available on all pages. The below screenshot is the navigation bar which is shown to users who are not authenticated.</br>
![Navigation bar for user with no permissions](img/empty-nav.png?raw=true "EmptyNavBar")</br>

In the below screenshot, the logged in user has permission to view all the pages on the website.</br>
You can see the company name, available pages and on the right- the email of the user which is logged in.</br>
![Navigation bar for user with full permissions](img/full-nav.png?raw=true "FullNavBar")</br>


### Login

This is the page you will first see when accessing our website. You are prompted to login using your email and password.</br>
![Empty login page](img/login-page.png?raw=true "Login")</br>

### Register

If you do not have a registration you can create one on this page.
You are prompted to enter your first and last name, email, type in a password and repeat it. The checkbox at the end is used for registering employees. </br>
If you are registering as an employee, you need to check the checkbox then the registration will be sent as a user, who wants employee rights.</br>
Those rights will be given to you if an administartor approves the request.</br>
![Empty register page](img/register-page.png?raw=true "Register")</br>

### Homepage

When you have logged in, you will be rediredcted to the home page of the website. There you can read some more information about our history and practice.</br>
![Home login page](img/home-page.png?raw=true "Home")</br>

### Profile

When you have logged in, your email will appear on the right corner in the navigation bar. When clicking on it you will be able to choose between two options:</br>
![Profile options](img/profile-opt.png?raw=true "Profile options")</br>

- If you click the "Profile" button, a new page for editing your user profile will be opened:</br>
![Edit profile page](img/profile-edit.png?raw=true "Edit profile")</br>
Here you can only edit your first and last name. The email field is read only. After you finish editing, you can click on the "Update" button to save your changes.

- If you click on the "Logout" button, you will be logged out and redirected to the Login page.</br>

### Offices

As a customer you can view the available offices and filter them by their address.</br>
The search is done by searching for offices which address contains the entered string in the input bar.
You can also choose how many offices are shown on one page. The offices which are active are shown on top, followed by the inactive ones.</br>
![Offices page](img/office-page.png?raw=true "Offices")</br>
Employees and Administrators are also able to create a new office by entering the office address and clicking on the "Create" button on the bottom of the page.</br>

Employees and Administrators are able to edit offices by clicking on the edit icon (under Actions column) next to the office they would like to modify.</br>
![Edit office page](img/office-edit.png?raw=true "Edit office")</br>

### Users

This page is only available for users which have administrative rights. There are two available options to choose from which are shown when clicked on the 'Users' button in the navigation bar:
![Admin navigation bar](img/admin-nav.png?raw=true "Admin navigation bar")</br>

- All:</br>
You can view the registered users and filter them by their email.</br>
The search is done by searching for users which email contains the entered string in the input bar.</br>
![Users page](img/users-page.png?raw=true "Users")</br>

- Waiting for approval:</br>
Administartors can give 'Employee' permissions to the users by clicking on the 'Make employee' button (under the Actions column).
![Emplpoyees waiting for approval page](img/employees-waiting.png?raw=true "Emplpoyees waiting for approval")</br>

### Employees

This page is only available for users which have administrative rights. You can view the registered employees and filter them by their email.</br>
![Employees page](img/employees-page.png?raw=true "Employees")</br>
Administrators are able to edit an employee by clicking on the edit icon (under Actions column) next to the employee they would like to modify.</br>
![Edit employee page](img/employees-edit.png?raw=true "Edit employee")</br>
Here the administrator can modify:
- The office which they work at. When clicking on the adress a drop down with all the active offices will be shown.
- Update the type of the employee. A drop down with these two options will appear: Office worker/ Delivery worker.
- Update their status. By checking or unchecking the checkbox next to "Is active".</br>

The modifications will be saced by clicking the 'Update' button.

### Rates


This page is only available for users which have 'Administrator' or 'Employee' rights. You can view the available shipping rates and filter them by their name.</br>
![Rates page](img/rates-page.png?raw=true "Rates")</br>
Employees and Administrators are also able to create a new shipping rate by entering the name, price per KG and flat price in the input fields on the button of the page. By clicking on the "Create" button on the bottom of the page, the new rate will be created.</br>

Administrators and Employees are able to edit a rate by clicking on the edit icon (under Actions column) next to the rate they would like to modify.</br>
![Edit rate page](img/rates-edit.png?raw=true "Edit rate")</br>
Here the Administrator or Employee can modify:
- The name of the rate.
- The price per kilogram.
- The flat price for this shipping rate.</br>

The modifications will be saced by clicking the 'Update' button.

### Customers

This page is only available for users which have 'Administrator' or 'Employee' rights. You can view the registered customers and filter them by their email.</br>
![Customers page](img/customers-page.png?raw=true "Customers")</br>

### Parcels

As a customer, when you open the Parcels page, you can view the parcels which are sent by you or being delivered to you.</br>
![Parcels page seen by customer](img/parcels-customer.png?raw=true "Parcels seen by customer")</br>
You can choose how many parcels are shown on one page. The parcels which are pre-paid are shown on top, followed by the unpaid ones.</br>
You can also filter the parcels by a combination of the following fields:</br>
- Start date and End date: the orders which have been created between these two dates will be listed.
- Status: The available values for status are 'NEW','IN_DELIVERY','DELIVERED'
- Paid: If checked, the search will return only parcels which have been paid for. Either they have been pre-paid or have been paid to on delivery.</br>
</br>
As an Employee or an Administrator, you can filter the parcels by these additional fields:
- Responsible:
- Sender:
- Receiver: </br>
The folowing screenshot is what the Parcels page looks like to an Employee or an Administrator:
![Parcels page seen by employee/ administrator](img/parcels-page.png?raw=true "Parcels seen by employee/ administrator")</br>
On the bottom left corner there will be statistics shown about how much the income from the loaded parcels that are paid is and the sum of the unpaid parcels.

</br>
Employees and Administrators are able to edit parcels by clicking on the edit icon (under the Actions column) next to the parcel they would like to modify. When clicked, the following form for editting the desired parcel will be shown:</br>

![Edit parcel page](img/parcels-edit.png?raw=true "Edit parcel")</br>

</br>
The fields that can be modified are:
- Status:
- Checkbox:
You can save the modifications by clicking on the 'Update' button on bottom right corner. Every time a parcel has been updated, the date and time for it's 'Status updated at' field will be updated.</br>
</br>
Employees are also able to create a new parcel by clicking on the 'Create New' button shown only to them from the navigation bar:

![Employee navigation bar](img/employee-nav.png?raw=true "Employee navigation bar")</br>

When clicked, the following form for creating a new parcel will be loaded:</br>

![Create parcel page](img/parcels-create.png?raw=true "Create parcel")</br>
The input fields for creating a new parcel are:
- From: You need to enter the email of the customer which is sending the parcel.
- To: You need to enter the email of the customer which the parcel will be delivered to.
- Address: If the parcel will be delivered to a specific addreess, you need to enter that address in this field.
- Office: If the parcel will be deliverd to an office, you need to select which office the customer wants it delivered to from the drop-down menu. Only active officess will be shown as an option.
- Weight in kg: Enter the weight of the parcel.
- Rate: Choose one of the availabe rates from the drop-down menu.
- Is paid: Check the box if the parcel order has been pre-paid or leave it unchecked if the parcel order will be paid for on delivery.

By clicking the 'Create' button on the bottom right corner you will create a new parcel order.

---

## Permissions to view pages

Unauthenticated users have permissions to only view Login and Register pages.

### Home 

The Home page can be accessed by any authenticated user.

### Profile 

The Profile page can be accessed by any authenticated user.

### Offices

Customers can access the Offices page, but can only view and filter offices.</br>

Employees and Administartors can access the Offices page and have permissions to view, filter through offices, edit already available ones and create new offices.

### Users

Customers and Employees do not have permissions to view the Users page.</br>

Administators can access the Users page and view, filter through users and approve registrations which have asked for employee permissions.

### Employees

Customers and Employees do not have permissions to view the Employees page.</br>

Administators can access the Employees page and have permissions to view, filter through employees and modify them.

### Rates

Customers do not have permissions to view the Rates page.</br>

Employees and Administartors have access to the Rates page and can view, filter through rates, edit already available ones and create new rates.

### Customers

Customers do not have permissions to view the Customers page.</br>

Employees and Administartors have access to the Customers page and can view and filter through customers.

### Parcels

Customers have access to the Parcels page, but they can only view and filter through parcels which are sent by them or are being delivered to them.</br>

Employees and Administartors can access this page and view, filter through parcels and edit already available ones.</br>

Only Employees have permission to create new parcel entries.

---

## Website capabilities

### Process of registering as an employee:
- Load the website.
- Click on the 'Register' button.
- Enter needed information in the shown form and be sure to check the 'Require Employee permissions' checkbox at the end of the form. Example for filled in form:

![Employee registering](img/register-employee.png?raw=true "Employee registering")</br>

- Click Register.
- Wait for administartor to approve your request.

### Adding a new Rate
- Load the website. You will be prompted to log in.
- Log in as an Employee or Administrator.
- Open 'Rates' page.
- Enter the needed information in the input fields on the bottom of the page like so:

![Creating a new rate](img/rates-create.png?raw=true "Creating a new rate")</br>

- Click on the 'Create' button on the bottom right corner.

### Creating a new parcel
- Load the website. You will be prompted to log in.
- Log in as an Employee.
- Click on 'Parcels' button from the navigation bar and select option 'Create new'.
- Enter the needed information in the input fields in the shown form.
- Click on the 'Create' button on the bottom right corner.</br>

Here are two examples of a correctly filled form:

![Creating a new parcel](img/parcels-create-examples.png?raw=true "Creating a new parcel")</br>


### Examples for searching for parcels using filters

![Parcel filter example 1](img/parcels-search1.png?raw=true "Parcel filter example 1")</br>
The above screenshot is the result of parcels being filtered by:
- specific sender: customer with email "customer2@test.com"
- specific start and end date: from 06.02.2024 to 08.02.2024
- chosen paid option: Paid (already paid for)

![Parcel filter example 2](img/parcels-search2.png?raw=true "Parcel filter example 2")</br>
The above screenshot is the result of parcels being filtered by:
- specific responsible employee: employee with email "employee2@test.com"
- specific sender: customer with email "customer2@test.com"
- specific status: parcels in status 'NEW'

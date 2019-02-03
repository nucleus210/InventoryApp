Inventory App
==============

ABDN project.
==============

Project demonstrate basic Sql database usage.

Few words about project:

    Project has Inventory Main Activity where is included Tab Layout with View Pager. Inside Tab Layout has Three main Fragment.

    --> Welcome fragment will provide some basic user manual information;

    --> Inventory Fragment populate data from Sql database in Table format. I was in trouble with space for this I make Scroll View and Inside Horizontal Scroll. In this way if table is bigger than screen is steel accessible from user. I am no sure is a write way, but I make it like this. Maybe if make somehow to collapse columns in runtime will be better;

    --> Inventory Item Fragment populate data from database into Recycle List View. Photos are stored in Internal Storage Private Directory. In database are stored only Urls to the files.
    I remove demo photos from density directory to raw directory to reduce project size.

    --> In additional have Inventory Add Activity for adding new record to database table.

    --> App have Search view inside tool bar and user can query database by product names phrases. All Fragments update from search phrase immediately.

    --> Data Base Helper class is located queries and real database fetching is implemented inside Live Data View Model. This is nice because give flexibility to update all view.

    Inventor Fragment and Inventory Item Fragment implements observers and when data change update table and list immediately. I that way I handle configuration changes.

    --> Added drawer, but is not full functional and Tool Bar too. This in next stage;

    --> Contractor class where are table and column names;

    --> Inventory class for Inventory object;

    --> In Inventory Item List Fragment I add few buttons to increment and decrement quantity and sell button;

    --> Invetory Details Activity. When user click on Item from list intent Detail Activity. Inside has EditText, buttons to change quantity. 
        In bottom tool bar has add to database button, edit fields button, delete row entries button and Order button;

    In rubric I see that order button handle call intent and for that i set call icon. Is a Order button.

    --> Inventory Add Activity - where can be inserted new records to database.

![alt text](https://image.ibb.co/kxFmAz/Inventory.png)

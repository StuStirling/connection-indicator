# Connection Indicator
A very small library to implement a connection indicator view. These connections may be GPS, Network or other loading indicators.

![](https://github.com/StuStirling/connection-indicator/blob/ss-sample/assets/sample-demo.gif?raw=true)

This is only an initial version of the library and I will be making improvements as I go. 

At the moment there aren't any attribute setters in code. All attributes must be set via XML.

    <com.stustirling.connectionindicator.ConnectionIndicatorView
                    android:layout_width="60dp"
                    android:layout_height="wrap_content"
                    android:id="@+id/indicator"
                    android:layout_centerHorizontal="true"
                    android:layout_marginBottom="2dp"
                    connection_indicator:emptyBarColor="@color/greyColor"
                    connection_indicator:solidSearchBarColor="@color/whiteColor"
                    connection_indicator:bars="4"
                    connection_indicator:animType="incremental"
                    connection_indicator:connectionLevels="4"/>

There are two `animTypes`:

  - flash = Alternates between connection level of 0 and max.
  - incremental = Each bar is turned solid in turn until all are solid and then it goes back to 0.
  
To begin search mode:

    indicatorView.startSearching();

To stop searching:

    indicatorView.stopSearching();

To display a certain "connection level" call the following:

    indicatorView.displayConnectionLevel( 2 );

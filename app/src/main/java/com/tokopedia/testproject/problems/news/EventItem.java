package com.tokopedia.testproject.problems.news;

import android.util.EventLog.Event;
import com.tokopedia.testproject.problems.news.ListItem;

import java.util.Date;

public class EventItem extends ListItem {

    private Event event;

    @Override
    public int getType() {
        return ListItem.EVENT_TYPE;
    }

}

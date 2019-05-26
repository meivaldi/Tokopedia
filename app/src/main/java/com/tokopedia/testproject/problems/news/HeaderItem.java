package com.tokopedia.testproject.problems.news;

import com.tokopedia.testproject.problems.news.ListItem;

import java.util.Date;

public class HeaderItem extends ListItem {

    private Date date;

    @Override
    public int getType() {
        return ListItem.HEADER_TYPE;
    }
}

package com.shaweibo.biu.model;


import java.util.ArrayList;
import java.util.List;

public class FavoListModel
{
    private List<FavoModel> favorites = new ArrayList<FavoModel>();

    public MessageListModel toMsgList() {
        MessageListModel msg = new MessageListModel();
        msg.total_number = 0;
        msg.previous_cursor = 0;
        msg.next_cursor = 0;

        List<MessageModel> msgs = (List<MessageModel>) msg.getList();

        for (FavoModel favo : favorites) {
            msgs.add(favo.status);
        }

        return msg;
    }
}

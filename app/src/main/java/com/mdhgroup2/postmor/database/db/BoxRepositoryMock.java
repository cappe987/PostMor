package com.mdhgroup2.postmor.database.db;

import androidx.lifecycle.LiveData;

import com.mdhgroup2.postmor.database.DTO.MessageContent;
import com.mdhgroup2.postmor.database.DTO.MsgCard;
import com.mdhgroup2.postmor.database.interfaces.IBoxRepository;

import java.util.List;

public class BoxRepositoryMock implements IBoxRepository {
    List<MsgCard> l;

    List<MsgCard> l2;

    List<MsgCard> l3;

    List<MsgCard> l4;

//    MutableLiveData<Integer> liveInt = new MutableLiveData<>();

    public BoxRepositoryMock(){
//        liveInt.setValue(10);

        MsgCard u1 = new MsgCard();
        u1.Name = "Ann-Marie Josefsson";
        u1.Address = "Isterbarnsgatan 12";
        u1.Picture = null;
        u1.IsFriend = true;
        u1.DateStamp = Utils.makeDate(2019, 12, 19);
        u1.MsgID = 1;

        MsgCard u2 = new MsgCard();
        u2.Name = "Arne Askersund";
        u2.Address = "Mastrostvägen 13";
        u2.Picture = null;
        u1.IsFriend = true;
        u1.DateStamp = Utils.makeDate(2019, 12, 11);
        u2.MsgID = 2;

        MsgCard u3 = new MsgCard();
        u3.Name = "Swedish Chef";
        u3.Address = "Muppetgatan 14";
        u3.Picture = null;
        u1.IsFriend = false;
        u1.DateStamp = Utils.makeDate(2019, 11, 23);
        u3.MsgID = 3;

        MsgCard u4 = new MsgCard();
        u4.Name = "Brittish Chef";
        u4.Address = "Fish'n chipsstreet 9";
        u4.Picture = null;
        u4.IsFriend = false;
        u4.DateStamp = Utils.makeDate(2019, 3, 2);
        u4.MsgID = 4;

        l.add(u1);
        l.add(u2);
        l.add(u3);

        l2.add(u1);
        l2.add(u2);

        l3.add(u2);
        l3.add(u3);

        l4.add(u1);
        l4.add(u3);
        l4.add(u4);
    }


    @Override
    public List<MsgCard> getAllMessages() {
        return l;
    }

    @Override
    public List<MsgCard> getAllMessages(int ID) {
        return l2;
    }

    @Override
    public List<MsgCard> getInboxMessages() {
        return l3;
    }

    @Override
    public List<MsgCard> getInboxMessages(int ID) {
        return l;
    }

    @Override
    public List<MsgCard> getOutboxMessages() {
        return l4;
    }

    @Override
    public List<MsgCard> getOutboxMessages(int ID) {
        return l3;
    }

    @Override
    public int getNewMessageCount() {



        return 14;
    }

    @Override
    public LiveData<Integer> outgoingLetterCount() {
        LiveData<Integer> ld =  new LiveData<Integer>() { {
                postValue(7);
            }
        };

        return ld;
    }

    @Override
    public MessageContent getMsgContent(int MsgID) {
        MessageContent msg = new MessageContent();
        msg.Images = null;
        msg.Text = "Hello Philip!\n Last time I went to the market I bought some potatoes."
                + "I sent them in a package to you, and hopefully you will receive them before"
                + "you get this letter.\n\n Love, Grandma";
        return msg;
    }

    @Override
    public int fetchNewMessages(){
        return 0;
    }
}

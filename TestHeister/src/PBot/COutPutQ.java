package PBot;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Simple queue implementation with a extra funktion in the dequeue method and some extras.
 * Created by Hagen on 05.01.2016.
 */
public class COutPutQ {

    private RalphBot _ralph;
    private ArrayList<String> _queueValues;
    private CMessagActivatorThread _activator;
    private String _channelName;

    public COutPutQ(RalphBot _ralph,String _channelName){
        this._ralph = _ralph;
        this._channelName = _channelName;
        _queueValues = new ArrayList<>();
        _activator = new CMessagActivatorThread(this);

    }

    /**
     * Simple enque method with a checkup if the message is a null Pointer
     * @param message
     */
    public void enque(String message){
        if(message!=null){
            _queueValues.add(message);
        }
    }

    /**
     * Simple deque message with the addtion of sending the message via the ralphBot. This method gets called by the
     * CMessageActivatorThread object.
     */
    public void deque(){
        _ralph.sendMessage(_channelName,_queueValues.get(0));
        _queueValues.remove(0);
    }

    /**
     * Simple checkup if the q is empty
     * @return
     */
    public boolean isQEmpty(){
        return _queueValues.isEmpty();
    }


}

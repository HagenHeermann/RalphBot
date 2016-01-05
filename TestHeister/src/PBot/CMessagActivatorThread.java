package PBot;

/**
 * This thread exists because some user started spamming bot commands and got my bot and me banned for 8 hours from
 * Tiwtch irc thanks again MingLee.
 * Created by Hagen on 05.01.2016.
 */
public class CMessagActivatorThread extends Thread{

    private COutPutQ _q;

    public CMessagActivatorThread(COutPutQ _q){
        this._q = _q;
    }

    /**
     * Simple run method. if the q isnt empty the thread dequeues the first element of the queue and sends with that
     * the message into the chat. After that he goes to sleep for 30sec to avoid being banned by twitch irc. In the
     * case that the queue is empty it does nothing.
     */
    public void run(){
        System.out.println("Message activator thread started");
        while(true){
            if(!_q.isQEmpty()){
                _q.deque();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

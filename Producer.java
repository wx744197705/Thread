package producer;

/**
 * @author wxx
 * @date 2018/6/6 18:53
 */
public class Producer implements Runnable{
    private boolean flag = true;

    private Container container;

    Producer(Container container){
        this.container = container;
    }
    @Override
    public void run(){
        while (flag){
            try {
                container.produce();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        this.flag = false;
    }
}

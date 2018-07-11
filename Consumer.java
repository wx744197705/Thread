package producer;

/**
 * @author wxx
 * @date 2018/6/6 18:56
 */
public class Consumer implements Runnable {
    private boolean flag = true;

    private Container container;

    Consumer(Container container){
        this.container = container;
    }
    @Override
    public void run(){
        while (flag){
            try {
                container.consumer();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public void stop(){
        this.flag = false;
    }
}

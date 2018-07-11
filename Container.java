package producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wxx
 * @date 2018/6/6 17:21
 */
public class Container {
    private int count = 0;
    public synchronized void produce(){
        int maxCount = 10;
        try {
            while (count >= maxCount){
                System.out.println("已满，生产等待," + Thread.currentThread() + "挂起");
                this.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        count++;
        System.out.println(Thread.currentThread() + "生产线程生产了1条记录，目前记录数为："+count);
        this.notifyAll();
        try{
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized void consumer(){
        System.out.println(Thread.currentThread() + "进入消费");
        try {
            //用if不加else的情况：当被唤醒时候，不会进行count<=0的判断，直接走count--的步骤，所以会导致出现负数的情况，最小到-2
            //而用while时，wait被唤醒时，自动循环，如果条件不成立，继续进入wait状态
            //if else的情况下，当唤醒所有时，本来挂起的线程不会消费，而用while的线程会进行判断并选择挂起还是消费
            while (count <= 0){
                System.out.println("已空，消费等待," + Thread.currentThread() + "挂起");
                this.wait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        count--;
        System.out.println(Thread.currentThread() +"消费线程消费了1条记录，目前记录数为： "+ count);
        this.notifyAll();     // 通知其它等待该锁的线程
        try{
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException{
        Container container = new Container();
        Producer producer1 = new Producer(container);
        Producer producer2 = new Producer(container);
        Producer producer3 = new Producer(container);
        Consumer consumer1 = new Consumer(container);
        Consumer consumer2 = new Consumer(container);
        Consumer consumer3 = new Consumer(container);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(producer1);
        executorService.execute(producer2);
        executorService.execute(producer3);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.execute(consumer3);
        Thread.sleep(3 * 1000);

        producer1.stop();
        producer2.stop();
        producer3.stop();
        consumer1.stop();
        consumer2.stop();
        consumer3.stop();
        executorService.shutdown();
    }
}

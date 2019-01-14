import java.util.*;
import java.util.concurrent.*;
import java.text.*;

public class Alarm {
  public static Map<Long,String > map = new TreeMap <>();
  public static Map<Integer, Long> dat = new TreeMap<>();
  public static Map<Integer, String> status = new TreeMap<>();
  public static Map<Integer, String> time_alarm = new TreeMap<>();
  public static int k=0;

  private static  void AddAlarm (String letter){
    String[] r = letter.split("-");
    String dt = r[2];
    Date dateNow = new Date();
    SimpleDateFormat ft = new SimpleDateFormat (" dd.MM.yyyy hh:mm");
    Date parsingDate;
    try {
      parsingDate = ft.parse(dt);
      if (parsingDate.getTime() - dateNow.getTime() > 10){
        if (map.containsKey(parsingDate.getTime())) {
          System.out.println(r[1]+r[2]+": denied. in past");
          return;
        } else {
          map.put(parsingDate.getTime(), r[1]);
          dat.put(k, parsingDate.getTime());
          time_alarm.put(k, r[2]);
          status.put(k, "SCHEDULED");
          System.out.println("added");
          k++;
        }
      }
    }catch (ParseException e) {
      System.out.println("Eror for Date " + ft);
    }
  }
  private static void CancelAlarm (String instr) {
    String[] r = instr.split(" ");
    int k = Integer.parseInt(r[1]);
    if (status.containsKey(k)) {
      status.put(k, "CANCELED");
      System.out.println("canceled");
    }
  }
  private static void ListAlarm() {
    for (int i : dat.keySet()) {
      System.out.println("["+i+"]"+time_alarm.get(i)+" ["+status.get(i)+"]");
    }
  }
  private static void ExecAlarm() {
    Date dateNow = new Date();
    for (int i : dat.keySet()) {
      if (dat.get(i)< dateNow.getTime() && status.get(i).equals("SCHEDULED")) {
        System.out.println(map.get(dat.get(i)));
        status.put(i, "EXECUTED");
      }
    }
  }

  public static void main(String[] args) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable task = () -> {
      try {
        TimeUnit.SECONDS.sleep(1);
        ExecAlarm();
      }
      catch (InterruptedException e) {
        System.err.println("task interrupted");
      }
    };
    executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

    while (true) {
      Scanner scan = new Scanner(System.in);
      String str = scan.nextLine();
      if (str.equals("exit")) {
        System.out.println("exit");
        executor.shutdown();
        return;
      }
      else if  (!(str.indexOf("add ") == -1)) AddAlarm(str);
      else if  (!(str.indexOf("list") == -1)) ListAlarm();
      else if  (!(str.indexOf("cancel ") == -1)) CancelAlarm(str);
      else System.out.println("Eror");
    }
  }
}



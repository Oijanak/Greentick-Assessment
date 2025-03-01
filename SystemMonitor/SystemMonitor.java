import java.io.File;
import java.util.Scanner;

public class SystemMonitor{

    public static void main(String[] args) {

        Scanner sc =new Scanner(System.in);
        while(true){
        System.out.println("Enter logfile file path");
        String logFile=sc.nextLine();
        
        File file=new File(logFile);
        System.out.println(file.getAbsolutePath());
        final String alertLogFile="alert.log";
        
            if(file.exists()){
                ProcessLogUtils.monitorLogFile(logFile, alertLogFile);
                sc.close();
            }
            else{
                System.out.println("ERROR: Log file doesn't exist");
            }    
        }
       
        
    }
}
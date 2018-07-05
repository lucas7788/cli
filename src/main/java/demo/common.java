package demo;

import com.github.ontio.OntSdk;

import java.util.Map;

public class common {
    public static void waitResult(OntSdk ontSdk,String txhash) {
        int notInpool = 0;
        Object obj = null;
        for(int i=0;i<20;i++){
            try{
                obj = ontSdk.getConnect().getSmartCodeEvent(txhash);
                if(obj== null|| obj.equals("")){
                    Thread.sleep(1000);
                    Object objTxState = ontSdk.getConnect().getMemPoolTxState(txhash);
                    if(objTxState != null){
                        System.out.println("transaction state: " + objTxState);
                    }
                    continue;
                }else if(((Map)obj).get("Notify") != null){
                    System.out.println(obj);
                    if(((Map)obj).get("State").equals(1)){
                        System.out.println("transaction success");
                    }else {
                        System.out.println("transaction failed");
                    }
                    return;
                }
            }catch (Exception e){
                if (e.getMessage().contains("UNKNOWN TRANSACTION") && e.getMessage().contains("getmempooltxstate")) {
                    notInpool++;
                    if ((obj.equals("") || obj == null) && notInpool >1){
                        System.out.println(obj);
                        System.out.println("transaction failed");
                    }
                } else {
                    continue;
                }
            }
        }
    }
}

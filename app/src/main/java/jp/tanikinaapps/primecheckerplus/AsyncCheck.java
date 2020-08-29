package jp.tanikinaapps.primecheckerplus;

import android.os.AsyncTask;

import java.util.ArrayList;

public class AsyncCheck extends AsyncTask<Integer,String,String[]> {
    int searchNum;
    private Listener listener;

    @Override
    protected String[] doInBackground(Integer... params) {
        String[] result = new String[2];
        searchNum = params[0];
        if(isPrime(searchNum)){
            result[0] = "1";
            result[1] = null;
        } else{
            result[0] = "0";
            result[1] = totalFactorization(params[0]);
        }
        return result;
    }

    private Boolean isPrime(int checkNumber){
        switch (checkNumber){
            case 0:
            case 1:
                return false;
            case 2:
                return true;
            default:
                for(int i=2;i<checkNumber;i++){
                    if(checkNumber % i == 0){
                        return false;
                    }
                }
                return true;
        }
    }

    private String totalFactorization(int checkNumber){
        ArrayList<Integer> intList = new ArrayList<>();
        for(int i=2;i<=checkNumber;i++){
            if(checkNumber % i ==0){
                intList.add(i);
                checkNumber = checkNumber / i;
                i = 1;
            } else if(i == checkNumber - 1){
                intList.add(checkNumber);
                break;
            }

        }
        StringBuilder stb = new StringBuilder();

        for(int i=0;i<intList.size();i++){
            if(i<intList.size() - 1){
                stb.append(intList.get(i));
                stb.append("×");
            } else{
                stb.append(intList.get(i));
            }
        }
        return stb.toString();
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(String[] result);
    }

    @Override
    protected void onPostExecute(String[] result){
        if (listener != null) {
            listener.onSuccess(result);
        }
    }
}

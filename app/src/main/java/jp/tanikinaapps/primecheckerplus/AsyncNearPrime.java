package jp.tanikinaapps.primecheckerplus;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncNearPrime extends AsyncTask<Integer,Integer,Integer> {
    private Listener listener;

    @Override
    protected Integer doInBackground(Integer... params) {
        int searchNum = params[0];

        int uPrime = upperPrime(searchNum);
        int lPrime = lowerPrime(searchNum);

        if (uPrime - params[0] <= params[0] - lPrime) {
            return uPrime;
        } else {
            return  lPrime;
        }
    }

    private int upperPrime(int num){
        boolean isPrimeBoolean = false;

        while(!isPrimeBoolean){
            boolean checkNum;
            checkNum = isPrime(num);
            if(checkNum){
                isPrimeBoolean = true;
                return num;
            }
            num++;
        }
        return 0;
    }

    private int lowerPrime(int num){
        boolean isPrimeBoolean = false;

        while(!isPrimeBoolean){
            boolean checkNum;
            checkNum = isPrime(num);
            if(checkNum){
                isPrimeBoolean = true;
                return num;
            }

            if(num == 1){
                return 0;
            } else{
                num--;
            }

        }
        return 0;
    }

    private Boolean isPrime(int num){
        switch (num){
            case 0:
            case 1:
                return false;
            case 2:
                return true;
            default:
                for(int i=2;i<num;i++){
                    if(num % i == 0){
                        return false;
                    }
                }
                return true;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(Integer result);
    }
}

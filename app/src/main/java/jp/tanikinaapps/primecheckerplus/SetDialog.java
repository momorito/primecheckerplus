package jp.tanikinaapps.primecheckerplus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SetDialog extends DialogFragment {

    public static class ResultDialog extends DialogFragment implements View.OnClickListener {
        AlertDialog dialog;
        AlertDialog.Builder alert;
        View alertView;
        String[] result;
        TextView resultNear,resultInst,resultInst2,resultShare;
        int searchNum;
        boolean isPrime;

        ResultDialog(int searchNum ,String[] result) {
            this.result = result;
            this.searchNum = searchNum;

            if(result[0].equals("0")){
                isPrime = false;
            } else{
                isPrime = true;
            }
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            alert = new AlertDialog.Builder(getActivity());


            if (getActivity() != null) {
                alertView = getActivity().getLayoutInflater().inflate(R.layout.dialog_result, null);
            }

            alert.setTitle("検索結果");
            resultInst = alertView.findViewById(R.id.result);
            resultInst2 = alertView.findViewById(R.id.result2);
            resultNear = alertView.findViewById(R.id.resultNear);
            resultShare = alertView.findViewById(R.id.resultShare);
            TextView resultClose = alertView.findViewById(R.id.resultClose);
            resultClose.setOnClickListener(this);
            resultShare.setOnClickListener(this);
            resultNear.setOnClickListener(this);

            if(isPrime == true){
                resultInst.setText(String.valueOf(searchNum) +" は素数です。");
                resultInst2.setText("");
                resultNear.setVisibility(View.INVISIBLE);
            } else{
                resultInst.setText(String.valueOf(searchNum) +" は素数ではありません。");
                resultInst2.setText("=" + result[1]);
                resultNear.setVisibility(View.VISIBLE);

                if(searchNum == 1 ){
                    resultInst2.setVisibility(View.INVISIBLE);
                }

                if(searchNum == 0){
                    resultInst2.setVisibility(View.INVISIBLE);
                    resultNear.setVisibility(View.INVISIBLE);
                }
            }



            alert.setView(alertView);
            dialog = alert.create();
            dialog.show();

            return dialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.resultNear:
                    resultInst.setText("直近の素数を検索中…");
                    resultNear.setVisibility(View.INVISIBLE);
                    resultShare.setVisibility(View.INVISIBLE);
                    resultInst2.setVisibility(View.INVISIBLE);
                    AsyncNearPrime asyncNearPrime = new AsyncNearPrime();
                    asyncNearPrime.setListener(createListener());
                    asyncNearPrime.execute(searchNum);
                    //dismiss();
                    break;
                case R.id.resultClose:
                    dismiss();
                    break;
                case R.id.resultShare:
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, shareSentence());
                    startActivity(i);
                    dismiss();
                    Log.d("進捗",shareSentence());
                    break;
            }
        }

        public String shareSentence(){
            final String SHARE_TAG = "#素数チェッカーPlus";
            String tweet;
            if(isPrime){
                tweet = String.valueOf(searchNum) +"は素数です。" + SHARE_TAG;

            } else{
                tweet = String.valueOf(searchNum) +"は素数ではありません。素因数分解すると" + result[1] + "になります。 " + SHARE_TAG;

            }
            return tweet;
        }

        private AsyncNearPrime.Listener createListener(){
            return  new AsyncNearPrime.Listener() {
                @Override
                public void onSuccess(Integer result) {
                    alert.setTitle("検索結果");
                    resultInst.setText("近くの素数は" + String.valueOf(result) + "です。");
                    resultInst2.setVisibility(View.INVISIBLE);
                }
            };
        }
    }
}

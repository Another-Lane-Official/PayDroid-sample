package jp.sample.sample_application;

import jp.alij.paydroid.common.Consts;
import jp.alij.paydroid.common.CustomerChange;
import jp.alij.paydroid.common.InputStatus;
import jp.alij.paydroid.common.QuickChargeStatus;
import jp.alij.paydroid.data.CustomerChangeCallback;
import jp.alij.paydroid.data.TransactionRequest;
import jp.alij.paydroid.data.TransactionResult;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author canu johann
 * アナザーレーン（株）
 * http://www.alij.ne.jp
 *
 */
public class MainActivity extends Activity {

	public SettlementReceiver receiver ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReceiver();
    }
    
    /* クリックlistener */
	public void click(View v){
    	
    	//新規トランザクション
		TransactionRequest tr = new TransactionRequest();
				
    	switch(v.getId()){
    	
    	case R.id.normal_payment:
    	
    		//必須パラメータ
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");
    		
			//任意パラメータ
    		//契約に合わせて表示・非表示を設定してください
    		//設定しない場合はfalseになります（非表示）
			tr.setVisibility_mail(InputStatus.INPUT_STATUS_MANDATORY);
			tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
			tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
			
	    	//SDKを呼び出す
			Intent i = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			i.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(i);
    		
    		break;
    		
    		
       	case R.id.quick_charge:
        	
    		//必須パラメータ
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");
    		
			//任意パラメータ
    		//契約に合わせて表示・非表示を設定してください
    		//設定しない場合はfalseになります（非表示）
			tr.setVisibility_mail(InputStatus.INPUT_STATUS_MANDATORY);
			tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
			tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
			
			//契約に合わせて, idとpass(もしくはメール)を渡す
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf"); //customerPassかCustomerMail
			
	    	//SDKを呼び出す
			Intent b = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			b.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(b);
    		
    		break;
    	   		
    	case R.id.quick_charge_second:
    		
    		//必須パラメータ
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");    		
    		
    		//契約に合わせて, idとpass(もしくはメール)を渡す
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		//クイックチャージフラッグ
    		tr.setQuickChargeStatus(QuickChargeStatus.QUICK_CHARGE_SECOND_TIME_AND_MORE);
    		
        	//SDKを呼び出す
    		Intent n = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
    		n.putExtra(Consts.TRANSACTION_INDENT, tr);
    		startActivity(n);
    		
    		break;
    		
    	case R.id.quick_charge_change:
    		
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH"); 
    		
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		tr.setCardName("test");
    		tr.setCardNo("44444444444444444");
    		tr.setCardMonth("12");
    		tr.setCardYear("18");
    		
    		CustomerChange customerChange = new CustomerChange(this, tr, new CustomerChangeCallback() {
				@Override
				public void onCustomerChange(TransactionResult tr) {
					if(tr!= null && tr.getState()== Consts.RESPONSE_STATE_DATA_SUCCESS)
					Toast.makeText(getApplicationContext(), "顧客情報の変更は完了になりました。", Toast.LENGTH_LONG).show();
					else
					Toast.makeText(getApplicationContext(), tr.getMsg(), Toast.LENGTH_LONG).show();
				}
			});
    		customerChange.changeInfo();    		
    		break;    		
    	}    	
    }
    
	/* ReceiverBroadcastを設定*/
    private void setReceiver() {
        IntentFilter filter = new IntentFilter(Consts.RESPONSE_PAYMENT);
        receiver = new SettlementReceiver();
        registerReceiver(receiver, filter);
    }
        
    /*
     * 決済が完了した時または「戻る」ボタンを押すときにBroadcastが配信され
     * 下記receiverが情報を取得し、データを処理します。
     */
    private class SettlementReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			TransactionResult result = arg1.getExtras().getParcelable(Consts.RESPONSE_DATA); 
			
			switch (result.getState()){
			case Consts.RESPONSE_STATE_DATA_SUCCESS:	//決済成功
				Toast.makeText(getApplicationContext(), "決済は完了しました。決済ID：" + result.getTransactionId(), Toast.LENGTH_LONG).show();
				break;				
			case Consts.RESPONSE_STATE_DATA_CANCEL:	//決済中止
				Toast.makeText(getApplicationContext(), "決済が中止されました。" , Toast.LENGTH_LONG).show();
				break;
			}			
		}
	}

}

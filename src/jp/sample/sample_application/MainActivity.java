package jp.sample.sample_application;

import jp.alij.paydroid.common.Consts;
import jp.alij.paydroid.common.CustomerChange;
import jp.alij.paydroid.common.InputStatus;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author canu johann
 * �A�i�U�[���[���i���j
 * http://www.alij.ne.jp
 *
 */
public class MainActivity extends Activity {

	public SettlementReceiver receiver ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Broadcast receiver
        setReceiver();
       
    }
    
    
    @SuppressWarnings("unused")
	public void click(View v){
    	
    	//new transaction
		TransactionRequest tr = new TransactionRequest();
		
		//mandatory parameters
		tr.setAmount(210);
		tr.setSiteId("99999928");
		tr.setSitePass("qYhWsRLH");
		
    	switch(v.getId()){    	
    	
    	case R.id.normal_payment:
    					
			//�C�Ӄp�����[�^
    		//�_��ɍ��킹�ĕ\���E��\����ݒ肵�Ă�������
    		//�ݒ肵�Ȃ��ꍇ��false�ɂȂ�܂��i��\���j
			tr.setVisibility_mail(InputStatus.INPUT_STATUS_MANDATORY);
			tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
			tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
    		
    		break;
    		
    	case R.id.quick_charge_first:
    		
    		//tr.setIsQuickCharge(true);
    		
    		
    		break;
    		
    	case R.id.quick_charge_second:
    		
    		tr.setIsQuickCharge(true);
    		
    		//�_��ɍ��킹��, id��pass(�������̓��[��)��n��
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		break;
    		
    	case R.id.quick_charge_change:
    		
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		tr.setCardName("test");
    		tr.setCardNo("44444444444444444");
    		tr.setCardMonth("12");
    		tr.setCardYear("18");
    		
    		CustomerChange.changeInfo(this, tr, new CustomerChangeCallback() {
				@Override
				public void onCustomerChange(TransactionResult tr) {
					Toast.makeText(getApplicationContext(), tr.getState(), Toast.LENGTH_LONG).show();
				}
			});
    		
    		break;
    		
    	}
    	
    	//SDK���Ăяo��
		Intent i = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
		i.putExtra(Consts.TRANSACTION_INDENT, tr);
		startActivity(i);
    	
    }
    
    private void setReceiver() {
        IntentFilter filter = new IntentFilter(Consts.RESPONSE_PAYMENT);
        receiver = new SettlementReceiver();
        registerReceiver(receiver, filter);
    }
    
    
    /*
     * 
     */
    private class SettlementReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			switch (arg1.getExtras().getInt(Consts.RESPONSE_STATE_DATA)){
			case Consts.RESPONSE_STATE_DATA_SUCCESS:	//���ϐ���
				Toast.makeText(getApplicationContext(), "���ϊ���", Toast.LENGTH_LONG).show();
				break;
				
			case Consts.RESPONSE_STATE_DATA_CANCEL:	//���ϒ��~
				Toast.makeText(getApplicationContext(), "���ϒ��~" , Toast.LENGTH_LONG).show();
				break;
			}
			
		}
	}


}
